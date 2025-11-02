package cz.kotox.crypto.sdk

import com.android.build.api.dsl.CommonExtension
import cz.kotox.crypto.sdk.extensions.bundle
import cz.kotox.crypto.sdk.extensions.library
import cz.kotox.crypto.sdk.extensions.libs
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureAndroidCompose(
    extension: CommonExtension<*, *, *, *, *, *>,
) {
    extension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.library("androidx-compose-bom")
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))

            add("implementation", libs.bundle("compose"))
            add("implementation", libs.library("androidx.lifecycle.runtimeCompose"))

            add("implementation", libs.findLibrary("kotlinx.collections.immutable").get())

            add("androidTestImplementation", libs.library("androidx.compose.ui.test.junit4"))
        }
    }
}

/**
 * Configure Material3
 */
internal fun Project.configureMobileCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        dependencies {
            add("implementation", libs.findLibrary("androidx.compose.material3").get())
        }
    }
}

/**
 * Configure Material Wear
 */
internal fun Project.configureWearCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        dependencies {
            add("implementation", libs.findLibrary("androidx-wear-compose-material").get())
            add("implementation", libs.findLibrary("androidx-wear-compose-foundation").get())
            add("implementation", libs.findLibrary("androidx-wear-tooling-preview").get())
        }
    }
}


internal fun Project.buildComposeMetricsParameters(): List<String> {
    val metricParameters = mutableListOf<String>()
    val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
    val enableMetrics = (enableMetricsProvider.orNull == "true")
    if (enableMetrics) {
        val metricsFolder = project.layout.buildDirectory.dir("compose-metrics").get().asFile
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath,
        )
    }

    val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
    val enableReports = (enableReportsProvider.orNull == "true")
    if (enableReports) {
        val reportsFolder = project.layout.buildDirectory.dir("compose-reports").get().asFile
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath,
        )
    }
    return metricParameters.toList()
}