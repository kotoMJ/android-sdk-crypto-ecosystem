package cz.kotox.crypto.sdk.internal.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonPrimitive
import java.math.BigDecimal

/**
 * Serializer that reads a JSON number (or string) directly into [java.math.BigDecimal]
 * without passing through Double, preserving exact API precision.
 */
public object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): BigDecimal {
        // Read the value as a generic JSON element first
        val jsonInput = decoder as? JsonDecoder
            ?: error("Can be deserialized only by JSON")
        val jsonElement = jsonInput.decodeJsonElement()

        // .content gives the raw string (e.g., "1234.5678") even if it was a number in JSON
        val rawString = jsonElement.jsonPrimitive.content
        return BigDecimal(rawString)
    }

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        // Serialize back as a string (or you could strictly output number if needed)
        encoder.encodeString(value.toString())
    }
}
