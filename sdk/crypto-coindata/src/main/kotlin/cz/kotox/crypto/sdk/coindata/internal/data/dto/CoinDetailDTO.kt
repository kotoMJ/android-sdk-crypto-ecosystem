package cz.kotox.crypto.sdk.coindata.internal.data.dto
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Represents the detailed data for a single coin from the /coins/{id} endpoint.
 */
@Serializable
@OptIn(ExperimentalTime::class)
public data class CoinDetailDTO(
    @SerialName("id")
    val id: String,

    @SerialName("symbol")
    val symbol: String,

    @SerialName("name")
    val name: String,

    @SerialName("asset_platform_id")
    val assetPlatformId: String? = null,

    // Example of a nested object for "platforms": { "ethereum": "0x..." }
    @SerialName("platforms")
    val platforms: Map<String, String?>,

    @SerialName("categories")
    val categories: List<String>,

    // The description is a map of language codes to strings
    @SerialName("description")
    val description: LocalizationDTO,

    @SerialName("links")
    val links: LinksDTO,

    @SerialName("image")
    val image: ImageDTO,

    @SerialName("genesis_date")
    val genesisDate: String? = null,

    @SerialName("market_cap_rank")
    val marketCapRank: Int? = null,

    @SerialName("coingecko_rank")
    val coingeckoRank: Int? = null, // <-- (This was the field causing the crash) ADDED = null

    // The main market data is heavily nested
    @SerialName("market_data")
    val marketData: MarketDataDTO,

    @Contextual
    @SerialName("last_updated")
    val lastUpdated: Instant? = null,
)

/**
 * Represents localized string data, e.g., for descriptions.
 * JSON: { "en": "...", "de": "...", ... }
 */
@Serializable
public data class LocalizationDTO(
    @SerialName("en")
    val en: String? = null,

    @SerialName("de")
    val de: String? = null,

    @SerialName("es")
    val es: String? = null,

    @SerialName("fr")
    val fr: String? = null,
)

/**
 * Represents the nested image URLs.
 * JSON: { "thumb": "...", "small": "...", "large": "..." }
 */
@Serializable
public data class ImageDTO(
    @SerialName("thumb")
    val thumb: String,

    @SerialName("small")
    val small: String,

    @SerialName("large")
    val large: String,
)

/**
 * Represents the nested links object.
 */
@Serializable
public data class LinksDTO(
    @SerialName("homepage")
    val homepage: List<String?>,

    @SerialName("blockchain_site")
    val blockchainSite: List<String?>,

    @SerialName("official_forum_url")
    val officialForumUrl: List<String?>,

    @SerialName("repos_url")
    val reposUrl: ReposUrlDTO,

    @SerialName("subreddit_url")
    val subredditUrl: String? = null,
)

@Serializable
public data class ReposUrlDTO(
    @SerialName("github")
    val github: List<String?>,

    @SerialName("bitbucket")
    val bitbucket: List<String?>,
)

/**
 * Represents the core market data.
 * Numerical values are stored as Strings to be later parsed into BigDecimal for precision.
 */
@Serializable
@OptIn(ExperimentalTime::class)
public data class MarketDataDTO(
    @SerialName("current_price")
    val currentPrice: Map<String, String>,

    @SerialName("ath")
    val ath: Map<String, String>,

    @SerialName("ath_change_percentage")
    val athChangePercentage: Map<String, String>,

    @Contextual
    @SerialName("ath_date")
    val athDate: Map<String, Instant>,

    @SerialName("atl")
    val atl: Map<String, String>,

    @SerialName("atl_change_percentage")
    val atlChangePercentage: Map<String, String>,

    @Contextual
    @SerialName("atl_date")
    val atlDate: Map<String, Instant>,

    @SerialName("market_cap")
    val marketCap: Map<String, String>,

    @SerialName("market_cap_rank")
    val marketCapRank: Int? = null,

    @SerialName("fully_diluted_valuation")
    val fullyDilutedValuation: Map<String, String?>,

    @SerialName("total_volume")
    val totalVolume: Map<String, String>,

    @SerialName("high_24h")
    val high24h: Map<String, String?>,

    @SerialName("low_24h")
    val low24h: Map<String, String?>,

    @SerialName("price_change_24h_in_currency")
    val priceChange24hInCurrency: Map<String, String?>,

    @SerialName("price_change_percentage_24h_in_currency")
    val priceChangePercentage24hInCurrency: Map<String, String?>,

    @SerialName("market_cap_change_24h_in_currency")
    val marketCapChange24hInCurrency: Map<String, String?>,

    @SerialName("market_cap_change_percentage_24h_in_currency")
    val marketCapChangePercentage24hInCurrency: Map<String, String?>,

    @SerialName("circulating_supply")
    val circulatingSupply: String,

    @SerialName("total_supply")
    val totalSupply: String? = null,

    @SerialName("max_supply")
    val maxSupply: String? = null,
)
