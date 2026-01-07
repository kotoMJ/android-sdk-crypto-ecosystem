package cz.kotox.crypto.sdk.coindata.internal.data.database.converters

import androidx.room.TypeConverter
import java.math.BigDecimal

internal interface IcuBigDecimalConverter {

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }
}
