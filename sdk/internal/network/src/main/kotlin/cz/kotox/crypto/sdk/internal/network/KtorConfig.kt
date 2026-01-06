package cz.kotox.crypto.sdk.internal.network

import cz.kotox.crypto.sdk.common.configuration.LoggingPolicy
import cz.kotox.crypto.sdk.common.configuration.StrictModePolicy
import kotlin.time.Duration

public data class KtorConfig(
    val baseUrl: String,
    val networkTimeout: Duration,
    val isLoggingEnabled: Boolean,
    val strictModePolicy: StrictModePolicy,
    val loggingPolicy: LoggingPolicy,
)
