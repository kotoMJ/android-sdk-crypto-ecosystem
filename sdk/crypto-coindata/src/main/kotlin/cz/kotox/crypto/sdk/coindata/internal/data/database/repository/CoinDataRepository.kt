package cz.kotox.crypto.sdk.coindata.internal.data.database.repository

import cz.kotox.crypto.sdk.coindata.domain.CoinDataRequestContext
import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import cz.kotox.crypto.sdk.coindata.internal.data.mapper.toDomain
import cz.kotox.crypto.sdk.coindata.internal.data.mapper.toEntity
import cz.kotox.crypto.sdk.coindata.internal.data.mapper.toEntityPair
import cz.kotox.crypto.sdk.coindata.internal.utils.logE
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.domain.model.coin.CurrencyId
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.common.fold
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@Suppress("TooGenericExceptionCaught")
internal class CoinDataRepository(
    private val context: CoinDataRequestContext,
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
    fun getCoinMarkets(currency: CurrencyId): Flow<Either<SdkError, List<CoinMarket>>> =
        with(context) {
            channelFlow {
                val now = Clock.System.now()

                // Job 1: Observe Database (Continuous)
                launch {
                    withDatabase("getCoinMarkets subscribeMarkets") {
                        coinDataDao().getMarketsFlow(currency.value).collectLatest { entities ->
                            send(Either.Value(entities.map { it.toDomain() }))
                        }
                    }
                }

                // Job 2: Refresh Cache (One-shot)
                launch {
                    withDatabase("getCoinMarkets refreshMarkets") {
                        val localData = coinDataDao().getMarkets(currency.value)

                        // Check Validity
                        val isCacheValid = if (localData.isNotEmpty()) {
                            val cachedAtInstant = localData.first().cachedAt
                            (now - cachedAtInstant) < cacheDuration
                        } else {
                            false
                        }

                        if (!isCacheValid) {
                            context.withApi { getMarkets(currency) }.fold(
                                { apiError ->
                                    logE(apiError, { "Unable to refresh getCoinMarkets cache" })
                                },
                                { dtos ->
                                    val entities = dtos.map { it.toEntity(currency.value) } // TODO now.toEpochMilliseconds() as parameter?
                                    coinDataDao().insertMarkets(entities)
                                },
                            )
                        }
                    }
                }
            }
        }

    @OptIn(ExperimentalTime::class)
    fun getCoinDetail(coinMarketId: CoinMarketId): Flow<Either<SdkError, CoinDetail>> =
        with(context) {
            channelFlow {
                val now = Clock.System.now()

                // Job 1: Observe Database
                launch {
                    withDatabase("getCoinDetail subscribeCoinDetailFlow") {
                        coinDataDao().getCoinDetailFlow(coinMarketId.value).collectLatest { relation ->
                            relation?.let {
                                send(Either.Value(it.toDomain()))
                            }
                        }
                    }
                }

                // Job 2: Refresh Cache
                launch {
                    withDatabase("getCoinDetail subscribeCoinDetailFlow") {
                        val localData = coinDataDao().getCoinDetail(coinMarketId.value)

                        val isCacheValid = if (localData != null) {
                            val cachedAtInstant = localData.coin.cachedAt
                            (now - cachedAtInstant) < cacheDuration
                        } else {
                            false
                        }

                        if (!isCacheValid) {
                            context.withApi { getCoinDetail(coinMarketId) }.fold(
                                { apiError ->
                                    logE(apiError, { "Unable to refresh getCoinMarkets cache" })
                                },
                                { dto ->
                                    val (entity, values) = dto.toEntityPair() // TODO now.toEpochMilliseconds() as parameter?

                                    coinDataDao().insertCoinDetailWithRelations(entity, values)
                                },
                            )
                        }
                    }
                }
            }
        }
}
