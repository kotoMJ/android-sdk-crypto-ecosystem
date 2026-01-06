package cz.kotox.crypto.sdk.common.configuration

/**
 * Granular control over diagnostic logging.
 *
 * Use this to enable verbose tracing for specific subsystems (Database, Network, etc.)
 * without flooding the logs globally.
 */
public data class LoggingPolicy(
    /**
     * If true, the SDK will log every SQL query executed by Room.
     * **Warning:** Extremely verbose. Use only for debugging specific query performance or syntax issues.
     */
    val logDatabaseQueries: Boolean = false,

    /**
     * Defines the verbosity of the network logger.
     * Default is BASIC (Production safe).
     */
    val networkLogMode: NetworkLogMode = NetworkLogMode.BASIC,
)
