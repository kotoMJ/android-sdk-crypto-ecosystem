package cz.kotox.crypto.sdk.integrity.internal

import cz.kotox.crypto.sdk.common.domain.model.integrity.SdkIntegrityToken
import cz.kotox.crypto.sdk.integrity.Integrity
import cz.kotox.crypto.sdk.integrity.IntegrityConfig
import cz.kotox.crypto.sdk.integrity.MODULE_IDENTIFIER
import cz.kotox.crypto.sdk.integrity.internal.integrity.IntegrityProvider
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class IntegrityImpl(
    private val config: IntegrityConfig,
    private val integrityProvider: IntegrityProvider,
    private val dispatcher: CoroutineDispatcher,
) : Integrity {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher)

    init {
        installLogger(config)

        scope.launch {
            integrityProvider.prepare()
        }
    }

//    // Keep this lazy so we can init logger first.
//    private val logger by lazy {
//        SDKLogger.getLogger(MODULE_IDENTIFIER)
//    }

    private fun installLogger(config: IntegrityConfig) {
        if (config.loggerCallback is SDKLoggerCallbackNoOp) {
            SDKLogger.uninstall(MODULE_IDENTIFIER)
        } else {
            SDKLogger.install(tag = MODULE_IDENTIFIER, loggerCallback = config.loggerCallback)
        }
    }

    override suspend fun getFreshToken(): SdkIntegrityToken? =
        integrityProvider.getIntegrityToken()?.let { SdkIntegrityToken(it) }

    /**
     * Cancels the scope and stops all background activity.
     */
    override fun shutdown() {
        job.cancel()
    }
}
