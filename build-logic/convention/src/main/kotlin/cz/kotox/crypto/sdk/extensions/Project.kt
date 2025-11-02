package cz.kotox.crypto.sdk.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File
import java.io.FileInputStream
import java.util.Properties

/**
 * Searches for a property with the specified [key] in the following order:
 * 1. A property in the `local.properties` file located in the root directory,
 * 2. A system environment variable,
 * 3. A property in Gradle properties (either project-level, or global in `.gradle/gradle.properties`).
 */
fun Project.getPropertyOrVariable(key: String): Provider<String> =
    provider { gradleLocalProperties(rootDir).getProperty(key) }
        .orElse(project.providers.environmentVariable(key))
        .orElse(project.providers.gradleProperty(key))

/**
 * Retrieve the project local properties if they are available.
 * If there is no local properties file then an empty set of properties is returned.
 */
private fun gradleLocalProperties(projectRootDir: File): Properties {
    val properties = Properties()
    val localPropertiesFile = File(projectRootDir, "local.properties")

    if (localPropertiesFile.exists()) {
        FileInputStream(localPropertiesFile).use { fileInput ->
            properties.load(fileInput)
        }
    }

    return properties
}

internal val Project.libs
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")


internal fun Project.kotlinOptions(block: KotlinCommonCompilerOptions.() -> Unit) {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions(block)
    }
}

internal val VersionCatalog.javaVersion
    get() = version("java")

internal val Project.isAndroid: Boolean
    get() = extensions.findByName("android") != null

internal fun Project.buildFreeCompilerArgs() =
    listOf(
        "-opt-in=kotlin.RequiresOptIn",
        // Enable experimental coroutines APIs, including Flow
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        // Enable kotlin experimental uuid api
        "-opt-in=kotlin.uuid.ExperimentalUuidApi",
        "-Xcontext-receivers",
        "-Xstring-concat=inline",
    )