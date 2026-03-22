package cz.kotox.crypto.sdk.monitoring

import android.content.Context
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.common.SdkDispatchers
import cz.kotox.crypto.sdk.internal.integrity.IntegrityBuilder
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.monitoring.internal.MonitoringImpl
import cz.kotox.crypto.sdk.monitoring.internal.sentry.SentryConfigStore
import cz.kotox.crypto.sdk.monitoring.internal.sentry.SentryProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public open class MonitoringBuilder(
    private val context: Context,
) {
    private var networkTimeout: Duration = 10.seconds
    private var loggerCallback: SDKLoggerCallback = SDKLoggerCallbackNoOp()
    private var fetchDispatcher: CoroutineDispatcher = SdkDispatchers.fetchDispatcher
    private var diagnosticsEnabled: Boolean = false
    private var bffBaseUrl: String = "https://bff-service-1029057924274.us-central1.run.app"

    /**
     * Enable diagnostic logging for monitoring subsystems (e.g. Sentry payload/envelope logs).
     * Disabled by default.
     */
    public fun setDiagnosticsEnabled(enabled: Boolean): MonitoringBuilder {
        this.diagnosticsEnabled = enabled
        return this
    }

    /**
     * Set the BFF base URL used for fetching the Sentry DSN.
     */
    public fun setBffBaseUrl(url: String): MonitoringBuilder {
        this.bffBaseUrl = url
        return this
    }

    /**
     * Set the fetch dispatcher [CoroutineDispatcher]
     */
    public fun setFetchDispatcher(dispatcher: CoroutineDispatcher): MonitoringBuilder {
        this.fetchDispatcher = dispatcher
        return this
    }

    /**
     * Set the network timeout
     * @param timeout
     */
    @JvmSynthetic
    public fun setNetworkTimeout(timeout: Duration): MonitoringBuilder {
        check(timeout > Duration.ZERO)
        this.networkTimeout = timeout
        return this
    }

    /**
     * Observe logging
     * @param sdkLoggerCallback is instance observing logs of the SDK. No operation callback is applied by default.
     */
    public fun setLoggerCallback(sdkLoggerCallback: SDKLoggerCallback): MonitoringBuilder {
        this.loggerCallback = sdkLoggerCallback
        return this
    }

    public fun build(): Monitoring = MonitoringImpl(
        config = MonitoringConfig(
            networkTimeout = networkTimeout,
            loggerCallback = loggerCallback,
        ),
        sentryProvider = SentryProvider(
            context = context,
            sentryConfigStore = SentryConfigStore(context),
            integrity = IntegrityBuilder(context = context)
                .setLoggerCallback(loggerCallback)
                .build(),
            sentryDiagnosticsEnabled = diagnosticsEnabled,
            bffBaseUrl = bffBaseUrl,
        ),
    )
}
