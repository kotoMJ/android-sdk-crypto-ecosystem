package cz.kotox.crypto.sdk.internal.network

import kotlinx.serialization.Serializable

/**
 * This class is used purely as a configuration flag.
 * If this class is registered in the Json SerializersModule,
 * it means "Strict Mode" is active.
 */
@Serializable
internal class StrictModeMarker
