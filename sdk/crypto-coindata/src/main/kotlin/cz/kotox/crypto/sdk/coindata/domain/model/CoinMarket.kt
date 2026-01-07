package cz.kotox.crypto.sdk.coindata.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Parcelize
public data class CoinMarket(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String,
    val currentPrice: BigDecimal,
    val marketCap: BigDecimal,
    val marketCapRank: Int,
    val fullyDilutedValuation: BigDecimal?,
    val totalVolume: BigDecimal,
    val high24h: BigDecimal?,
    val low24h: BigDecimal?,
    val priceChange24h: BigDecimal?,
    val priceChangePercentage24h: BigDecimal?,
    val marketCapChange24h: BigDecimal?,
    val marketCapChangePercentage24h: BigDecimal?,
    val circulatingSupply: BigDecimal,
    val totalSupply: BigDecimal?,
    val maxSupply: BigDecimal?,
    val ath: BigDecimal,
    val athChangePercentage: BigDecimal,
    val athDate: Instant,
    val atl: BigDecimal,
    val atlChangePercentage: BigDecimal,
    val atlDate: Instant,
    val roi: Roi? = null,
    val lastUpdated: Instant?,
) : Parcelable

/**
 * Represents the ROI (Return on Investment) data, which can be null.
 * The actual fields might be different if the object is present.
 */
@Parcelize
public data class Roi(
    val times: BigDecimal,
    val currency: String,
    val percentage: BigDecimal,
) : Parcelable
