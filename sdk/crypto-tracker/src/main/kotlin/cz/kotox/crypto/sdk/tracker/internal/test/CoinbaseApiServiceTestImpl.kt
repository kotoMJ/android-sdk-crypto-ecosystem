package cz.kotox.crypto.sdk.tracker.internal.test

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.tracker.domain.TrackerRequestContext
import cz.kotox.crypto.sdk.tracker.internal.dto.CoinbaseWebSocketMessage
import cz.kotox.crypto.sdk.tracker.internal.dto.TradingProductDTO
import kotlinx.coroutines.flow.Flow

internal class CoinbaseApiServiceTestImpl(
    private val trackerRequestContext: TrackerRequestContext,
) {
    suspend fun getProducts(): Either<SdkError, List<TradingProductDTO>> = trackerRequestContext.withApi {
        getTradingProducts()
    }

    /**
     * Exposes the WebSocket Flow for testing.
     * We return the Flow directly; the test will handle collection and errors.
     */
    fun observeMessages(
        productIds: List<String>,
        channels: List<String>,
    ): Flow<CoinbaseWebSocketMessage> {
        return trackerRequestContext.apiService.observeMessages(productIds, channels)
    }
}
