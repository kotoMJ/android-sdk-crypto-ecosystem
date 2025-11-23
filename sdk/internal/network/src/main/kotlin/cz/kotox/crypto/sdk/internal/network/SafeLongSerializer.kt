package cz.kotox.crypto.sdk.internal.network

import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
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

// ==========================================
// Serializer for Nullable Longs (Long?)
// ==========================================
internal class SafeNullableLongSerializer(
    private val logger: SDKLogger,
    private val strictMode: Boolean,
) : KSerializer<Long?> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SafeNullableLong", PrimitiveKind.LONG).nullable

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Long?) {
        if (value != null) encoder.encodeLong(value) else encoder.encodeNull()
    }

    override fun deserialize(decoder: Decoder): Long? {
        val jsonDecoder = decoder as? JsonDecoder ?: error("Json only")
        val element = jsonDecoder.decodeJsonElement()

        // Delegates to shared logic
        return parseSafeLong(element, logger, strictMode, descriptor.serialName)
    }
}

// ==========================================
// Serializer for Non-Null Longs (Long)
// ==========================================
internal class SafeLongSerializer(
    private val logger: SDKLogger,
    private val strictMode: Boolean,
) : KSerializer<Long> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SafeLong", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeLong(value)
    }

    override fun deserialize(decoder: Decoder): Long {
        val jsonDecoder = decoder as? JsonDecoder ?: error("Json only")
        val element = jsonDecoder.decodeJsonElement()

        // Delegates to shared logic
        val result = parseSafeLong(element, logger, strictMode, descriptor.serialName)

        // FAIL-SAFE LOGIC FOR NON-NULL FIELDS
        if (result == null) {
            // Logic: If strict mode, we would have already thrown inside parseSafeLong (mostly).
            // But if the JSON was explicit "null", parseSafeLong returns null without throwing.
            // For a non-nullable field, receiving "null" is a crash in strict mode.
            if (strictMode) {
                throw SerializationException("Strict Mode: Non-nullable field '${descriptor.serialName}' received null.")
            }

            // In PROD: Return default value 0L instead of crashing
            logger.log(LogPriority.WARN, null) { "⚠️ Field '${descriptor.serialName}' is Non-Null but received null/garbage. Defaulting to 0L." }
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
        if (strictMode) {
            throw SerializationException("Strict Mode: Field '$fieldName' received Double ($doubleVal) but expected Long.")
        }
        val truncated = doubleVal.toLong()
        logger.log(LogPriority.ERROR, null) { "⚠️ Mapping: Truncating $doubleVal to $truncated for $fieldName" }
        return truncated
    }

    // D. Garbage Data
    if (strictMode) {
        throw SerializationException("Strict Mode: Field '$fieldName' received invalid value '${primitive.content}'.")
    }
    logger.log(LogPriority.ERROR, null) { "⚠️ SafeLongSerializer: Failed to parse '${primitive.content}' for $fieldName." }
    return null
}
