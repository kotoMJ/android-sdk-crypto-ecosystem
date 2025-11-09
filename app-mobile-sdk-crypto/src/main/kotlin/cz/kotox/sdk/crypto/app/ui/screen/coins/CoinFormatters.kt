package cz.kotox.sdk.crypto.app.ui.screen.coins

import java.text.DecimalFormat
import java.util.Locale

/**
 * Formats a large number (like market cap) into a short string (e.g., "$2.06T").
 */
fun formatMarketCap(number: Long): String {
    if (number < 1_000_000_000) {
        // Less than a billion, show as millions
        val millions = number / 1_000_000.0
        return String.format(locale = Locale.getDefault(), format = "$%.2fM", millions)
    }
    if (number < 1_000_000_000_000) {
        // Less than a trillion, show as billions
        val billions = number / 1_000_000_000.0
        return String.format(locale = Locale.getDefault(), format = "$%.2fB", billions)
    }
    // Show as trillions
    val trillions = number / 1_000_000_000_000.0
    return String.format(locale = Locale.getDefault(), format = "$%.2fT", trillions)
}

/**
 * Formats a standard price with a dollar sign, commas, and two decimal places.
 * e.g., 103618.0 -> "$103,618.00"
 */
fun formatCurrency(number: Double): String {
    val formatter = DecimalFormat("$#,##0.00")
    return formatter.format(number)
}

/**
 * Formats a percentage with a leading + or - sign and two decimal places.
 * e.g., 1.68189 -> "+1.68%"
 * e.g., -0.5 -> "-0.50%"
 */
fun formatPercentage(number: Double): String {
    // The + flag adds a sign, .2f limits to 2 decimal places, and %% escapes the % symbol
    return String.format(locale = Locale.getDefault(), format = "%+.2f%%", number)
}
