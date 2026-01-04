package cz.kotox.crypto.sdk.coindata.internal.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailStaticEntity
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinMarketEntity

@Dao
internal interface CoinDao {

    // --- Market Data (Fast) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkets(markets: List<CoinMarketEntity>)

    @Query("SELECT * FROM coin_markets WHERE id = :id")
    suspend fun getMarket(id: String): CoinMarketEntity?

    // --- Static Data (Slow) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaticDetail(detail: CoinDetailStaticEntity)

    @Query("SELECT * FROM coin_detail_static WHERE id = :id")
    suspend fun getStaticDetail(id: String): CoinDetailStaticEntity?
}
