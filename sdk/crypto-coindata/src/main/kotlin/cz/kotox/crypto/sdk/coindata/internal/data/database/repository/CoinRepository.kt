package cz.kotox.crypto.sdk.coindata.internal.data.database.repository

import android.icu.math.BigDecimal
import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.domain.model.Image
import cz.kotox.crypto.sdk.coindata.domain.model.Links
import cz.kotox.crypto.sdk.coindata.domain.model.Localization
import cz.kotox.crypto.sdk.coindata.domain.model.MarketData
import cz.kotox.crypto.sdk.coindata.domain.model.ReposUrl
import cz.kotox.crypto.sdk.coindata.internal.data.api.CoinGeckoApi
import cz.kotox.crypto.sdk.coindata.internal.data.database.dao.CoinDao
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailStaticEntity
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinMarketEntity
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.internal.common.util.number.toBigDecimalOrNull
import cz.kotox.crypto.sdk.internal.common.util.number.wrapInMap
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// Cache validity: 24 hours for descriptions
private const val STATIC_CACHE_TIMEOUT = 24 * 60 * 60 * 1000L

@Suppress("TooGenericExceptionCaught")
internal class CoinRepository(
    private val api: CoinGeckoApi,
    private val dao: CoinDao,
) {

    suspend fun getCoinDetail(coinId: String): Either<SdkError, CoinDetail> {
        val currency: String = "usd" // TODO MJ - make this dynamic

        // 1. Try to load from DB (Stitching)
        val marketEntity = dao.getMarket(coinId)
        val staticEntity = dao.getStaticDetail(coinId)

        // 2. STITCH CHECK: Do we have both parts?
        if (marketEntity != null && staticEntity != null) {
            val isStaticFresh = (System.currentTimeMillis() - staticEntity.lastFetched) < STATIC_CACHE_TIMEOUT

            if (isStaticFresh) {
                // SUCCESS: Return without any API call
                return Either.Value(
                    mapEntitiesToDomain(
                        market = marketEntity,
                        staticInfo = staticEntity,
                        currency = currency,
                    ),
                )
            }
        }

        // 3. Fallback: Call API if static data is missing or stale
        return try {
            // Call the endpoint defined in CoinGeckoApi.kt
            val detailDto = api.getCoinDetail(coinId)

            // SAVE ONLY STATIC PARTS
            val newStaticEntity = CoinDetailStaticEntity(
                id = detailDto.id,
                descriptionEn = detailDto.description.en, //
                genesisDate = detailDto.genesisDate,
                homepageUrl = detailDto.links.homepage.firstOrNull(),
                githubUrl = detailDto.links.reposUrl.github.firstOrNull(),
                lastFetched = System.currentTimeMillis(),
            )
            dao.insertStaticDetail(newStaticEntity)

            // If we have market data (from the list), stitch it.
            // If not (edge case), we might need to fetch market data or use the `market_data` from DTO temporarily.
            // Assuming market data exists from the list screen:
            val safeMarketEntity = marketEntity ?: fetchAndSaveSingleMarket(coinId = coinId, currency = currency)

            Either.Value(
                mapEntitiesToDomain(
                    market = safeMarketEntity,
                    staticInfo = newStaticEntity,
                    currency = currency,
                ),
            )
        } catch (e: Exception) {
            // Error handling (429, etc.)
            // If we have STALE data in DB, return it instead of error!
            if (marketEntity != null && staticEntity != null) {
                Either.Value(
                    mapEntitiesToDomain(
                        market = marketEntity,
                        staticInfo = staticEntity,
                        currency = currency,
                    ),
                )
            } else {
                Either.Error(ApiError.ResponseError(e.message ?: "Unable to fetch getCoinDetail", e))
            }
        }
    }

    // Helper to merge the two Entities into your Domain Model
    @OptIn(ExperimentalTime::class)
    private fun mapEntitiesToDomain(
        market: CoinMarketEntity,
        staticInfo: CoinDetailStaticEntity,
        currency: String,
    ): CoinDetail {
        return CoinDetail(
            id = market.id,
            symbol = market.symbol,
            name = market.name,
            assetPlatformId = null, // Not stored in market entity
            platforms = emptyMap(), // Simplification for offline mode
            categories = emptyList(),
            description = Localization(en = staticInfo.descriptionEn, de = null, es = null, fr = null),
            links = Links(
                homepage = listOfNotNull(staticInfo.homepageUrl),
                blockchainSite = emptyList(),
                officialForumUrl = emptyList(),
                reposUrl = ReposUrl(github = listOfNotNull(staticInfo.githubUrl), bitbucket = emptyList()),
            ),
            image = Image(thumb = "", small = "", large = market.imageUrl),
            genesisDate = staticInfo.genesisDate,
            marketCapRank = null,
            coingeckoRank = null,
            lastUpdated = null,
            // VITAL: Reconstruct MarketData from the fast Market Entity
            // The Mapping Logic
            marketData = MarketData(
                // 1. Basic Prices
                currentPrice = mapOf(currency to market.currentPrice),

                // 2. ATH
                ath = market.ath.wrapInMap(currency),
                athChangePercentage = market.athChangePercentage.wrapInMap(currency),
                athDate = mapOf(currency to Instant.fromEpochMilliseconds(market.athDate)),

                // 3. ATL
                atl = market.atl.wrapInMap(currency),
                atlChangePercentage = market.atlChangePercentage.wrapInMap(currency),
                atlDate = mapOf(currency to Instant.fromEpochMilliseconds(market.atlDate)),

                // 4. Ranks & Valuation
                marketCap = mapOf(currency to BigDecimal(market.marketCap.toString())), // Long -> BigDecimal is safe
                marketCapRank = market.marketCapRank,
                fullyDilutedValuation = market.fullyDilutedValuation?.let {
                    mapOf(currency to BigDecimal(it.toString()))
                } ?: emptyMap(),

                // 5. Volume & Changes
                totalVolume = market.totalVolume.wrapInMap(currency),
                high24h = market.high24h.wrapInMap(currency),
                low24h = market.low24h.wrapInMap(currency),

                priceChange24hInCurrency = market.priceChange24h.wrapInMap(currency),
                priceChangePercentage24hInCurrency = market.priceChangePercentage24h.wrapInMap(currency),

                marketCapChange24hInCurrency = market.marketCapChange24h.wrapInMap(currency),
                marketCapChangePercentage24hInCurrency = market.marketCapChangePercentage24h.wrapInMap(currency),

                // 6. Supply (These are single fields in Entity, simple BigDecimal in Domain)
                circulatingSupply = market.circulatingSupply,
                totalSupply = market.totalSupply,
                maxSupply = market.maxSupply,
            ),
        )
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun fetchAndSaveSingleMarket(
        coinId: String,
        currency: String,
    ): CoinMarketEntity {
        // 1. Fetch the full detail DTO (contains everything)
        val detailDto = api.getCoinDetail(coinId)

        // 2. Map "Sideways": DetailDTO -> CoinMarketEntity
        // We extract the 'usd' values because CoinMarketEntity is flat
        val marketData = detailDto.marketData

        val newEntity = CoinMarketEntity(
            id = detailDto.id,
            symbol = detailDto.symbol,
            name = detailDto.name,
            imageUrl = detailDto.image.large,

            // Convert String -> Double for the Entity
            currentPrice = marketData.currentPrice[currency]?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
            marketCap = marketData.marketCap[currency]?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
            marketCapRank = detailDto.marketCapRank ?: 0,
            totalVolume = marketData.totalVolume[currency]?.toBigDecimalOrNull() ?: BigDecimal.ZERO,

            high24h = marketData.high24h[currency]?.toBigDecimalOrNull(),
            low24h = marketData.low24h[currency]?.toBigDecimalOrNull(),

            priceChange24h = marketData.priceChange24hInCurrency[currency]?.toBigDecimalOrNull(),
            priceChangePercentage24h = marketData.priceChangePercentage24hInCurrency[currency]?.toDoubleOrNull(),

            marketCapChange24h = marketData.marketCapChange24hInCurrency[currency]?.toBigDecimalOrNull(),
            marketCapChangePercentage24h = marketData.marketCapChangePercentage24hInCurrency[currency]?.toDoubleOrNull(),

            circulatingSupply = marketData.circulatingSupply.toBigDecimalOrNull() ?: BigDecimal.ZERO,
            totalSupply = marketData.totalSupply?.toBigDecimalOrNull(),
            maxSupply = marketData.maxSupply?.toBigDecimalOrNull(),

            fullyDilutedValuation = marketData.fullyDilutedValuation[currency]?.toBigDecimalOrNull(),

            ath = marketData.ath[currency]?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
            athChangePercentage = marketData.athChangePercentage[currency]?.toDoubleOrNull() ?: 0.0,
            athDate = marketData.athDate[currency]?.toEpochMilliseconds() ?: 0L,

            atl = marketData.atl[currency]?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
            atlChangePercentage = marketData.atlChangePercentage[currency]?.toDoubleOrNull() ?: 0.0,
            atlDate = marketData.atlDate[currency]?.toEpochMilliseconds() ?: 0L,

            lastUpdated = System.currentTimeMillis(),
        )
        // 3. Save to the "Fast" table
        dao.insertMarkets(listOf(newEntity))

        return newEntity
    }
}
