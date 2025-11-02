package cz.kotox.crypto.sdk.common.extensions

import android.util.Patterns

@JvmSynthetic
public fun String.containsAnyOf(
    strings: List<String>,
    ignoreCase: Boolean = false,
): Boolean =
    strings.any { this.contains(it, ignoreCase = ignoreCase) }

public fun String.validUrlOrNull(): String? =
    this.trim().takeIf { it.isNotBlank() && Patterns.WEB_URL.matcher(it).matches() }
