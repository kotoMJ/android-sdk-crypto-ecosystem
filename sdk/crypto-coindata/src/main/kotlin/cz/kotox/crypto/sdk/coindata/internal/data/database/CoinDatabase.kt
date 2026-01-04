package cz.kotox.crypto.sdk.coindata.internal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.kotox.crypto.sdk.coindata.internal.data.database.dao.CoinDao
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailStaticEntity
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinMarketEntity
import java.io.File

@Database(
    entities = [
        CoinMarketEntity::class,
        CoinDetailStaticEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(CoinRoomConverters::class)
internal abstract class CoinDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    /**
     * Get the approximate size of the database in bytes
     * @return approximate size of the database in bytes
     */
    internal fun sizeInBytes(): Long {
        val path = requireNotNull(openHelper.readableDatabase.path)
        return File(path).length()
    }
}
