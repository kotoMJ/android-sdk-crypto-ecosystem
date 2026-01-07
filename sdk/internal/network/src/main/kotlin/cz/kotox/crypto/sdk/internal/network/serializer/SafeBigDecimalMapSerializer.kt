package cz.kotox.crypto.sdk.internal.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

public object SafeBigDecimalMapSerializer : KSerializer<Map<String, BigDecimal>> {
    // We delegate to a standard Map serializer, but use our Safe serializer for values
    private val delegate = MapSerializer(String.serializer(), SafeBigDecimalSerializer("MapValue"))
    override val descriptor: SerialDescriptor = delegate.descriptor
    override fun serialize(encoder: Encoder, value: Map<String, BigDecimal>) = delegate.serialize(encoder, value)
    override fun deserialize(decoder: Decoder): Map<String, BigDecimal> = delegate.deserialize(decoder)
}
