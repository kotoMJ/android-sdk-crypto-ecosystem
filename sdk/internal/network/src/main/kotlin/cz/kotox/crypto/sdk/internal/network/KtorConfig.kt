package cz.kotox.crypto.sdk.internal.network

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public data class KtorConfig(
    val baseUrl: String,
    val networkTimeout: Duration = 30.seconds,
    val isLoggingEnabled: Boolean = false,
    val isStrictModeEnabled: Boolean = false,
)
