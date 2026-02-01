package cz.kotox.crypto.sdk.monitoring.internal.sentry

import android.content.Context
import io.sentry.Sentry
import io.sentry.SentryAttribute
import io.sentry.SentryAttributes
import io.sentry.SentryLogLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.logger.SentryLogParameters
import io.sentry.protocol.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal class SentryProvider(
    private val context: Context,
    private val sentryConfigStore: SentryConfigStore,
    private val isDebug: Boolean,
) {

    internal fun initSentry() {
        val config = sentryConfigStore.getInitialConfig()

        if (!config.dsn.isNullOrBlank()) {
            initSentry(
                dsn = config.dsn,
                deviceId = config.installId,
                isDebug = isDebug,
            )
        }

        syncMonitoringConfig()
    }

    private fun initSentry(
        dsn: String,
        deviceId: String,
        isDebug: Boolean,
    ) {
        SentryAndroid.init(context) { options ->
            options.dsn = dsn
            options.isDebug = isDebug
            options.isEnableAutoSessionTracking = true

            options.logs.isEnabled = true

            // Enable Distributed Tracing
            options.isEnableUserInteractionTracing = true
            options.isEnableUserInteractionBreadcrumbs = true

            options.environment = "production"
            options.isEnableExternalConfiguration = false // Ensure it doesn't look for manifest values

            options.tracesSampleRate = 1.0 // Adjust for production to 0.1 or less
            options.isEnableUserInteractionTracing = true

            // --- Session Replay Configuration ---
            // 1. Whole Session Sampling (0.0 to 1.0)
            // Set to 1.0 to capture every single session for testing.
            options.sessionReplay.sessionSampleRate = 1.0

            // 2. Error-Only Sampling (0.0 to 1.0)
            // Captures a replay only if an error occurs, including 30s of buffer before the error.
            options.sessionReplay.onErrorSampleRate = 1.0

            // CRITICAL for Distributed Tracing:
            // Add your BFF domain here to ensure the sentry-trace header is attached
            // more restricted variant: "^https://bff-service-1029057924274\\.us-central1\\.run.app/.*"
            options.setTracePropagationTargets(listOf("localhost", "bff-service-1029057924274.us-central1.run.app"))
        }

        Sentry.setUser(
            User().apply {
                id = deviceId // This links all subsequent events to this "Anonymous" user
            },
        )

        // Sentry.captureMessage("Sentry init for $deviceId")
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
    private fun syncMonitoringConfig() {
        // Use the application scope so the sync isn't killed if an Activity closes
        MainScope().launch(Dispatchers.IO) {
            try {
//                // 1. Fetch Play Integrity Token (You'll need the Google Play library)
//                val integrityToken = fetchPlayIntegrityToken()
//
//                // 2. Call your Ktor BFF
//                // Pass the installId in a header to link the device to the backend logs
//                val config = sentryStore.getInitialConfig()
//                val response = bffClient.get("https://api.yourcryptoapp.com/config/monitoring") {
//                    header("X-Play-Integrity", integrityToken)
//                    header("X-Device-ID", config.installId)
//                }.body<MonitoringConfig>()

                // 3. Update the store if the DSN has changed
//                if (response.dsn != config.dsn) {
//                    sentryStore.updateDsn(response.dsn)
//
//                    // 4. Re-initialize Sentry immediately with the fresh DSN
//                    // Sentry handles re-init gracefully by updating the active client
//                    initSentry(dsn = response.dsn, deviceId = config.installId)
//                }
            } catch (e: Exception) {
                // Log locally; Sentry isn't ready yet or network failed
                // Timber.e(e, "Failed to sync monitoring config")
            }
        }
    }
}
