package cz.kotox.crypto.sdk.internal.logger

import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.logger.impl.AndroidLogger
import cz.kotox.crypto.sdk.internal.logger.impl.NoOpLogger

class SDKLoggerCallbackNoOp : SDKLoggerCallback {
    override fun onLogMessage(
        tag: String,
        priority: LogPriority,
        t: Throwable?,
        message: String,
    ) {
        // No-op
    }
}

interface SDKLogger {
    companion object {
        private val loggers = mutableMapOf<String, SDKLogger>()

        fun getLogger(tag: SDKLoggerIdentifier): SDKLogger {
            return loggers[tag.value] ?: NoOpLogger
        }

        @JvmStatic
        fun install(
            tag: SDKLoggerIdentifier,
            loggerCallback: SDKLoggerCallback,
        ) {
            loggers[tag.value] =
                AndroidLogger(originalTag = tag.value, loggerCallback = loggerCallback)
        }

        @JvmStatic
        fun uninstall(tag: SDKLoggerIdentifier) {
            loggers[tag.value] = NoOpLogger
        }
    }

    val canLog: Boolean

    val loggerCallback: SDKLoggerCallback

    fun log(
        priority: LogPriority,
        t: Throwable? = null,
        message: () -> String,
    )
}
