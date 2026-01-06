package cz.kotox.crypto.sdk.coindata.internal.data.mapper

import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import cz.kotox.crypto.sdk.coindata.domain.model.Roi
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinMarketEntity
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinMarketDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.RoiDTO
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Maps the data layer [CoinMarketDTO] to the domain layer [CoinMarket].
 */
@OptIn(ExperimentalTime::class)
@JvmSynthetic
internal fun CoinMarketDTO.toDomain(): CoinMarket = CoinMarket(
    id = id,
    symbol = symbol,
    name = name,
    imageUrl = imageUrl,
    currentPrice = currentPrice,
    marketCap = marketCap,
    marketCapRank = marketCapRank,
    fullyDilutedValuation = fullyDilutedValuation,
    totalVolume = totalVolume,
    high24h = high24h,
    low24h = low24h,
    priceChange24h = priceChange24h,
    priceChangePercentage24h = priceChangePercentage24h,
    marketCapChange24h = marketCapChange24h,
    marketCapChangePercentage24h = marketCapChangePercentage24h,
    circulatingSupply = circulatingSupply,
    totalSupply = totalSupply,
    maxSupply = maxSupply,
    ath = ath,
    athChangePercentage = athChangePercentage,
    athDate = athDate,
    atl = atl,
    atlChangePercentage = atlChangePercentage,
    atlDate = atlDate,
    roi = roi?.toRoi(),
    lastUpdated = lastUpdated,
)

/**
 * Maps the data layer [RoiDTO] to the domain layer [Roi].
 */
@JvmSynthetic
internal fun RoiDTO.toRoi(): Roi = Roi(
    times = times,
    currency = currency,
    percentage = percentage,
)

// --- Entity -> Domain ---

@OptIn(ExperimentalTime::class)
internal fun CoinMarketEntity.toDomain(): CoinMarket {
    return CoinMarket(
        id = id,
        symbol = symbol,
        name = name,
        imageUrl = imageUrl,
        currentPrice = currentPrice,
        marketCap = marketCap,
        marketCapRank = marketCapRank,
        fullyDilutedValuation = fullyDilutedValuation,
        totalVolume = totalVolume,
        high24h = high24h,
        low24h = low24h,
        priceChange24h = priceChange24h,
        priceChangePercentage24h = priceChangePercentage24h,
        marketCapChange24h = marketCapChange24h,
        marketCapChangePercentage24h = marketCapChangePercentage24h,
        circulatingSupply = circulatingSupply,
        totalSupply = totalSupply,
        maxSupply = maxSupply,
        ath = ath,
        athChangePercentage = athChangePercentage,
        athDate = athDate,
        atl = atl,
        atlChangePercentage = atlChangePercentage,
        atlDate = atlDate,
        roi = if (roiTimes != null && roiCurrency != null && roiPercentage != null) {
            Roi(times = roiTimes, currency = roiCurrency, percentage = roiPercentage)
        } else {
            null
        },
        lastUpdated = lastUpdated,
    )
}

// --- DTO -> Entity ---

@OptIn(ExperimentalTime::class)
internal fun CoinMarketDTO.toEntity(vsCurrency: String): CoinMarketEntity {
    val now = Clock.System.now()
    return CoinMarketEntity(
        id = id,
        vsCurrency = vsCurrency,
        symbol = symbol,
        name = name,
        imageUrl = imageUrl,
        currentPrice = currentPrice,
        marketCap = marketCap,
        marketCapRank = marketCapRank,
        fullyDilutedValuation = fullyDilutedValuation,
        totalVolume = totalVolume,
        high24h = high24h,
        low24h = low24h,
        priceChange24h = priceChange24h,
        priceChangePercentage24h = priceChangePercentage24h,
        marketCapChange24h = marketCapChange24h,
        marketCapChangePercentage24h = marketCapChangePercentage24h,
        circulatingSupply = circulatingSupply,
        totalSupply = totalSupply,
        maxSupply = maxSupply,
        ath = ath,
        athChangePercentage = athChangePercentage,
        athDate = athDate,
        atl = atl,
        atlChangePercentage = atlChangePercentage,
        atlDate = atlDate,
        roiTimes = roi?.times,
        roiCurrency = roi?.currency,
        roiPercentage = roi?.percentage,
        lastUpdated = lastUpdated,
        cachedAt = now,
    )
}
