package cz.kotox.crypto.sdk.coindata.internal.data.database.entity

import android.icu.math.BigDecimal
import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO MJ - consider Doubles to be BigDecimals, verify room for that
@Entity(tableName = "coin_markets")
internal data class CoinMarketEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String,

    // --- PRICES (CRITICAL) ---
    val currentPrice: BigDecimal,
    val high24h: BigDecimal?,
    val low24h: BigDecimal?,
    val ath: BigDecimal,
    val atl: BigDecimal,

    // --- MARKET VALUES ---
    // Changed from Long -> BigDecimal to capture cents and avoid overflow
    val marketCap: BigDecimal,
    val fullyDilutedValuation: BigDecimal?,
    val totalVolume: BigDecimal,

    // --- VALUE CHANGES ---
    val priceChange24h: BigDecimal?,
    val marketCapChange24h: BigDecimal?,

    // --- SUPPLY (CRITICAL for precision) ---
    val circulatingSupply: BigDecimal,
    val totalSupply: BigDecimal?,
    val maxSupply: BigDecimal?,

    // --- PERCENTAGES (OPTIONAL - kept as Double for simplicity, or change to BD) ---
    val priceChangePercentage24h: Double?,
    val marketCapChangePercentage24h: Double?,
    val athChangePercentage: Double,
    val atlChangePercentage: Double,

    // --- METADATA ---
    val marketCapRank: Int,
    val athDate: Long, // Timestamp
    val atlDate: Long, // Timestamp
    val lastUpdated: Long, // Timestamp
)
