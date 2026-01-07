package cz.kotox.crypto.sdk.coindata.internal.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.math.BigDecimal
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

    // Flattened ROI (nullable)
    val roiTimes: BigDecimal?,
    val roiCurrency: String?,
    val roiPercentage: BigDecimal?,

    val lastUpdated: Instant?,
    val cachedAt: Instant,
)
