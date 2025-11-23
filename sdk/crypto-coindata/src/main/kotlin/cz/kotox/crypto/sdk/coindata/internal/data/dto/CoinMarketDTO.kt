package cz.kotox.crypto.sdk.coindata.internal.data.dto

import cz.kotox.crypto.sdk.internal.network.SafeLongSerializer
import cz.kotox.crypto.sdk.internal.network.SafeNullableLongSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
public data class CoinMarketDTO
@OptIn(ExperimentalTime::class)
constructor(
    @SerialName("id")
    val id: String,

    @SerialName("symbol")
    val symbol: String,

    @SerialName("name")
    val name: String,

    @SerialName("image")
    val imageUrl: String,

    @SerialName("current_price")
    val currentPrice: Double,

    @Serializable(with = SafeLongSerializer::class)
    @SerialName("market_cap")
    val marketCap: Long,

    @SerialName("market_cap_rank")
    val marketCapRank: Int,

    @Serializable(with = SafeNullableLongSerializer::class)
    @SerialName("fully_diluted_valuation")
    val fullyDilutedValuation: Long?,

    @SerialName("total_volume")
    val totalVolume: Double,

    @SerialName("high_24h")
    val high24h: Double?,

    @SerialName("low_24h")
    val low24h: Double?,

    @SerialName("price_change_24h")
    val priceChange24h: Double?,

    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double?,

    @SerialName("market_cap_change_24h")
    val marketCapChange24h: Double?,

    @SerialName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double?,

    @SerialName("circulating_supply")
    val circulatingSupply: Double,

    @SerialName("total_supply")
    val totalSupply: Double?,

    @SerialName("max_supply")
    val maxSupply: Double?,

    @SerialName("ath")
    val ath: Double,

    @SerialName("ath_change_percentage")
    val athChangePercentage: Double,

    @SerialName("ath_date")
    val athDate: Instant,

    @SerialName("atl")
    val atl: Double,

    @SerialName("atl_change_percentage")
    val atlChangePercentage: Double,

    @SerialName("atl_date")
    val atlDate: Instant,

    @SerialName("roi")
    val roi: RoiDTO? = null,

    @Contextual // TODO MJ - this is just to overcome IDE underline warning
    @SerialName("last_updated")
    val lastUpdated: Instant?,
)

/**
 * Represents the ROI (Return on Investment) data, which can be null.
 * The actual fields might be different if the object is present.
 */
@Serializable
public data class RoiDTO(
    @SerialName("times")
    val times: Double,

    @SerialName("currency")
    val currency: String,

    @SerialName("percentage")
    val percentage: Double,
)
