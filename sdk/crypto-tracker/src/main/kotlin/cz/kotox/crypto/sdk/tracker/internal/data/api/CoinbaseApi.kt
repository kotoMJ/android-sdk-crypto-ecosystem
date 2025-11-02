package cz.kotox.crypto.sdk.tracker.internal.data.api

import cz.kotox.crypto.sdk.tracker.internal.dto.TradingProductDTO
import de.jensklingenberg.ktorfit.http.GET

public interface CoinbaseApi {

    @GET("products")
    public suspend fun getTradingProducts(): List<TradingProductDTO>
}
