package cz.kotox.crypto.sdk


import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinCompose(
    extension: KotlinAndroidProjectExtension,
) {
    extension.apply {
        compilerOptions {
            freeCompilerArgs.addAll(
                buildComposeMetricsParameters() +
                        listOf("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"),
            )
        }
    }
}