package cz.kotox.sdk.crypto.app.utils.security

import kotlinx.serialization.Serializable

/**
 * Typed container for Sentry monitoring configuration.
 *
 * @property dsn - The dynamic ingestion key from your BFF
 * @property installId - The persistent UUID for "Full Stack" tracking
 * @property isMonitoringEnabled - Optional: kill-switch for privacy
 */
@Serializable
data class SentryConfig(
    val dsn: String? = null,
    val installId: String = "",
    val isMonitoringEnabled: Boolean = true,
)
