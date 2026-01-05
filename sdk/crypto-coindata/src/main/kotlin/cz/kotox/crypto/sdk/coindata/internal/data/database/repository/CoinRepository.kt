package cz.kotox.crypto.sdk.coindata.internal.data.database.repository

import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import cz.kotox.crypto.sdk.coindata.internal.data.api.CoinDataApiService
import cz.kotox.crypto.sdk.coindata.internal.data.database.dao.CoinDao
import cz.kotox.crypto.sdk.coindata.internal.data.mapper.toDomain
import cz.kotox.crypto.sdk.coindata.internal.data.mapper.toEntity
import cz.kotox.crypto.sdk.coindata.internal.data.mapper.toEntityPair
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.domain.model.coin.CurrencyId
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

internal class CoinRepository(
    private val apiService: CoinDataApiService,
    private val coinDataDao: CoinDao,
) {

    // Define cache duration using kotlin.time
    private val cacheDuration = 2.minutes

    @OptIn(ExperimentalTime::class)
    suspend fun getCoinMarkets(currency: CurrencyId): List<CoinMarket> {
        val now = Clock.System.now()

        // 1. Fetch from DB
        val localData = coinDataDao.getMarkets(currency.value)

        // 2. Check Validity
        // We check the first item to determine the age of the list
        val isCacheValid = if (localData.isNotEmpty()) {
            val cachedAtInstant = kotlin.time.Instant.fromEpochMilliseconds(localData.first().cachedAt)
            (now - cachedAtInstant) < cacheDuration
        } else {
            false
        }

        return if (isCacheValid) {
            localData.map { it.toDomain() }
        } else {
            // 3. Fetch from API
            val dtos = apiService.getMarkets(currency)

            // 4. Save to DB (with current timestamp)
            val entities = dtos.map { it.toEntity(currency.value) } // TODO parameter now.toEpochMilliseconds()?
            coinDataDao.insertMarkets(entities)

            entities.map { it.toDomain() }
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getCoinDetail(coinMarketId: CoinMarketId): CoinDetail {
        val now = Clock.System.now()

        val localData = coinDataDao.getCoinDetail(coinMarketId.value)

        val isCacheValid = if (localData != null) {
            val cachedAtInstant = kotlin.time.Instant.fromEpochMilliseconds(localData.coin.cachedAt)
            (now - cachedAtInstant) < cacheDuration
        } else {
            false
        }

        return if (isCacheValid) {
            localData!!.toDomain()
        } else {
            val dto = apiService.getCoinDetail(coinMarketId)

            // Convert DTO to Entity + Relation List
            val (entity, values) = dto.toEntityPair() // TODO parameter now.toEpochMilliseconds()?

            coinDataDao.insertCoinDetailWithRelations(entity, values)

            // Return mapped domain directly from the DTO we just fetched
            // (Or re-fetch from DB if you want strict source-of-truth consistency)
            dto.toDomain()
        }
    }
}
