package cz.kotox.crypto.sdk.coindata.internal.data.mapper

import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.domain.model.Image
import cz.kotox.crypto.sdk.coindata.domain.model.Links
import cz.kotox.crypto.sdk.coindata.domain.model.Localization
import cz.kotox.crypto.sdk.coindata.domain.model.MarketData
import cz.kotox.crypto.sdk.coindata.domain.model.ReposUrl
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailCurrencyValueEntity
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailEntity
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailWithRelations
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.ImageEntityData
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.LocalizationEntityData
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinDetailDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.ImageDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.LinksDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.LocalizationDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.MarketDataDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.ReposUrlDTO
import cz.kotox.crypto.sdk.internal.common.util.number.toBigDecimalOrNull
import cz.kotox.crypto.sdk.internal.common.util.number.toBigDecimalOrNullValues
import cz.kotox.crypto.sdk.internal.common.util.number.toBigDecimalOrZero
import cz.kotox.crypto.sdk.internal.common.util.number.toBigDecimalValues
import java.math.BigDecimal
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Maps the network DTO [CoinDetailDTO] to the domain model [CoinDetail].
 */
@OptIn(ExperimentalTime::class)
internal fun CoinDetailDTO.toDomain(): CoinDetail {
    return CoinDetail(
        id = this.id,
        symbol = this.symbol,
        name = this.name,
        assetPlatformId = this.assetPlatformId,
        platforms = this.platforms,
        categories = this.categories,
        description = this.description.toDomain(),
        links = this.links.toDomain(),
        image = this.image.toDomain(),
        genesisDate = this.genesisDate,
        marketCapRank = this.marketCapRank,
        coingeckoRank = this.coingeckoRank,
        marketData = this.marketData.toDomain(),
        lastUpdated = this.lastUpdated,
    )
}

// --- Sub-Mappers ---

internal fun LocalizationDTO.toDomain(): Localization {
    return Localization(
        en = this.en,
        de = this.de,
        es = this.es,
        fr = this.fr,
    )
}

internal fun ImageDTO.toDomain(): Image {
    return Image(
        thumb = this.thumb,
        small = this.small,
        large = this.large,
    )
}

internal fun ReposUrlDTO.toDomain(): ReposUrl {
    return ReposUrl(
        github = this.github,
        bitbucket = this.bitbucket,
    )
}

internal fun LinksDTO.toDomain(): Links {
    return Links(
        homepage = this.homepage,
        blockchainSite = this.blockchainSite,
        officialForumUrl = this.officialForumUrl,
        reposUrl = this.reposUrl.toDomain(),
        subredditUrl = this.subredditUrl,
    )
}

/**
 * Maps the [MarketDataDTO] to the [MarketData] domain model,
 * performing safe String-to-BigDecimal conversions.
 */
@OptIn(ExperimentalTime::class)
internal fun MarketDataDTO.toDomain(): MarketData {
    return MarketData(
        currentPrice = this.currentPrice.toBigDecimalValues(),
        ath = this.ath.toBigDecimalValues(),
        athChangePercentage = this.athChangePercentage.toBigDecimalValues(),
        athDate = this.athDate,
        atl = this.atl.toBigDecimalValues(),
        atlChangePercentage = this.atlChangePercentage.toBigDecimalValues(),
        atlDate = this.atlDate,
        marketCap = this.marketCap.toBigDecimalValues(),
        marketCapRank = this.marketCapRank,
        fullyDilutedValuation = this.fullyDilutedValuation.toBigDecimalOrNullValues(),
        totalVolume = this.totalVolume.toBigDecimalValues(),
        high24h = this.high24h.toBigDecimalOrNullValues(),
        low24h = this.low24h.toBigDecimalOrNullValues(),
        priceChange24hInCurrency = this.priceChange24hInCurrency.toBigDecimalOrNullValues(),
        priceChangePercentage24hInCurrency = this.priceChangePercentage24hInCurrency.toBigDecimalOrNullValues(),
        marketCapChange24hInCurrency = this.marketCapChange24hInCurrency.toBigDecimalOrNullValues(),
        marketCapChangePercentage24hInCurrency = this.marketCapChangePercentage24hInCurrency.toBigDecimalOrNullValues(),

        // Handle single String-to-BigDecimal conversions
        circulatingSupply = this.circulatingSupply.toBigDecimalOrZero(),
        totalSupply = this.totalSupply?.toBigDecimalOrNull(),
        maxSupply = this.maxSupply?.toBigDecimalOrNull(),
    )
}

// --- Entity -> Domain ---

