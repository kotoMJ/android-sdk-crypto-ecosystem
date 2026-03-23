package cz.kotox.crypto.sdk.monitoring.internal.sentry

import android.content.Context
import android.util.Log
import cz.kotox.crypto.sdk.internal.integrity.Integrity
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.sentry.ILogger
import io.sentry.Sentry
import io.sentry.SentryAttribute
import io.sentry.SentryAttributes
import io.sentry.SentryLevel
import io.sentry.SentryLogLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.logger.SentryLogParameters
import io.sentry.protocol.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.Closeable
import java.net.URI

private val SENTRY_DSN_REGEX = Regex("^https://[a-f0-9]+@[^/]+/\\d+$")

@Serializable
private data class SentryDsnRequest(val integrityToken: String)

internal class SentryProvider(
    private val context: Context,
    private val sentryConfigStore: SentryConfigStore,
    private val integrity: Integrity,
    private val sentryDiagnosticsEnabled: Boolean,
    private val bffBaseUrl: String,
) : Closeable {

    private val bffHost: String = URI(bffBaseUrl).host

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    }

    internal fun initSentry(scope: CoroutineScope) {
        val config = sentryConfigStore.getInitialConfig()

        if (!config.dsn.isNullOrBlank()) {
            doInitSentry(
                dsn = config.dsn,
                deviceId = config.installId,
            )
        }

        syncMonitoringConfig(scope)
    }

    @Synchronized
    private fun doInitSentry(
        dsn: String,
        deviceId: String,
    ) {
        val isDebug = sentryDiagnosticsEnabled
        SentryAndroid.init(context) { options ->
            options.dsn = dsn
            options.isDebug = isDebug
            if (isDebug) {
                options.setLogger(object : ILogger {
                    private val tag = "Sentry"
                    override fun log(level: SentryLevel, message: String, vararg args: Any?) {
                        val msg = if (args.isNotEmpty()) String.format(message, *args) else message
                        when (level) {
                            SentryLevel.DEBUG -> Log.v(tag, msg)
                            SentryLevel.INFO -> Log.i(tag, msg)
                            SentryLevel.WARNING -> Log.w(tag, msg)
                            SentryLevel.ERROR, SentryLevel.FATAL -> Log.e(tag, msg)
                        }
                    }

                    override fun log(level: SentryLevel, message: String, throwable: Throwable?) {
                        val logFunc: (String, String, Throwable?) -> Int = when (level) {
                            SentryLevel.DEBUG -> { t, m, tr -> if (tr != null) Log.v(t, m, tr) else Log.v(t, m) }
                            SentryLevel.INFO -> { t, m, tr -> if (tr != null) Log.i(t, m, tr) else Log.i(t, m) }
                            SentryLevel.WARNING -> { t, m, tr -> if (tr != null) Log.w(t, m, tr) else Log.w(t, m) }
                            SentryLevel.ERROR, SentryLevel.FATAL -> { t, m, tr -> if (tr != null) Log.e(t, m, tr) else Log.e(t, m) }
                        }
                        logFunc(tag, message, throwable)
                    }

                    override fun log(level: SentryLevel, throwable: Throwable?, message: String, vararg args: Any?) {
                        val msg = if (args.isNotEmpty()) String.format(message, *args) else message
                        log(level, msg, throwable)
                    }

                    override fun isEnabled(level: SentryLevel?): Boolean = true
                })
            }
            options.isEnableAutoSessionTracking = true

            options.logs.isEnabled = true

            // Enable Distributed Tracing
            options.isEnableUserInteractionTracing = true
            options.isEnableUserInteractionBreadcrumbs = true

            options.environment = if (isDebug) "development" else "production"
            options.isEnableExternalConfiguration = false // Ensure it doesn't look for manifest values

            options.tracesSampleRate = 1.0 // Adjust for production to 0.1 or less

            // --- Session Replay Configuration ---
            // 1. Whole Session Sampling (0.0 to 1.0)
            // Set to 1.0 to capture every single session for testing.
            options.sessionReplay.sessionSampleRate = 1.0

            // 2. Error-Only Sampling (0.0 to 1.0)
            // Captures a replay only if an error occurs, including 30s of buffer before the error.
            options.sessionReplay.onErrorSampleRate = 1.0

            // CRITICAL for Distributed Tracing:
            // Add your BFF domain here to ensure the sentry-trace header is attached
            val traceTargets = buildList {
                add(bffHost)
                if (isDebug) add("localhost")
            }
            options.setTracePropagationTargets(traceTargets)
        }

        Sentry.setUser(
            User().apply {
                id = deviceId // This links all subsequent events to this "Anonymous" user
            },
        )

        Sentry.logger().log(
            SentryLogLevel.INFO,
            SentryLogParameters.create(
                SentryAttributes.of(
                    SentryAttribute.stringAttribute("device_id", deviceId),
                    SentryAttribute.stringAttribute("storage_type", "tink_datastore"),
                    SentryAttribute.booleanAttribute("is_cold_start", true),
                ),
            ).toString(),
        )
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun syncMonitoringConfig(scope: CoroutineScope) {
        scope.launch {
            try {
                val requestHash = integrity.getIntegrityHash("sentry/android")
                val token = integrity.getFreshToken(requestHash) ?: return@launch

                val response = httpClient.post("${bffBaseUrl.trimEnd('/')}/sentry/android") {
                    contentType(ContentType.Application.Json)
                    setBody(SentryDsnRequest(integrityToken = token.value))
                    integrity.getSecurityHeader()?.let { header(it.key, it.value) }
                }

                if (!response.status.isSuccess()) return@launch

                val dsn = response.bodyAsText().trim()
                if (dsn.isBlank() || !SENTRY_DSN_REGEX.matches(dsn)) return@launch

                val config = sentryConfigStore.getInitialConfig()
                if (dsn != config.dsn) {
                    sentryConfigStore.updateDsn(dsn)
                    doInitSentry(dsn = dsn, deviceId = config.installId)
                }
            } catch (e: Exception) {
                // Network or integrity failure — Sentry simply won't be initialized with a DSN
            }
        }
    }

    override fun close() {
        httpClient.close()
    }
}
