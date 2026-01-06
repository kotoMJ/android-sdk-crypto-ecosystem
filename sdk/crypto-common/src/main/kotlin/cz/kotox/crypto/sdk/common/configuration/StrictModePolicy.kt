package cz.kotox.crypto.sdk.common.configuration

/**
 * Configuration for the SDK's strictness and fault tolerance.
 *
 * This policy controls how the SDK handles violations of internal contracts (such as
 * API schema mismatches or performance rules).
 *
 * - **Strict Mode (Development):** The SDK follows a "Fail Fast" strategy. It throws exceptions
 * immediately when expectations are not met, ensuring bugs are caught early.
 * - **Safe Mode (Production):** The SDK follows a "Defensive" strategy. It logs errors
 * via [cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback] and attempts to recover (e.g., by returning null or defaults)
 * to prevent application crashes.
 */
public data class StrictModePolicy(
    /**
     * If true, the JSON serializer throws exceptions on schema violations (e.g. receiving a Double for a Long).
     * If false, it attempts to truncate, ignore, or default the value.
     */
    val strictSerialization: Boolean = false,

//    /**
//     * Future proofing example:
//     * If true, throw exception on main thread DB access, etc.
//     */
//    val strictDatabaseAccess: Boolean = false
)
