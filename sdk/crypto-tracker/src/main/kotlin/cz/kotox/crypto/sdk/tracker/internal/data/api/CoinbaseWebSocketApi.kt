package cz.kotox.crypto.sdk.tracker.internal.data.api

import cz.kotox.crypto.sdk.tracker.internal.dto.CoinbaseWebSocketMessage
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing a WebSocket connection to Coinbase.
 */
internal interface CoinbaseWebSocketApi {
    /**
     * Connects to the WebSocket and subscribes to the given products/channels.
     *
     * The returned Flow will emit messages from the socket.
     * The Flow will complete when the socket is closed.
     * It will emit an error if the connection fails.
     *
     * @param productIds A list of product IDs to subscribe to (e.g., "BTC-USD").
     * @param channels A list of channels to subscribe to (e.g., "ticker").
     */
    fun observeMessages(
        productIds: List<String>,
        channels: List<String>,
    ): Flow<CoinbaseWebSocketMessage>
}
