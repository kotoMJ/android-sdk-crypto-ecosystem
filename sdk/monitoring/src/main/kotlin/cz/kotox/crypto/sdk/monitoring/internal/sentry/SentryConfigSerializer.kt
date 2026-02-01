package cz.kotox.crypto.sdk.monitoring.internal.sentry

import androidx.datastore.core.Serializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class SentryConfigSerializer(private val crypto: SentryConfigCryptoManager) : Serializer<SentryConfig> {
    override val defaultValue: SentryConfig = SentryConfig()

    override suspend fun readFrom(input: InputStream): SentryConfig {
        return try {
            val encryptedData = input.readBytes()
            // Decrypt using the AEAD primitive
            val decryptedData = crypto.aead.decrypt(encryptedData, null)
            Json.decodeFromString(decryptedData.decodeToString())
        } catch (e: Exception) {
            defaultValue // Return default if file is corrupted or not yet created
        }
    }

    override suspend fun writeTo(t: SentryConfig, output: OutputStream) {
        val jsonString = Json.encodeToString(SentryConfig.serializer(), t)
        // Encrypt the JSON string before writing to disk
        val encryptedData = crypto.aead.encrypt(jsonString.toByteArray(), null)
        output.write(encryptedData)
    }
}
