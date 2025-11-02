package cz.kotox.crypto.sdk.coindata.internal.data.mapper

import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.domain.model.Image
import cz.kotox.crypto.sdk.coindata.domain.model.Links
import cz.kotox.crypto.sdk.coindata.domain.model.Localization
import cz.kotox.crypto.sdk.coindata.domain.model.MarketData
import cz.kotox.crypto.sdk.coindata.domain.model.ReposUrl
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
