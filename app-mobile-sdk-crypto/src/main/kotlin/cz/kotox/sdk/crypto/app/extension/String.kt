package cz.kotox.sdk.crypto.app.extension

import java.util.Locale

fun String.toTitleCase(): String =
    lowercase()
        .replaceFirstChar {
            if (it.isLowerCase()) {
                it.titlecase(Locale.getDefault())
            } else {
                it.toString()
            }
        }
