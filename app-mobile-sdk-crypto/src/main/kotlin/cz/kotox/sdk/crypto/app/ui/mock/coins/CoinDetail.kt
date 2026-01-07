package cz.kotox.sdk.crypto.app.ui.mock.coins

import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.domain.model.Image
import cz.kotox.crypto.sdk.coindata.domain.model.Links
import cz.kotox.crypto.sdk.coindata.domain.model.Localization
import cz.kotox.crypto.sdk.coindata.domain.model.MarketData
import cz.kotox.crypto.sdk.coindata.domain.model.ReposUrl
import java.math.BigDecimal
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal val coinDetailBitcoin = CoinDetail(
    id = "bitcoin",
    symbol = "BTC",
    name = "Bitcoin",
    image = Image(
        thumb = "https://assets.coingecko.com/coins/images/1/thumb/bitcoin.png",
        small = "https://assets.coingecko.com/coins/images/1/small/bitcoin.png",
        large = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
    ),
    marketCapRank = 1,
    coingeckoRank = 1,
    genesisDate = "2009-01-03",
    description = Localization(
        en = "Bitcoin is the first successful internet money based on peer-to-peer technology.",
        de = null,
        es = null,
        fr = null,
    ),
    links = Links(
        homepage = listOf("http://www.bitcoin.org"),
        blockchainSite = listOf(),
        officialForumUrl = listOf(),
        reposUrl = ReposUrl(github = listOf(), bitbucket = listOf()),
    ),
    platforms = emptyMap(),
    categories = listOf("Cryptocurrency"),
    assetPlatformId = null,
    lastUpdated = null,
    marketData = MarketData(
        // We use BigDecimal("String") to ensure precision in the mock
        currentPrice = mapOf("usd" to BigDecimal("45200.00")),

        // Price Change
        priceChangePercentage24hInCurrency = mapOf("usd" to BigDecimal("12.5")),
        priceChange24hInCurrency = mapOf("usd" to BigDecimal("5000.00")),

        // Market Cap
        marketCap = mapOf("usd" to BigDecimal("890000000000")), // 890B
        marketCapChangePercentage24hInCurrency = mapOf("usd" to BigDecimal("2.5")),
        marketCapChange24hInCurrency = mapOf("usd" to BigDecimal("100000")),
        marketCapRank = 1,

        // Volume
        totalVolume = mapOf("usd" to BigDecimal("35000000000")), // 35B

        // Supply
        circulatingSupply = BigDecimal("17700000"), // 17.7M
        totalSupply = BigDecimal("21000000"),
        maxSupply = BigDecimal("21000000"),

        // ATH & ATL
        ath = mapOf("usd" to BigDecimal("69000.00")),
        athChangePercentage = mapOf("usd" to BigDecimal("-34.0")),
        atl = mapOf("usd" to BigDecimal("67.81")),
        atlChangePercentage = mapOf("usd" to BigDecimal("80000.0")),

        // Null/Empty fields not used in this specific UI design
        athDate = emptyMap(),
        atlDate = emptyMap(),
        fullyDilutedValuation = emptyMap(),
        high24h = emptyMap(),
        low24h = emptyMap(),
    ),
)
