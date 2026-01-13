package cz.kotox.crypto.sdk.internal.integrity.domain

@JvmInline
public value class SdkIntegrityToken(public val value: String) {
    init {
        require(value.isNotBlank()) { "Integrity token cannot be blank" }
    }
}
