package cz.kotox.crypto.sdk.coindata.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Parcelize
public data class CoinMarket(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String,
    val currentPrice: Double,
    val marketCap: Long,
    val marketCapRank: Int,
    val fullyDilutedValuation: Long?,
    val totalVolume: Double,
    val high24h: Double?,
    val low24h: Double?,
    val priceChange24h: Double?,
    val priceChangePercentage24h: Double?,
    val marketCapChange24h: Double?,
    val marketCapChangePercentage24h: Double?,
    val circulatingSupply: Double,
    val totalSupply: Double?,
    val maxSupply: Double?,
    val ath: Double,
    val athChangePercentage: Double,
    val athDate: Instant,
    val atl: Double,
    val atlChangePercentage: Double,
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
    val times: Double,
    val currency: String,
    val percentage: Double,
) : Parcelable
