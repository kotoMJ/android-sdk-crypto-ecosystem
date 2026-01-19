package cz.kotox.sdk.crypto.app.utils.security

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

class CryptoManager(context: Context) {
    init {
        AeadConfig.register() // Register AEAD primitives
    }

    private val keysetHandle = AndroidKeysetManager.Builder()
        .withSharedPref(context, "sentry_keyset", "sentry_prefs")
        .withKeyTemplate(KeyTemplates.get("AES256_GCM")) // Standard for 2026
        .withMasterKeyUri("android-keystore://sentry_master_key")
        .build()
        .keysetHandle

    // The Aead primitive provides Encrypt/Decrypt methods
    val aead: Aead = keysetHandle.getPrimitive(Aead::class.java)
}
