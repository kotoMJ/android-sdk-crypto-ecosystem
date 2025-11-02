package cz.kotox.crypto.sdk.coindata.domain.model

import android.icu.math.BigDecimal
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Represents the detailed domain model for a single coin,
 * using precise number types like BigDecimal for financial data.
 */
@OptIn(ExperimentalTime::class)
public data class CoinDetail(
    val id: String,
    val symbol: String,
    val name: String,
    val assetPlatformId: String? = null,
    val platforms: Map<String, String?>,
    val categories: List<String>,
    val description: Localization,
    val links: Links,
    val image: Image,
    val genesisDate: String? = null,
    val marketCapRank: Int?,
    val coingeckoRank: Int?,
    val marketData: MarketData,
    val lastUpdated: Instant?,
)

/**
 * Represents localized string data for descriptions.
 */
public data class Localization(
    val en: String?,
    val de: String?,
    val es: String?,
    val fr: String?,
)

/**
 * Represents the nested image URLs.
 */
public data class Image(
    val thumb: String,
    val small: String,
    val large: String,
)

/**
 * Represents the nested links object.
 */
public data class Links(
    val homepage: List<String?>,
    val blockchainSite: List<String?>,
    val officialForumUrl: List<String?>,
    val reposUrl: ReposUrl,
    val subredditUrl: String? = null,
)

/**
 * Represents repository URLs.
 */
public data class ReposUrl(
    val github: List<String?>,
    val bitbucket: List<String?>,
)

/**
 * Represents the core market data, using BigDecimal for all
 * monetary and high-precision supply values.
 */
@OptIn(ExperimentalTime::class)
public data class MarketData(
    val currentPrice: Map<String, BigDecimal>,
    val ath: Map<String, BigDecimal>,
    val athChangePercentage: Map<String, BigDecimal>,
    val athDate: Map<String, Instant>,
    val atl: Map<String, BigDecimal>,
    val atlChangePercentage: Map<String, BigDecimal>,
    val atlDate: Map<String, Instant>,
    val marketCap: Map<String, BigDecimal>,
    val marketCapRank: Int?,
    val fullyDilutedValuation: Map<String, BigDecimal?>,
    val totalVolume: Map<String, BigDecimal>,
    val high24h: Map<String, BigDecimal?>,
    val low24h: Map<String, BigDecimal?>,
    val priceChange24hInCurrency: Map<String, BigDecimal?>,
    val priceChangePercentage24hInCurrency: Map<String, BigDecimal?>,
    val marketCapChange24hInCurrency: Map<String, BigDecimal?>,
    val marketCapChangePercentage24hInCurrency: Map<String, BigDecimal?>,

    // Converted from String/String? to BigDecimal/BigDecimal?
    val circulatingSupply: BigDecimal,
    val totalSupply: BigDecimal?,
    val maxSupply: BigDecimal?,
)
