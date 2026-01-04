package cz.kotox.crypto.sdk.coindata.internal.data.database

import androidx.room.TypeConverter
import cz.kotox.crypto.sdk.coindata.internal.data.database.converters.IcuBigDecimalConverter
import java.time.Instant

internal class CoinRoomConverters :
    IcuBigDecimalConverter {

    @TypeConverter
    fun stringToInstant(value: String?): Instant? = value?.let { Instant.parse(it) }

    @TypeConverter
    fun instantToString(value: Instant?): String? = value?.toString()
}
