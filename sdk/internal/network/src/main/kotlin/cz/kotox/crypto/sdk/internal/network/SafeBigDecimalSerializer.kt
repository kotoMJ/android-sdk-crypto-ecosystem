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
import kotlinx.serialization.json.jsonPrimitive
import java.math.BigDecimal

/**
 * A custom safe serializer for **nullable** BigDecimal fields (`BigDecimal?`).
 *
 * **Purpose:**
 * Handles "dirty" API data where a price/percentage might arrive as a standard Number,
 * a String ("123.45"), or garbage (e.g. empty string "").
 *
 * **Behavior:**
 * - **Valid Number/String:** Returns the BigDecimal.
 * - **Explicit Null:** Returns `null`.
 * - **Garbage/Invalid Format:**
 * - **Strict Mode:** Throws [SerializationException].
 * - **Relaxed Mode (Prod):** Logs an error and returns `null`.
 */
public open class SafeNullableBigDecimalSerializer(
    private val debugFieldName: String,
) : KSerializer<BigDecimal?> {

    private val logger = SDKLogger.getLogger(MODULE_IDENTIFIER)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SafeNullableBigDecimal", PrimitiveKind.STRING).nullable

    override fun serialize(encoder: Encoder, value: BigDecimal?) {
        if (value != null) encoder.encodeString(value.toString()) else encoder.encodeNull()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): BigDecimal? {
        val jsonDecoder = decoder as? JsonDecoder ?: error("Json only")
        val element = jsonDecoder.decodeJsonElement()
        val isStrict = decoder.serializersModule.getContextual(StrictSerializationMarker::class) != null

        return parseSafeBigDecimal(
            element = element,
            logger = logger,
            strictMode = isStrict,
            fieldName = debugFieldName,
        )
    }
}

/**
 * A custom safe serializer for **non-nullable** BigDecimal fields (`BigDecimal`).
 *
 * **Behavior:**
 * - **Valid Number/String:** Returns the value.
 * - **Null / Missing / Garbage:**
 * - **Strict Mode:** Throws [SerializationException].
 * - **Relaxed Mode:** Logs a warning and **returns `BigDecimal.ZERO`**.
 */
public open class SafeBigDecimalSerializer(
    private val debugFieldName: String,
) : KSerializer<BigDecimal> {

    private val logger = SDKLogger.getLogger(MODULE_IDENTIFIER)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SafeBigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toString())
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): BigDecimal {
        val jsonDecoder = decoder as? JsonDecoder ?: error("Json only")
        val element = jsonDecoder.decodeJsonElement()
        val isStrict = decoder.serializersModule.getContextual(StrictSerializationMarker::class) != null

        val result = parseSafeBigDecimal(
            element = element,
            logger = logger,
            strictMode = isStrict,
            fieldName = debugFieldName,
        )

        // FAIL-SAFE LOGIC FOR NON-NULL FIELDS
        if (result == null) {
            if (isStrict) {
                throw SerializationException("Strict Mode: Non-nullable field '$debugFieldName' received null/garbage.")
            }
            logger.log(LogPriority.WARN, null) { "⚠️ Field '$debugFieldName' is Non-Null but received null/garbage. Defaulting to ZERO." }
            return BigDecimal.ZERO
        }

        return result
    }
}

// ==========================================
// Shared Logic
// ==========================================
private fun parseSafeBigDecimal(
    element: JsonElement,
    logger: SDKLogger,
    strictMode: Boolean,
    fieldName: String,
): BigDecimal? {
    // A. Handle Explicit Nulls
    if (element is JsonNull) {
        return null
    }

    val content = element.jsonPrimitive.content

    // B. Handle Empty Strings (Common API issue)
    if (content.isBlank()) {
        if (strictMode) throw SerializationException("Strict Mode: Field '$fieldName' is empty.")
        return null
    }

    // C. Try Parsing
    return try {
        // BigDecimal(String) constructor handles integer strings, float strings, and scientific notation
        BigDecimal(content)
    } catch (e: NumberFormatException) {
        // D. Garbage Data (e.g. "N/A", "Infinity")
        logger.log(LogPriority.ERROR, e) { "⚠️ SafeBigDecimalSerializer: Failed to parse '$content' for $fieldName." }

        if (strictMode) {
            throw SerializationException("Strict Mode: Field '$fieldName' has invalid format '$content'.", e)
        }
        null
    }
}
