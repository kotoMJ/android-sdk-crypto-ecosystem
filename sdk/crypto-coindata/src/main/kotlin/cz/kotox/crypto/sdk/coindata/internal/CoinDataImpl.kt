package cz.kotox.crypto.sdk.coindata.internal

import cz.kotox.crypto.sdk.coindata.CoinData
import cz.kotox.crypto.sdk.coindata.CoinDataConfig
import cz.kotox.crypto.sdk.coindata.MODULE_IDENTIFIER
import cz.kotox.crypto.sdk.coindata.domain.CoinDataRequestContext
import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import cz.kotox.crypto.sdk.coindata.internal.data.api.CoinDataApiService
import cz.kotox.crypto.sdk.coindata.internal.test.CoinDataApiServiceTestImpl
import cz.kotox.crypto.sdk.coindata.internal.usecase.GetCoinDetailUseCase
import cz.kotox.crypto.sdk.coindata.internal.usecase.GetCoinMarketsUseCase
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.domain.model.coin.CurrencyId
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp

internal class CoinDataImpl(
    private val config: CoinDataConfig,
    private val dispatchers: CoroutineDispatchers,
) : CoinData {

    init {
        installLogger(config)
    }

//    // Keep this lazy so we can init logger first.
//    private val logger by lazy {
//        SDKLogger.getLogger(MODULE_IDENTIFIER)
//    }

    private val coinDataRequestContext: CoinDataRequestContext by lazy {
        CoinDataRequestContext(
            apiService = provideCoinDataApiService(),
            dispatchers = dispatchers,
        )
    }

    internal val apiTestDelegate by lazy { CoinDataApiServiceTestImpl(coinDataRequestContext = coinDataRequestContext) }

    override suspend fun getCoinMarkets(currency: CurrencyId): Either<SdkError, List<CoinMarket>> =
        GetCoinMarketsUseCase(
            context = coinDataRequestContext,
        ).execute(currency = currency)

    override suspend fun getCoinDetail(coinMarketId: CoinMarketId): Either<SdkError, CoinDetail> =
        GetCoinDetailUseCase(
            context = coinDataRequestContext,
        ).execute(coinMarketId = coinMarketId)

    private fun provideCoinDataApiService(): CoinDataApiService = CoinDataApiService(
        coinDataConfig = config,
    )

    private fun installLogger(config: CoinDataConfig) {
        if (config.loggerCallback is SDKLoggerCallbackNoOp) {
            SDKLogger.uninstall(MODULE_IDENTIFIER)
        } else {
            SDKLogger.install(tag = MODULE_IDENTIFIER, loggerCallback = config.loggerCallback)
        }
    }
}
