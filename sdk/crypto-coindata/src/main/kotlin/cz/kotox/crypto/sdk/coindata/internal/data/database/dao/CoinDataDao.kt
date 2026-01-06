package cz.kotox.crypto.sdk.coindata.internal.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailCurrencyValueEntity
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailEntity
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailWithRelations
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinMarketEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CoinDataDao {

    // --- Markets ---

    // 1. Reactive stream for UI
    @Query("SELECT * FROM coin_markets WHERE vs_currency = :currency ORDER BY marketCapRank ASC")
    fun getMarketsFlow(currency: String): Flow<List<CoinMarketEntity>>

    // 2. One-shot fetch for Repository cache-check
    @Query("SELECT * FROM coin_markets WHERE vs_currency = :currency ORDER BY marketCapRank ASC")
    suspend fun getMarkets(currency: String): List<CoinMarketEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkets(markets: List<CoinMarketEntity>)

    // --- Detail ---

    // 1. Reactive stream for UI
    @Transaction
    @Query("SELECT * FROM coin_details WHERE id = :id")
    fun getCoinDetailFlow(id: String): Flow<CoinDetailWithRelations?>

    // 2. One-shot fetch for Repository cache-check
    @Transaction
    @Query("SELECT * FROM coin_details WHERE id = :id")
    suspend fun getCoinDetail(id: String): CoinDetailWithRelations?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinDetail(detail: CoinDetailEntity)

    // We replace the currency values (prices) for this coin
    @Query("DELETE FROM coin_detail_currency_values WHERE coinId = :coinId")
    suspend fun deleteCurrencyValues(coinId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyValues(values: List<CoinDetailCurrencyValueEntity>)

    @Transaction
    suspend fun insertCoinDetailWithRelations(
        detail: CoinDetailEntity,
        values: List<CoinDetailCurrencyValueEntity>,
    ) {
        insertCoinDetail(detail)
        deleteCurrencyValues(detail.id)
        insertCurrencyValues(values)
    }
}
