package cz.kotox.crypto.sdk.coindata.internal.data.api

import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinDetailDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinMarketDTO
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

public interface CoinGeckoApi {

    @GET("api/v3/coins/markets")
    public suspend fun getMarkets(
        @Query("vs_currency") currency: String,
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") count: Int = 100,
        @Query("page") page: Int = 1,
    ): List<CoinMarketDTO>

    @GET("api/v3/coins/{id}")
    public suspend fun getCoinDetail(
        @Path("id") coinMarketId: String,
    ): CoinDetailDTO
}
