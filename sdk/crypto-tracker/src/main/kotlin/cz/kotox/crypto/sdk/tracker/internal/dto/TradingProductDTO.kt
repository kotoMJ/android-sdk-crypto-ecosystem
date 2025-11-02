package cz.kotox.crypto.sdk.tracker.internal.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a single trading product (pair) from the Coinbase Exchange API.
 * Corresponds to an item in the array response from GET /products
 */
@Serializable
public data class TradingProductDTO(
    @SerialName("id")
    val id: String, // e.g., "SAFE-USD"

    @SerialName("base_currency")
    val baseCurrency: String, // e.g., "SAFE"

    @SerialName("quote_currency")
    val quoteCurrency: String, // e.g., "USD"

    @SerialName("base_min_size")
    val baseMinSize: String? = null,

    @SerialName("base_max_size")
    val baseMaxSize: String? = null,

    @SerialName("quote_increment")
    val quoteIncrement: String, // e.g., "0.0001"

    @SerialName("base_increment")
    val baseIncrement: String, // e.g., "0.01"

    @SerialName("display_name")
    val displayName: String, // e.g., "SAFE/USD"

    @SerialName("min_market_funds")
    val minMarketFunds: String, // e.g., "1"

    @SerialName("max_market_funds")
    val maxMarketFunds: String? = null,

    @SerialName("margin_enabled")
    val marginEnabled: Boolean, // e.g., false

    @SerialName("post_only")
    val postOnly: Boolean, // e.g., false

    @SerialName("limit_only")
    val limitOnly: Boolean, // e.g., false

    @SerialName("cancel_only")
    val cancelOnly: Boolean, // e.g., false

    @SerialName("trading_disabled")
    val tradingDisabled: Boolean, // e.g., false

    @SerialName("status")
    val status: String, // e.g., "online"

    @SerialName("status_message")
    val statusMessage: String?, // e.g., "" (can be null or empty)

    // --- NEW FIELDS from your JSON ---
    @SerialName("fx_stablecoin")
    val fxStablecoin: Boolean? = null, // e.g., false

    @SerialName("max_slippage_percentage")
    val maxSlippagePercentage: String? = null, // e.g., "0.03000000"

    @SerialName("auction_mode")
    val auctionMode: Boolean? = null, // e.g., false

    @SerialName("high_bid_limit_percentage")
    val highBidLimitPercentage: String? = null, // e.g., ""
)
