package cz.kotox.crypto.sdk.internal.common.util.number

import java.math.BigDecimal

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

// Helper to convert Double to BigDecimal
// We convert to String first to avoid floating point artifacts (e.g. 0.1 -> 0.1000000005)
@Suppress("TooGenericExceptionCaught", "SwallowedException")
public fun Double?.toBigDecimal(): BigDecimal {
    return if (this == null) {
        BigDecimal.ZERO
    } else {
        try {
            BigDecimal(this.toString())
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }
}

public fun Double?.wrapInMap(key: String): Map<String, BigDecimal> {
    return if (this != null) {
        mapOf(key to this.toBigDecimal())
    } else {
        emptyMap()
    }
}

public fun BigDecimal?.wrapInMap(key: String): Map<String, BigDecimal> {
    return if (this != null) {
        mapOf(key to this)
    } else {
        emptyMap()
    }
}
