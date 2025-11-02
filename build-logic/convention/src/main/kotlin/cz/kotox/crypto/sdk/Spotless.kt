package cz.kotox.crypto.sdk

import com.diffplug.gradle.spotless.SpotlessExtension
import cz.kotox.crypto.sdk.extensions.libs
import cz.kotox.crypto.sdk.extensions.version
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureSpotless() {
    pluginManager.withPlugin("com.diffplug.spotless") {
        configure<SpotlessExtension> {
            val ktlintVersion = libs.version("ktlint")
            kotlin {
                target("src/**/*.kt")
                targetExclude("**/build/**/*.kt")
                ktlint(ktlintVersion)
            }

            kotlinGradle {
                target("*.gradle.kts")
                targetExclude("**/build/**/*.kts")
                ktlint(ktlintVersion)
            }

            format("xml") {
                target("**/*.xml")
                targetExclude("**/build/**/*.xml")
            }
        }
    }
}
