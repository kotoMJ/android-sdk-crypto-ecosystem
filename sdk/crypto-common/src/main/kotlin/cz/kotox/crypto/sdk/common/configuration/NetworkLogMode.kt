package cz.kotox.crypto.sdk.common.configuration

/**
 * Mutually exclusive network logging levels.
 */
public enum class NetworkLogMode {
    /**
     * Logs only the request method, URL, and response status code.
     * Safe for production.
     * Maps to Ktor [io.ktor.client.plugins.logging.LogLevel.INFO]
     * with sdk log priority [cz.kotox.crypto.sdk.common.logger.LogPriority.INFO].
     */
    BASIC,

    /**
     * Logs BASIC info plus request/response headers.
     * Useful for debugging auth/caching without leaking payload data.
     * Maps to Ktor [io.ktor.client.plugins.logging.LogLevel.HEADERS]
     * with sdk log priority [cz.kotox.crypto.sdk.common.logger.LogPriority.DEBUG].
     */
    HEADERS,

    /**
     * Logs everything: Headers AND Body.
     * **Warning:** Extremely verbose and may leak PII.
     * Maps to Ktor [io.ktor.client.plugins.logging.LogLevel.ALL]
     * with sdk log priority [cz.kotox.crypto.sdk.common.logger.LogPriority.VERBOSE].
     */
    BODY,
}
