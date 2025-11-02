package cz.kotox.crypto.sdk.internal.logger.impl

import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp

internal object NoOpLogger : SDKLogger {
    override val canLog: Boolean get() = false
    override var loggerCallback: SDKLoggerCallback = SDKLoggerCallbackNoOp()

    override fun log(
        priority: LogPriority,
        t: Throwable?,
        message: () -> String,
    ) {
        // No-op
    }
}
