package cz.kotox.sdk.crypto.app.utils.formatter

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Suppress("TooGenericExceptionCaught", "SwallowedException")
fun BigDecimal?.formatAmountCurrency(): String {
    if (this == null) return "N/A"
    return try {
        NumberFormat.getCurrencyInstance(Locale.US).format(this)
    } catch (e: Exception) {
        this.toString()
    }
}

fun BigDecimal?.formatAmountCompact(): String {
    if (this == null) return "N/A"
    val doubleVal = this.toDouble()
    return when {
        doubleVal >= 1_000_000_000 -> String.format(Locale.US, "%.2fB", doubleVal / 1_000_000_000)
        doubleVal >= 1_000_000 -> String.format(Locale.US, "%.2fM", doubleVal / 1_000_000)
        doubleVal >= 1_000 -> String.format(Locale.US, "%.2fK", doubleVal / 1_000)
        else -> String.format(Locale.US, "%.2f", doubleVal)
    }
}
