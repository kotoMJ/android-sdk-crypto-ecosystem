package cz.kotox.crypto.sdk.monitoring

import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerIdentifier
import kotlin.time.Duration

internal val MODULE_IDENTIFIER = SDKLoggerIdentifier("crypto-sdk-monitoring")

/**
 * Configuration data for the monitoring module.
 *
 * @property networkTimeout duration of network timeout
 * @property loggerCallback logger instance observing SDK logs
 */
internal data class MonitoringConfig(
    val networkTimeout: Duration,
    val loggerCallback: SDKLoggerCallback,
) {

    val isLoggingEnabled: Boolean = loggerCallback !is SDKLoggerCallbackNoOp
}
