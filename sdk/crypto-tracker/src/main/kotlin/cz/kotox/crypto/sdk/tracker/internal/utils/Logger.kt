package cz.kotox.crypto.sdk.tracker.internal.utils

import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.toLogPriority
import cz.kotox.crypto.sdk.tracker.MODULE_IDENTIFIER

internal fun logV(message: () -> String) {
    SDKLogger.getLogger(MODULE_IDENTIFIER).log(LogPriority.VERBOSE, null, message)
}

internal fun logD(message: () -> String) {
    SDKLogger.getLogger(MODULE_IDENTIFIER).log(LogPriority.DEBUG, null, message)
}

internal fun logI(t: Throwable? = null, message: () -> String) {
    SDKLogger.getLogger(MODULE_IDENTIFIER).log(LogPriority.INFO, t, message)
}

internal fun logW(t: Throwable? = null, message: () -> String) {
    SDKLogger.getLogger(MODULE_IDENTIFIER).log(LogPriority.WARN, t, message)
}

internal fun logE(t: Throwable?, message: () -> String) {
    SDKLogger.getLogger(MODULE_IDENTIFIER).log(t.toLogPriority(), t, message)
}

internal fun log(sdkError: SdkError, message: () -> String) {
    SDKLogger.getLogger(MODULE_IDENTIFIER).log(sdkError.toLogPriority(), sdkError, message)
}
