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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@Suppress("TooGenericExceptionCaught")
internal class CoinRepository(
    private val apiService: CoinDataApiService,
    private val coinDataDao: CoinDao,
) {

    // Define cache duration using kotlin.time
    private val cacheDuration = 2.minutes

    /**
     * returns a Flow that:
     * 1. Immediately emits DB data (if any).
     * 2. Checks if data is stale. If so, fetches API, saves to DB.
     * 3. DB save triggers a new emission in step 1.
     */
    @OptIn(ExperimentalTime::class)
    fun getCoinMarkets(currency: CurrencyId): Flow<List<CoinMarket>> = channelFlow {
        val now = Clock.System.now()

        // Job 1: Observe Database (Continuous)
        // We launch this in a separate coroutine so it doesn't block the Refresh check
        launch {
            coinDataDao.getMarketsFlow(currency.value).collectLatest { entities ->
                send(entities.map { it.toDomain() })
            }
        }

        // Job 2: Refresh Cache (One-shot)
        launch {
            val localData = coinDataDao.getMarkets(currency.value)

            // Check Validity
            val isCacheValid = if (localData.isNotEmpty()) {
                val cachedAtInstant = kotlin.time.Instant.fromEpochMilliseconds(localData.first().cachedAt)
                (now - cachedAtInstant) < cacheDuration
            } else {
                false
            }

            if (!isCacheValid) {
                try {
                    val dtos = apiService.getMarkets(currency)
                    // Pass current time for the 'cachedAt' field
                    val entities = dtos.map { it.toEntity(currency.value) } // TODO now.toEpochMilliseconds() as parameter?
                    coinDataDao.insertMarkets(entities)
                } catch (e: Exception) {
                    // In a real app, you might want to send an error state or log this.
                    // For now, we suppress network errors so the user keeps seeing cached data.
                    e.printStackTrace()
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun getCoinDetail(coinMarketId: CoinMarketId): Flow<CoinDetail> = channelFlow {
        val now = Clock.System.now()

        // Job 1: Observe Database
        launch {
            coinDataDao.getCoinDetailFlow(coinMarketId.value).collectLatest { relation ->
                // Only emit if we have data. If DB is empty, we wait for the network fetch.
                relation?.let {
                    send(it.toDomain())
                }
            }
        }

        // Job 2: Refresh Cache
        launch {
            val localData = coinDataDao.getCoinDetail(coinMarketId.value)

            val isCacheValid = if (localData != null) {
                val cachedAtInstant = kotlin.time.Instant.fromEpochMilliseconds(localData.coin.cachedAt)
                (now - cachedAtInstant) < cacheDuration
            } else {
                false
            }

            if (!isCacheValid) {
                try {
                    val dto = apiService.getCoinDetail(coinMarketId)

                    // Convert DTO to Entity + Relation List with timestamp
                    val (entity, values) = dto.toEntityPair() // TODO now.toEpochMilliseconds() as parameter?

                    coinDataDao.insertCoinDetailWithRelations(entity, values)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
