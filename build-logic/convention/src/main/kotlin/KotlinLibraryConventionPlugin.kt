import cz.kotox.crypto.sdk.configureDetekt
import cz.kotox.crypto.sdk.configureKotlinJvm
import cz.kotox.crypto.sdk.configureSpotless
import cz.kotox.crypto.sdk.extensions.library
import cz.kotox.crypto.sdk.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class KotlinLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("java-library")
                apply("org.jetbrains.kotlin.jvm")
                apply("io.gitlab.arturbosch.detekt")
                apply("com.diffplug.spotless")
            }

            extensions.configure<JavaPluginExtension> {
                configureKotlinJvm(this)
            }

            configureDetekt()
            configureSpotless()

            dependencies {
                add("testImplementation", libs.library("junit"))

                add("testImplementation", libs.library("kotlin.test"))
                add("testImplementation", libs.library("kotlinx.coroutines.test"))
            }
        }
    }
}
