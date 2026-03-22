package cz.kotox.crypto.sdk.coindata.internal.data.api

import cz.kotox.crypto.sdk.coindata.CoinDataConfig
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinDetailDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinMarketDTO
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.domain.model.coin.CurrencyId
import cz.kotox.crypto.sdk.internal.network.ApiResult
import cz.kotox.crypto.sdk.internal.network.KtorConfig
import cz.kotox.crypto.sdk.internal.network.KtorFactory
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

private suspend inline fun <reified T> toResult(httpResponse: HttpResponse): ApiResult<T> {
    return if (httpResponse.status.isSuccess()) {
        ApiResult.Success(httpResponse.body())
    } else {
        ApiResult.Failure(httpResponse.status.value, httpResponse.status.description)
    }
}

internal class CoinDataApiService2(
    private val coinDataConfig: CoinDataConfig,
) {

    private val ktorConfig = KtorConfig(
        baseUrl = "https://api.coingecko.com/",
        isLoggingEnabled = coinDataConfig.isLoggingEnabled,
        networkTimeout = coinDataConfig.networkTimeout,
        strictModePolicy = coinDataConfig.strictModePolicy,
        loggingPolicy = coinDataConfig.loggingPolicy,
    )

    private val ktorFactory = KtorFactory(
        config = ktorConfig,
        sdkLoggerCallback = coinDataConfig.loggerCallback,
    )

    private val coinGeckoApi: CoinGeckoApi2 = CoinGeckoApi2(ktorFactory.httpClient)

    /**
     * Fetches coin market data.
     */
    suspend fun getMarkets(currency: CurrencyId): ApiResult<List<CoinMarketDTO>> {
        val response = coinGeckoApi.getMarkets(currency = currency.value)
        return toResult(response)
    }

    /**
     * Fetches coin detail data.
     */
    suspend fun getCoinDetail(coinMarketId: CoinMarketId): ApiResult<CoinDetailDTO> {
        val response = coinGeckoApi.getCoinDetail(coinMarketId = coinMarketId.value)
        return toResult(response)
    }
}
