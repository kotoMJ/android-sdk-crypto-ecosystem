package cz.kotox.crypto.sdk.common.error

import cz.kotox.crypto.sdk.common.extensions.containsAnyOf
import cz.kotox.crypto.sdk.common.logger.LogPriority

public sealed class ApiError(
    message: String,
    cause: Throwable? = null,
) : SdkError(message, cause) {

    public data class ConnectivityError(
        override val message: String,
        override val cause: Throwable? = null,
        val logPriority: LogPriority? = null,
    ) : ApiError(message, cause)

    public data class CancellationError(
        override val message: String,
        override val cause: Throwable? = null,
        val logPriority: LogPriority? = null,
    ) : ApiError(message, cause)

    public data class EmptyResponseError(
        override val message: String,
        override val cause: Throwable? = null,
    ) : ApiError(message, cause)

    public data class ResponseError(
        override val message: String,
        override val cause: Throwable? = null,
    ) : ApiError(message, cause)
}

public fun Exception.transformApiError(errorResponseMessage: String? = null): ApiError {
    return if (message?.containsAnyOf(
            strings = connectivityInfoDictionary,
            ignoreCase = true,
        ) == true
    ) {
        ApiError.ConnectivityError(
            message = message ?: "",
            cause = this,
            logPriority = LogPriority.INFO,
        )
    } else if (this is kotlinx.coroutines.CancellationException) {
        ApiError.CancellationError(
            message = message ?: "",
            cause = this,
            logPriority = LogPriority.VERBOSE,
        )
    } else {
        ApiError.ResponseError(
            message = errorResponseMessage ?: message ?: "Unknown",
            cause = this,
        )
    }
}

public val connectivityInfoDictionary: List<String> =
    listOf(
        "Unable to resolve host",
        "Software caused connection abort",
        "timeout",
        "Read timed out",
        "Broken pipe",
        "failed to connect",
        "Required SETTINGS preface not received",
        "Expected a SETTINGS frame but was",
        "unexpected end of stream",
        "No network connection is available",
        "stream was reset",
        "Connection reset",
        "connection closed",
        "Connection timed out",
        "SSL handshake timed out",
        "SSL handshake aborted",
        "exhausted all routes",
        "Connection closed by peer",
        "Read error",
        "ConnectionShutdownException",
    )