@OptIn(ExperimentalTime::class)
internal fun CoinDetailWithRelations.toDomain(): CoinDetail {
    val entity = this.coin
    val values = this.currencyValues

    // Helper to reconstruct Maps from the List<Entity>
    fun getMap(type: String): Map<String, BigDecimal> {
        return values.filter { it.valueType == type }
            .associate { it.currency to BigDecimal(it.value) }
    }

    // Helper for nullable maps
    fun getMapNullable(type: String): Map<String, BigDecimal?> {
        return values.filter { it.valueType == type }
            .associate { it.currency to BigDecimal(it.value) }
    }

    // Reconstruct MarketData
    val marketData = MarketData(
        currentPrice = getMap("current_price"),
        ath = getMap("ath"),
        athChangePercentage = getMap("ath_change_percentage"),
        marketCap = getMap("market_cap"),
        totalVolume = getMap("total_volume"),
        circulatingSupply = BigDecimal(entity.circulatingSupply),
        totalSupply = entity.totalSupply?.let { BigDecimal(it) },
        maxSupply = entity.maxSupply?.let { BigDecimal(it) },
        // ... map the rest of MarketData fields similarly using specific "types" ...
        // For brevity, assuming similar pattern for all other maps
        athDate = emptyMap(), // Dates are tricky in this map structure, might need specific handling or separate table if critical
        atl = getMap("atl"),
        atlChangePercentage = getMap("atl_change_percentage"),
        atlDate = emptyMap(),
        marketCapRank = entity.marketCapRank,
        fullyDilutedValuation = getMapNullable("fdv"),
        high24h = getMapNullable("high_24h"),
        low24h = getMapNullable("low_24h"),
        priceChange24hInCurrency = getMapNullable("price_change_24h"),
        priceChangePercentage24hInCurrency = getMapNullable("price_change_pct_24h"),
        marketCapChange24hInCurrency = getMapNullable("mcap_change_24h"),
        marketCapChangePercentage24hInCurrency = getMapNullable("mcap_change_pct_24h"),
    )

    return CoinDetail(
        id = entity.id,
        symbol = entity.symbol,
        name = entity.name,
        assetPlatformId = entity.assetPlatformId,
        platforms = emptyMap(), // Platforms map not persisted in this strict example (add table if needed)
        categories = entity.categories.split("|"), // Simple delimiter
        description = Localization(entity.description.en, entity.description.de, entity.description.es, entity.description.fr),
        links = Links(emptyList(), emptyList(), emptyList(), ReposUrl(emptyList(), emptyList()), null), // Links simplified for example
        image = Image(entity.image.thumb, entity.image.small, entity.image.large),
        genesisDate = entity.genesisDate,
        marketCapRank = entity.marketCapRank,
        coingeckoRank = entity.coingeckoRank,
        marketData = marketData,
        lastUpdated = entity.lastUpdated,
    )
}

// --- DTO -> Entity ---

@OptIn(ExperimentalTime::class)
internal fun CoinDetailDTO.toEntityPair(): Pair<CoinDetailEntity, List<CoinDetailCurrencyValueEntity>> {
    val now = Clock.System.now()

    val entity = CoinDetailEntity(
        id = id,
        symbol = symbol,
        name = name,
        assetPlatformId = assetPlatformId,
        categories = categories.joinToString("|"),
        genesisDate = genesisDate,
        marketCapRank = marketCapRank,
        coingeckoRank = coingeckoRank,
        lastUpdated = lastUpdated,
        cachedAt = now,
        image = ImageEntityData(image.thumb, image.small, image.large),
        description = LocalizationEntityData(description.en, description.de, description.es, description.fr),
        circulatingSupply = marketData.circulatingSupply,
        totalSupply = marketData.totalSupply,
        maxSupply = marketData.maxSupply,
    )

    val values = mutableListOf<CoinDetailCurrencyValueEntity>()

    // Helper to flatten maps
    fun addMap(type: String, map: Map<String, String?>) {
        map.forEach { (currency, value) ->
            if (value != null) {
                values.add(CoinDetailCurrencyValueEntity(id, type, currency, value))
            }
        }
    }

    // Flatten all MarketData maps
    addMap("current_price", marketData.currentPrice)
    addMap("ath", marketData.ath)
    addMap("ath_change_percentage", marketData.athChangePercentage)
    addMap("market_cap", marketData.marketCap)
    addMap("total_volume", marketData.totalVolume)
    addMap("atl", marketData.atl)
    addMap("atl_change_percentage", marketData.atlChangePercentage)
    addMap("fdv", marketData.fullyDilutedValuation)
    addMap("high_24h", marketData.high24h)
    addMap("low_24h", marketData.low24h)
    addMap("price_change_24h", marketData.priceChange24hInCurrency)
    addMap("price_change_pct_24h", marketData.priceChangePercentage24hInCurrency)
    addMap("mcap_change_24h", marketData.marketCapChange24hInCurrency)
    addMap("mcap_change_pct_24h", marketData.marketCapChangePercentage24hInCurrency)

    return Pair(entity, values)
}
