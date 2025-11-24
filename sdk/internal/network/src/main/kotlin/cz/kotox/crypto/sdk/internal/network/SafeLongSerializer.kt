package cz.kotox.crypto.sdk.internal.network

import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.network.utils.MODULE_IDENTIFIER
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

// /**
// * Internal configuration holder allows tests to override strict mode.
// */
// internal object SerializationConfig {
//    // Default to BuildConfig.DEBUG, but mutable for tests
//    var isStrictMode: Boolean = BuildConfig.DEBUG
// }

/**
 * Default singleton for general use where you don't need a specific log name.
 */
public object SafeNullableLongSerializerDefault : SafeNullableLongSerializer("SafeNullableLong")

/**
 * A custom safe serializer for **nullable** Long fields (`Long?`).
 *
 * **Purpose:**
 * Handles "dirty" API data where a field expected to be a `Long` might arrive as a `Double` (e.g. `31.38`)
 * or invalid garbage.
 *
 * **Behavior:**
 * - **Valid Long (e.g., `123`):** Returns the value.
 * - **Explicit Null:** Returns `null`.
 * - **Double (e.g., `31.38`):**
 * - **Strict Mode (Debug):** Throws [SerializationException].
 * - **Relaxed Mode (Prod):** Logs a warning and truncates to `Long` (returns `31L`).
 * - **Garbage/Invalid:**
 * - **Strict Mode (Debug):** Throws [SerializationException].
 * - **Relaxed Mode (Prod):** Logs an error and returns `null`.
 *
 * **Usage:**
 * Apply to nullable fields:
 * ```
 * @Serializable(with = SafeNullableLongSerializer::class)
 * val value: Long?
 * ``
 */
// TODO MJ - think about way how to avoid observability(public) those serializers by the sdk client.
public open class SafeNullableLongSerializer(
    private val debugFieldName: String,
) : KSerializer<Long?> {

    val logger = SDKLogger.getLogger(MODULE_IDENTIFIER)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SafeNullableLong", PrimitiveKind.LONG).nullable

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Long?) {
        if (value != null) encoder.encodeLong(value) else encoder.encodeNull()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Long? {
        val jsonDecoder = decoder as? JsonDecoder ?: error("Json only")
        val element = jsonDecoder.decodeJsonElement()
        val isStrict = decoder.serializersModule.getContextual(StrictModeMarker::class) != null

        // Delegates to shared logic
        return parseSafeLong(
            element = element,
            logger = logger,
            strictMode = isStrict,
            serialName = descriptor.serialName,
            fieldName = debugFieldName,
        )
    }
}

/**
 * Default singleton for general use.
 */
public object SafeLongSerializerDefault : SafeLongSerializer("SafeLong")

/**
 * A custom safe serializer for **non-nullable** Long fields (`Long`).
 *
 * **Purpose:**
 * Ensures a valid `Long` is always returned, even if the API sends `null`, garbage, or a `Double`.
 * Prevents application crashes in production when mandatory fields are missing or malformed.
 *
 * **Behavior:**
 * - **Valid Long:** Returns the value.
 * - **Double (e.g., `31.38`):**
 * - **Strict Mode:** Throws [SerializationException].
 * - **Relaxed Mode:** Truncates to `31L`.
 * - **Null / Missing / Garbage:**
 * - **Strict Mode:** Throws [SerializationException] (Enforces Non-Null contract).
 * - **Relaxed Mode:** Logs a warning and **returns `0L`** (Default value).
 *
 * **Usage:**
 * Apply to non-null fields where `0` is an acceptable fallback:
 * ```
 * @Serializable(with = SafeLongSerializer::class)
 * val value: Long
 * ```
 */
// TODO MJ - think about way how to avoid observability(public) those serializers by the sdk client.
public open class SafeLongSerializer(
    private val debugFieldName: String,
) : KSerializer<Long> {

    val logger = SDKLogger.getLogger(MODULE_IDENTIFIER)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SafeLong", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeLong(value)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Long {
        val jsonDecoder = decoder as? JsonDecoder ?: error("Json only")
        val element = jsonDecoder.decodeJsonElement()

        // CHECK CONTEXT: "Is the StrictModeMarker registered?"
        val isStrict = decoder.serializersModule.getContextual(StrictModeMarker::class) != null

        // Delegates to shared logic
        val result = parseSafeLong(
            element = element,
            logger = logger,
            strictMode = isStrict,
            serialName = descriptor.serialName,
            fieldName = debugFieldName,
        )

        // FAIL-SAFE LOGIC FOR NON-NULL FIELDS
        if (result == null) {
            // Logic: If strict mode, we would have already thrown inside parseSafeLong (mostly).
            // But if the JSON was explicit "null", parseSafeLong returns null without throwing.
            // For a non-nullable field, receiving "null" is a crash in strict mode.
            if (isStrict) {
                throw SerializationException("Strict Mode: Non-nullable field '${descriptor.serialName}' received null.")
            }

            // In PROD: Return default value 0L instead of crashing
            logger.log(LogPriority.WARN, null) { "⚠️ Field '${descriptor.serialName}' is Non-Null but received null/garbage. Defaulting to 0L." }
            println("⚠️ Field '${descriptor.serialName}' is Non-Null but received null/garbage. Defaulting to 0L.")
            return 0L
        }

        return result
    }
}

// ==========================================
// 1. Shared Logic (Private Helper)
// ==========================================
private fun parseSafeLong(
    element: JsonElement,
    logger: SDKLogger,
    strictMode: Boolean,
    serialName: String,
    fieldName: String,
): Long? {
    // A. Handle Explicit Nulls
    if (element is JsonNull) {
        return null
    }

    val primitive = element.jsonPrimitive

    // B. Happy Path (Standard Long)
    val longVal = primitive.longOrNull

    if (longVal != null) return longVal

    // C. Handle Double Mismatch (31.38 -> 31)
    val doubleVal = primitive.doubleOrNull

    if (doubleVal != null) {
        logger.log(LogPriority.ERROR, null) { "⚠️ Double instead of Long Mismatch in field [$fieldName] with double value [$doubleVal] " }
        if (strictMode) {
            logger.log(LogPriority.ERROR, null) { "⚠️ Strict mode (mismatch to failure) enabled!" }
            throw SerializationException("Strict Mode: Field '$fieldName' received Double ($doubleVal) but expected Long.")
        }
        val truncated = doubleVal.toLong()
        logger.log(LogPriority.INFO, null) { "⚠️ Mapping: Truncating $doubleVal to $truncated for $serialName" }
        return truncated
    }

    // D. Garbage Data
    if (strictMode) {
        throw SerializationException("Strict Mode: Field '$fieldName' received invalid value '${primitive.content}'.")
    }
    logger.log(LogPriority.ERROR, null) { "⚠️ SafeLongSerializer: Failed to parse '${primitive.content}' for $fieldName." }
    return null
}
