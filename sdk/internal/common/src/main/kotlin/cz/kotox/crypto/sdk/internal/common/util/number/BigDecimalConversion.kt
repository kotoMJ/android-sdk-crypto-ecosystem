package cz.kotox.crypto.sdk.internal.common.util.number

import android.icu.math.BigDecimal

/**
 * Safely converts a [String] to [BigDecimal].
 * If the string is null or invalid, returns [BigDecimal.ZERO].
 */
public fun String?.toBigDecimalOrZero(): BigDecimal {
    return this.toBigDecimalOrNull() ?: BigDecimal.ZERO
}

/**
 * Safely converts a [String] to [BigDecimal].
 * If the string is null or invalid, returns null.
 */
public fun String?.toBigDecimalOrNull(): BigDecimal? {
    return this?.let {
        try {
            BigDecimal(it)
        } catch (e: NumberFormatException) {
            // Optional: Log the error, e.g., Log.w("Mapper", "Invalid number format: $it")
            null
        }
    }
}

/**
 * Helper to convert a Map's values from [String] to [BigDecimal].
 * Uses [toBigDecimalOrZero] for conversion.
 */
public fun Map<String, String>.toBigDecimalValues(): Map<String, BigDecimal> {
    return this.mapValues { it.value.toBigDecimalOrZero() }
}

/**
 * Helper to convert a Map's values from [String?] to [BigDecimal?].
 * Uses [toBigDecimalOrNull] for conversion.
 */
public fun Map<String, String?>.toBigDecimalOrNullValues(): Map<String, BigDecimal?> {
    return this.mapValues { it.value.toBigDecimalOrNull() }
}
