package cz.kotox.crypto.sdk.coindata.internal.data.api

import cz.kotox.crypto.sdk.coindata.CoinDataConfig
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinDetailDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinMarketDTO
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.domain.model.coin.CurrencyId
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.network.KtorConfig
import cz.kotox.crypto.sdk.internal.network.KtorfitFactory

internal class CoinDataApiService(
    val sdkLogger: SDKLogger, // TODO MJ: can we use our own logger in KtorfitFactory?
    private val coinDataConfig: CoinDataConfig,
) {

    private val ktorConfig = KtorConfig(
        baseUrl = "https://api.coingecko.com/",
        isLoggingEnabled = coinDataConfig.isLoggingEnabled,
        networkTimeout = coinDataConfig.networkTimeout,
    )

    val ktorfitFactory = KtorfitFactory(
        config = ktorConfig,
        sdkLogger = sdkLogger,
    )

    private val coinGeckoApi: CoinGeckoApi = ktorfitFactory.ktorfit.createCoinGeckoApi()

    /**
     * Fetches coin market data.
     */
    suspend fun getMarkets(currency: CurrencyId): List<CoinMarketDTO> {
        return coinGeckoApi.getMarkets(currency = currency.value)
    }

    /**
     * Fetches coin market data.
     */
    suspend fun getCoinDetail(coinMarketId: CoinMarketId): CoinDetailDTO {
        return coinGeckoApi.getCoinDetail(coinMarketId = coinMarketId.value)
    }
}
