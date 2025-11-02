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
                    val signingValues = Signing.signingValues()
                    storeFile = rootProject.file("extras/keystores/release/upload.keystore")
                    storePassword = signingValues.storePassword
                    keyAlias = signingValues.keyAlias
                    keyPassword = signingValues.keyPassword
                }
            }
        }

    }
}

internal object Signing {
    private const val release_keystore_properties: String = "extras/keystores/release/upload.properties"
    private const val release_keystore_file: String = "extras/keystores/release/upload.keystore"

    fun initialize(project: Project) {
        projectRootDir = project.rootDir.toString()
    }

    private lateinit var projectRootDir: String

    fun releaseSigningAvailable(): Boolean {
        return File("$projectRootDir/$release_keystore_file").exists()
    }

    fun signingValues(): ReleaseValues {
        val properties = Properties()
        val keystoreProperties = release_keystore_properties
        val keystoreFile = release_keystore_file
        val storePropertiesPath = "$projectRootDir/$keystoreProperties"
        val storeFilePath = "$projectRootDir/$keystoreFile"

        properties.load(FileInputStream(File(storePropertiesPath)))

        return ReleaseValues(
            storeFilePath = storeFilePath,
            storePassword = properties.getProperty("keystore.store.password", "MISSING"),
            keyAlias = properties.getProperty("keystore.key.alias", "MISSING"),
            keyPassword = properties.getProperty("keystore.key.password", "MISSING"),
        )
    }
}

internal data class ReleaseValues(
    val storeFilePath: String,
    val storePassword: String,
    val keyAlias: String,
    val keyPassword: String,
)
