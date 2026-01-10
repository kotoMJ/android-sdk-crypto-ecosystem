package cz.kotox.crypto.sdk

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.Properties

internal fun Project.configureSigning(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    Signing.initialize(this)

    commonExtension.apply {
        signingConfigs {
            create("release") {
                // Configure release signing only if the secrets are available
                // This allows to build debug app without the need for having release keystore.
                if (Signing.releaseSigningAvailable()) {
                    val signingValues: SigningValues = Signing.signingValues()
                    storeFile = Signing.signingFile()
                    storePassword = signingValues.storePassword
                    keyAlias = signingValues.keyAlias
                    keyPassword = signingValues.keyPassword
                }
            }
        }

    }
}

internal object Signing {
    private const val release_keystore: String = "extras/keystores/app/upload.keystore"

    fun initialize(project: Project) {
        projectRootDir = project.rootDir.toString()
    }

    private lateinit var projectRootDir: String

    fun signingFile(): File =
        File("$projectRootDir/$release_keystore")

    fun releaseSigningAvailable(): Boolean {
        return signingFile().exists()
    }

    fun signingValues(): SigningValues {
        return SigningValues(
            storePassword = getLocalPropertyValue("app.keystore.store.password") ?: System.getenv("APP_KOTOX_CRYPTO_KEYSTORE_PASSWORD"),
            keyAlias = getLocalPropertyValue("app.keystore.key.alias") ?: System.getenv("APP_KOTOX_CRYPTO_KEY_ALIAS"),
            keyPassword = getLocalPropertyValue("app.keystore.key.password") ?: System.getenv("APP_KOTOX_CRYPTO_KEY_PASSWORD"),
        )
    }

    fun getLocalPropertyValue(
        key: String,
        file: String = "local.properties",
    ): String? {
        try {
            val prop =
                Properties().apply {
                    load(FileInputStream(File(projectRootDir, file)))
                }
            val propertyValue = prop.getProperty(key)
            println("Reading local property $key: $propertyValue")
            return propertyValue
        } catch (t: Throwable) {
            println("Unable to locate property $key in local file $file")
            return null
        }
    }
}

internal data class SigningValues(
    val storePassword: String,
    val keyAlias: String,
    val keyPassword: String,
)


