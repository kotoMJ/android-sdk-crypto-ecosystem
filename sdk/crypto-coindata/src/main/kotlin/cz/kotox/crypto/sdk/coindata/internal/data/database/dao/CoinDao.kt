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

@Dao
internal interface CoinDao {

    // --- Markets ---
    @Query("SELECT * FROM coin_markets WHERE vs_currency = :currency ORDER BY marketCapRank ASC")
    suspend fun getMarkets(currency: String): List<CoinMarketEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkets(markets: List<CoinMarketEntity>)

    // --- Detail ---
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
