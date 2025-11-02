package cz.kotox.crypto.sdk.tracker.domain.model

import android.icu.math.BigDecimal
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a single trading product (pair) from Coinbase.
 * This is the domain model, free of serialization annotations and
 * ready to be used in the UI/domain layers.
 */
@Parcelize
public data class TradingProduct(
    val id: String, // e.g., "BTC-USD"
    val baseCurrency: String, // e.g., "BTC"
    val quoteCurrency: String, // e.g., "USD"
    val baseMinSize: BigDecimal?, // <<< NOW NULLABLE
    val baseMaxSize: BigDecimal?, // <<< NOW NULLABLE
    val quoteIncrement: BigDecimal,
    val baseIncrement: BigDecimal,
    val displayName: String, // e.g., "BTC/USD"
    val minMarketFunds: BigDecimal,
    val maxMarketFunds: BigDecimal?, // Already nullable, which is correct
    val marginEnabled: Boolean,
    val postOnly: Boolean,
    val limitOnly: Boolean,
    val cancelOnly: Boolean,
    val tradingDisabled: Boolean,
    val status: String, // e.g., "online"
    val statusMessage: String?,
    val fxStablecoin: Boolean?,
    val maxSlippagePercentage: BigDecimal?, // Using BigDecimal for precision
    val auctionMode: Boolean?,
) : Parcelable
