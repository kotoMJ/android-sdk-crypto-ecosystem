package cz.kotox.crypto.sdk.monitoring.internal

import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.monitoring.MODULE_IDENTIFIER
import cz.kotox.crypto.sdk.monitoring.Monitoring
import cz.kotox.crypto.sdk.monitoring.MonitoringConfig
import cz.kotox.crypto.sdk.monitoring.internal.sentry.SentryProvider
import kotlinx.coroutines.SupervisorJob

internal class MonitoringImpl(
    private val config: MonitoringConfig,
    private val sentryProvider: SentryProvider,
//    private val dispatcher: CoroutineDispatcher,
) : Monitoring {

    private val job = SupervisorJob()
//    private val scope = CoroutineScope(job + dispatcher)

    init {
        installLogger(config)
    }

//    // Keep this lazy so we can init logger first.
//    private val logger by lazy {
//        SDKLogger.getLogger(MODULE_IDENTIFIER)
//    }

    private fun installLogger(config: MonitoringConfig) {
        if (config.loggerCallback is SDKLoggerCallbackNoOp) {
            SDKLogger.Companion.uninstall(MODULE_IDENTIFIER)
        } else {
            SDKLogger.Companion.install(tag = MODULE_IDENTIFIER, loggerCallback = config.loggerCallback)
        }
    }

//    override suspend fun getFreshToken(uniqueRequestHash: String): SdkIntegrityToken? =
//        integrityProvider.getIntegrityToken(uniqueRequestHash)?.let { SdkIntegrityToken(it) }
//
//    override fun getIntegrityHash(content: String): String = content.toSha256Base64()

    override fun initMonitoring() {
        sentryProvider.initSentry()
    }

    /**
     * Cancels the scope and stops all background activity.
     */
    override fun shutdown() {
        job.cancel()
    }
}
