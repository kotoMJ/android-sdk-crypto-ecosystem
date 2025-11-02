package cz.kotox.crypto.sdk

import cz.kotox.crypto.sdk.extensions.buildFreeCompilerArgs
import cz.kotox.crypto.sdk.extensions.bundle
import cz.kotox.crypto.sdk.extensions.javaVersion
import cz.kotox.crypto.sdk.extensions.library
import cz.kotox.crypto.sdk.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(
    extension: KotlinAndroidProjectExtension,
) {
    extension.apply {
        jvmToolchain(libs.javaVersion.toInt())
        compilerOptions {
            allWarningsAsErrors.set(properties["warningsAsErrors"] as? Boolean ?: false)
            jvmTarget.set(JvmTarget.fromTarget(libs.javaVersion))
            freeCompilerArgs.addAll(buildFreeCompilerArgs())
        }
    }

    dependencies {
        add("coreLibraryDesugaring", libs.library("android.desugarJdkLibs"))
        add("implementation", libs.bundle("kotlin-android"))
    }
}
