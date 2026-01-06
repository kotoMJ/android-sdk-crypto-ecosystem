package cz.kotox.crypto.sdk.coindata.internal.data.database.converters

import android.icu.math.BigDecimal
import androidx.room.TypeConverter

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
