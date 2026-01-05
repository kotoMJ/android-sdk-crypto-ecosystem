package cz.kotox.crypto.sdk.coindata.internal.data.database.converters

import androidx.room.TypeConverter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal interface InstantConverter {
    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun fromInstant(value: Instant?): String? {
        return value?.toString() // ISO-8601 format, e.g., "2023-10-05T12:00:00Z"
    }

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun toInstant(value: String?): Instant? {
        return value?.let { Instant.parse(it) }
    }
}
