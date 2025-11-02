package cz.kotox.crypto.sdk

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.get

internal fun Project.configureKotlinJvm(
    extension: JavaPluginExtension,
) {
    extension.apply {
        // Up to Java 11 APIs are available through desugaring
        // https://developer.android.com/studio/write/java11-minimal-support-table
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

        sourceSets["main"].java.srcDir("src/main/kotlin")

        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
}
