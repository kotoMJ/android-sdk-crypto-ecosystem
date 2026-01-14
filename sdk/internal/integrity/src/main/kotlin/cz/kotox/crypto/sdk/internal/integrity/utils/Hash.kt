package cz.kotox.crypto.sdk.internal.integrity.utils

import android.util.Base64
import java.security.MessageDigest

internal fun String.toSha256Base64(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(this.toByteArray(Charsets.UTF_8))
    // Google Play Integrity accepts up to 500 bytes.
    // Base64-URL-safe is standard for these tokens.
    return Base64.encodeToString(hashBytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
}
