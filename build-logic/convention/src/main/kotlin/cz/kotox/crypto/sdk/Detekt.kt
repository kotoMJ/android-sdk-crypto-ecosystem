package cz.kotox.crypto.sdk

// IMPORTS YOU WERE MISSING (from Detekt.kt)
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
// ---------------------------------------------
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType // <-- Also make sure you have this

/**
 * Configures Detekt with default rules AND Jetpack Compose rules.
 */
internal fun Project.configureDetektCompose() {
    configureDetektInternal(
        extrasFile("detekt.yml"),
        extrasFile("detekt_compose.yml"),
        addComposeRules = true
    )
}

/**
 * Configures Detekt with default rules only.
 */
internal fun Project.configureDetekt() {
    configureDetektInternal(
        extrasFile("detekt.yml"),
        addComposeRules = false
    )
}

private fun Project.extrasFile(fileName: String) =
    "${rootDir.absolutePath}/extras/$fileName"

/**
 * Internal function to configure Detekt extensions and tasks.
 */
private fun Project.configureDetektInternal(
    vararg configPaths: String,
    addComposeRules: Boolean = false
) {
    pluginManager.withPlugin("io.gitlab.arturbosch.detekt") {

        // 1. Configure the DetektExtension
        configure<DetektExtension> {
            buildUponDefaultConfig = true
            config.from(*configPaths)
            parallel = true
        }

        // 2. Conditionally add Compose dependencies
        if (addComposeRules) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("detektPlugins", libs.findLibrary("detektRules.compose").get())
            }
        }

        // 3. Configure the Detekt analysis task
        tasks.withType<Detekt>().configureEach {
            exclude("**/generated/**")

            reports {
                html.required.set(true)
                xml.required.set(true)
                txt.required.set(false)
                sarif.required.set(false)
            }
        }

        // 4. Configure the Baseline task - ready for ./gradlew detektBaseline
        tasks.withType<DetektCreateBaselineTask>().configureEach {
            exclude("**/generated/**")
        }
    }
}