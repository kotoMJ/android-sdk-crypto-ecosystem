package cz.kotox.crypto.sdk.monitoring.internal

import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.monitoring.MODULE_IDENTIFIER
import cz.kotox.crypto.sdk.monitoring.Monitoring
import cz.kotox.crypto.sdk.monitoring.MonitoringConfig
import cz.kotox.crypto.sdk.monitoring.internal.sentry.SentryProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

internal class MonitoringImpl(
    private val config: MonitoringConfig,
    private val sentryProvider: SentryProvider,
) : Monitoring {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    init {
        installLogger(config)
    }

    private fun installLogger(config: MonitoringConfig) {
        if (config.loggerCallback is SDKLoggerCallbackNoOp) {
            SDKLogger.Companion.uninstall(MODULE_IDENTIFIER)
        } else {
            SDKLogger.Companion.install(tag = MODULE_IDENTIFIER, loggerCallback = config.loggerCallback)
        }
    }

    override fun initMonitoring() {
        sentryProvider.initSentry(scope)
    }

    /**
     * Cancels the scope and stops all background activity.
     */
    override fun shutdown() {
        sentryProvider.close()
        job.cancel()
    }
}
