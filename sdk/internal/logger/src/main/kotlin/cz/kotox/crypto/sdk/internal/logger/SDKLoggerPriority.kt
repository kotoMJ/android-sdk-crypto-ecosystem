package cz.kotox.crypto.sdk.internal.logger

import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.common.error.connectivityInfoDictionary
import cz.kotox.crypto.sdk.common.extensions.containsAnyOf
import cz.kotox.crypto.sdk.common.logger.LogPriority
import kotlinx.coroutines.CancellationException

/**
 * This is reducing noise in the error/warning logs since
 * connectivity issue, empty responses, jobCancellation, ... are really common and
 * are just causing false positives.
 */

public fun SdkError.toLogPriority(): LogPriority =
    when (this) {
        is ApiError.ConnectivityError -> this.logPriority ?: LogPriority.INFO
        is ApiError.CancellationError -> this.logPriority ?: LogPriority.VERBOSE
        is ApiError.EmptyResponseError -> LogPriority.INFO
        else -> LogPriority.ERROR
    }

public fun Throwable?.toLogPriority(): LogPriority =
    when (this) {
        is CancellationException -> LogPriority.VERBOSE
        else -> {
            if (this?.message?.containsAnyOf(
                    strings = connectivityInfoDictionary,
                    ignoreCase = true,
                ) == true
            ) {
                LogPriority.INFO
            } else {
                LogPriority.ERROR
            }
        }
    }
