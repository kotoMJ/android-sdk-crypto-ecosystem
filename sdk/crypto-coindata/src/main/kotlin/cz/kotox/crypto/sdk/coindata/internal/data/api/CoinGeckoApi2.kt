package cz.kotox.crypto.sdk.coindata.internal.data.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

public class CoinGeckoApi2(private val httpClient: HttpClient) {

    public suspend fun getMarkets(
        currency: String,
        order: String = "market_cap_desc",
        count: Int = 100,
        page: Int = 1,
    ): HttpResponse {
        return httpClient.get("api/v3/coins/markets") {
            parameter("vs_currency", currency)
            parameter("order", order)
            parameter("per_page", count)
            parameter("page", page)
        }
    }

    public suspend fun getCoinDetail(
        coinMarketId: String,
    ): HttpResponse {
        return httpClient.get("api/v3/coins/$coinMarketId")
    }
}
