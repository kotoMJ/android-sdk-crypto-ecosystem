package cz.kotox.crypto.sdk.tracker

import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerIdentifier
import kotlin.time.Duration

internal val MODULE_IDENTIFIER = SDKLoggerIdentifier("crypto-sdk-tracker")

/**
 * Configuration data for the content module.
 *
 * @property networkTimeout is duration of network timeout
 * @property loggerCallback specify proper logger instance

 * @property isLoggingEnabled is flag indicating whether logging is enabled.
 * @constructor creates initialization configuration.
 */
internal data class TrackerConfig(
    val networkTimeout: Duration,
    val loggerCallback: SDKLoggerCallback,
    val loggerIdentifier: SDKLoggerIdentifier = MODULE_IDENTIFIER,
    val isStrictModeEnabled: Boolean = false,
) {

    val isLoggingEnabled: Boolean = loggerCallback !is SDKLoggerCallbackNoOp
}
