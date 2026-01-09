package cz.kotox.sdk.crypto.app.utils.formatter

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Suppress("TooGenericExceptionCaught", "SwallowedException")
fun Instant.formatDateArticle(): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm", Locale.US)
            .withZone(ZoneId.systemDefault())
        formatter.format(this.toJavaInstant())
    } catch (e: Exception) {
        this.toString()
    }
}
