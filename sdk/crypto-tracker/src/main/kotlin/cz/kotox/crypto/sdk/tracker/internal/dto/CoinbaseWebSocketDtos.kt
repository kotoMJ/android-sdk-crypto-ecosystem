package cz.kotox.crypto.sdk.tracker.internal.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Message to send to Coinbase to subscribe to a channel.
 *
 * Example:
 * {
 * "type": "subscribe",
 * "product_ids": ["ETH-USD", "BTC-USD"],
 * "channels": ["ticker"]
 * }
 */
@Serializable
public data class CoinbaseSubscribeMessage(
    val type: String = "subscribe",
    @SerialName("product_ids")
    val productIds: List<String>,
    val channels: List<String>,
)

/**
 * A sealed class representing all possible incoming messages.
 * We'll start with just the 'ticker' message.
 */
@Serializable
public sealed class CoinbaseWebSocketMessage {

    @Serializable
    @SerialName("ticker")
    public data class Ticker(
        val type: String,
        @SerialName("trade_id")
        val tradeId: Long,
        val sequence: Long,
        val time: String,
        @SerialName("product_id")
        val productId: String,
        val price: String,
        val side: String,
        @SerialName("last_size")
        val lastSize: String,
        @SerialName("best_bid")
        val bestBid: String,
        @SerialName("best_ask")
        val bestAsk: String,
    ) : CoinbaseWebSocketMessage()

    @Serializable
    @SerialName("subscriptions")
    public data class Subscriptions(
        val type: String,
        val channels: List<ChannelSubscription>,
    ) : CoinbaseWebSocketMessage() {
        @Serializable
        public data class ChannelSubscription(
            val name: String,
            @SerialName("product_ids")
            val productIds: List<String>,
        )
    }

    @Serializable
    @SerialName("error")
    public data class Error(
        val type: String,
        val message: String,
        val reason: String,
    ) : CoinbaseWebSocketMessage()

    /**
     * Catches any message types we haven't explicitly defined.
     */
    @Serializable
    public data class Unknown(
        val type: String? = null,
    ) : CoinbaseWebSocketMessage()
}
