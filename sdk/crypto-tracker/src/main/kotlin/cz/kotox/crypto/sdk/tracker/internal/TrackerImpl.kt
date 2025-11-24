package cz.kotox.crypto.sdk.tracker.internal

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.tracker.MODULE_IDENTIFIER
import cz.kotox.crypto.sdk.tracker.Tracker
import cz.kotox.crypto.sdk.tracker.TrackerConfig
import cz.kotox.crypto.sdk.tracker.domain.TrackerRequestContext
import cz.kotox.crypto.sdk.tracker.domain.model.TradingProduct
import cz.kotox.crypto.sdk.tracker.internal.data.api.TrackerApiService
import cz.kotox.crypto.sdk.tracker.internal.test.CoinbaseApiServiceTestImpl
import cz.kotox.crypto.sdk.tracker.internal.usecase.GetTradingProductsUseCase

internal class TrackerImpl(
    private val config: TrackerConfig,
    private val dispatchers: CoroutineDispatchers,
) : Tracker {

    init {
        installLogger(config)
    }

//    // Keep this lazy so we can init logger first.
//    private val logger by lazy {
//        SDKLogger.getLogger(MODULE_IDENTIFIER)
//    }

    private val trackerRequestContext: TrackerRequestContext by lazy {
        TrackerRequestContext(
            apiService = provideTrackerApiService(),
            dispatchers = dispatchers,
        )
    }

    internal val apiTestDelegate by lazy { CoinbaseApiServiceTestImpl(trackerRequestContext = trackerRequestContext) }

    override suspend fun getTradingProducts(): Either<SdkError, List<TradingProduct>> =
        GetTradingProductsUseCase(
            context = trackerRequestContext,
        ).execute()

    private fun provideTrackerApiService(): TrackerApiService = TrackerApiService(
        trackerConfig = config,
    )

    private fun installLogger(config: TrackerConfig) {
        if (config.loggerCallback is SDKLoggerCallbackNoOp) {
            SDKLogger.uninstall(MODULE_IDENTIFIER)
        } else {
            SDKLogger.install(tag = MODULE_IDENTIFIER, loggerCallback = config.loggerCallback)
        }
    }
}
