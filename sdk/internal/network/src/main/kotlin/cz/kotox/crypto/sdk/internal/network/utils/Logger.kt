package cz.kotox.crypto.sdk.internal.network.utils

import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerIdentifier
import cz.kotox.crypto.sdk.internal.logger.toLogPriority

internal val MODULE_IDENTIFIER = SDKLoggerIdentifier("crypto-sdk-internal-network")

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
