package cz.kotox.crypto.sdk.coindata.internal.data.dto

import cz.kotox.crypto.sdk.internal.network.BigDecimalSerializer
import cz.kotox.crypto.sdk.internal.network.SafeBigDecimalSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private object MarketCapSerializer : SafeBigDecimalSerializer("CoinMarketDTO.market_cap")
private object FullyDilutedValuationSerializer : SafeBigDecimalSerializer("CoinMarketDTO.fully_diluted_valuation")

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

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("current_price")
    val currentPrice: BigDecimal,

    @Serializable(with = MarketCapSerializer::class)
    @SerialName("market_cap")
    val marketCap: BigDecimal,

    @SerialName("market_cap_rank")
    val marketCapRank: Int,

    @Serializable(with = FullyDilutedValuationSerializer::class)
    @SerialName("fully_diluted_valuation")
    val fullyDilutedValuation: BigDecimal?,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("total_volume")
    val totalVolume: BigDecimal,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("high_24h")
    val high24h: BigDecimal?,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("low_24h")
    val low24h: BigDecimal?,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("price_change_24h")
    val priceChange24h: BigDecimal?,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: BigDecimal?,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("market_cap_change_24h")
    val marketCapChange24h: BigDecimal?,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: BigDecimal?,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("circulating_supply")
    val circulatingSupply: BigDecimal,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("total_supply")
    val totalSupply: BigDecimal?,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("max_supply")
    val maxSupply: BigDecimal?,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("ath")
    val ath: BigDecimal,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("ath_change_percentage")
    val athChangePercentage: BigDecimal,

    @SerialName("ath_date")
    val athDate: Instant,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("atl")
    val atl: BigDecimal,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("atl_change_percentage")
    val atlChangePercentage: BigDecimal,

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
    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("times")
    val times: BigDecimal,

    @SerialName("currency")
    val currency: String,

    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("percentage")
    val percentage: BigDecimal,
)
