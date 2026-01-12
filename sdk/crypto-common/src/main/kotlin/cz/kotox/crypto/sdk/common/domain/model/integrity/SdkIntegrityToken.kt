package cz.kotox.crypto.sdk.common.domain.model.integrity

@JvmInline
public value class SdkIntegrityToken(public val value: String) {
    init {
        require(value.isNotBlank()) { "Integrity token cannot be blank" }
    }
}
