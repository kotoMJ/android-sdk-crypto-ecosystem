package cz.kotox.crypto.sdk.coindata.internal.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// --- 1. Coin Market Entity ---
@Entity(
    tableName = "coin_markets",
    primaryKeys = ["id", "vs_currency"],
)
@OptIn(ExperimentalTime::class)
internal data class CoinMarketEntity(
    val id: String,
    @ColumnInfo(name = "vs_currency")
    val vsCurrency: String,
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

    // Flattened ROI (nullable)
    val roiTimes: Double?,
    val roiCurrency: String?,
    val roiPercentage: Double?,

    val lastUpdated: Instant?,
    val cachedAt: Instant,
)
