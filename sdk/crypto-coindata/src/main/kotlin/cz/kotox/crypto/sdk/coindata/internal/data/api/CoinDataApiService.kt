package cz.kotox.crypto.sdk.coindata.internal.data.api

import cz.kotox.crypto.sdk.coindata.CoinDataConfig
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinDetailDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinMarketDTO
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.domain.model.coin.CurrencyId
import cz.kotox.crypto.sdk.internal.network.KtorConfig
import cz.kotox.crypto.sdk.internal.network.KtorfitFactory
import de.jensklingenberg.ktorfit.Response

internal class CoinDataApiService(
    private val coinDataConfig: CoinDataConfig,
) {

    private val ktorConfig = KtorConfig(
        baseUrl = "https://api.coingecko.com/",
        isLoggingEnabled = coinDataConfig.isLoggingEnabled,
        networkTimeout = coinDataConfig.networkTimeout,
        strictModePolicy = coinDataConfig.strictModePolicy,
        loggingPolicy = coinDataConfig.loggingPolicy,
    )

    val ktorfitFactory = KtorfitFactory(
        config = ktorConfig,
        sdkLoggerCallback = coinDataConfig.loggerCallback,
    )

    private val coinGeckoApi: CoinGeckoApi = ktorfitFactory.ktorfit.createCoinGeckoApi()

    /**
     * Fetches coin market data.
     */
    suspend fun getMarkets(currency: CurrencyId): Response<List<CoinMarketDTO>> {
        return coinGeckoApi.getMarkets(currency = currency.value)
    }

    /**
     * Fetches coin market data.
     */
    suspend fun getCoinDetail(coinMarketId: CoinMarketId): Response<CoinDetailDTO> {
        return coinGeckoApi.getCoinDetail(coinMarketId = coinMarketId.value)
    }
}
