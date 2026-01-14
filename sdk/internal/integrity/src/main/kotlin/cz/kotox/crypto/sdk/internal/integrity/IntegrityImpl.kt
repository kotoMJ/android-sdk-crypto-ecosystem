package cz.kotox.crypto.sdk.internal.integrity

import cz.kotox.crypto.sdk.internal.integrity.domain.SdkIntegrityToken
import cz.kotox.crypto.sdk.internal.integrity.domain.SdkSecurityHeader
import cz.kotox.crypto.sdk.internal.integrity.utils.toSha256Base64
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
            SDKLogger.Companion.uninstall(MODULE_IDENTIFIER)
        } else {
            SDKLogger.Companion.install(tag = MODULE_IDENTIFIER, loggerCallback = config.loggerCallback)
        }
    }

    override suspend fun getFreshToken(uniqueRequestHash: String): SdkIntegrityToken? =
        integrityProvider.getIntegrityToken(uniqueRequestHash)?.let { SdkIntegrityToken(it) }

    override fun getIntegrityHash(content: String): String = content.toSha256Base64()

    override fun getSecurityHeader(): SdkSecurityHeader? =
        if (BuildConfig.DEBUG && BuildConfig.BFF_CRYPTO_ADMIN_BYPASS_SECRET.isNotBlank()) {
            SdkSecurityHeader(
                key = "X-Kotox-Bypass-Key",
                value = BuildConfig.BFF_CRYPTO_ADMIN_BYPASS_SECRET,
            )
        } else {
            null
        }

    /**
     * Cancels the scope and stops all background activity.
     */
    override fun shutdown() {
        job.cancel()
    }
}
