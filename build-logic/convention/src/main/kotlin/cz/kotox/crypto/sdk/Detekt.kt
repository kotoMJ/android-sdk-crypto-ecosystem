package cz.kotox.crypto.sdk

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureDetektCompose() {
    configureDetektInternal(
        extrasFile("detekt.yml"),
        extrasFile("detekt_compose.yml"),
    )
}

internal fun Project.configureDetekt() {
    configureDetektInternal(
        extrasFile("detekt.yml"),
    )
}

private fun Project.extrasFile(fileName: String) =
    "${rootDir.absolutePath}/extras/$fileName"

private fun Project.configureDetektInternal(
    vararg configPaths: String,
) {
    pluginManager.withPlugin("io.gitlab.arturbosch.detekt") {
        configure<DetektExtension> {
            buildUponDefaultConfig = true
            config.from(*configPaths)
            parallel = true
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        dependencies {
            add("detektPlugins", libs.findLibrary("detektRules.compose").get())
        }
    }
}

