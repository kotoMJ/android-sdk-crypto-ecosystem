import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import cz.kotox.crypto.sdk.configureAndroid
import cz.kotox.crypto.sdk.configureDetekt
import cz.kotox.crypto.sdk.configureKotlinAndroid
import cz.kotox.crypto.sdk.configureSpotless
import cz.kotox.crypto.sdk.disableUnnecessaryAndroidTests
import cz.kotox.crypto.sdk.extensions.library
import cz.kotox.crypto.sdk.extensions.libs
import cz.kotox.crypto.sdk.extensions.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

@Suppress("unused")
class SdkAndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("kotlin-parcelize")
                apply("org.jetbrains.kotlin.android")
                apply("io.gitlab.arturbosch.detekt")
                apply("com.diffplug.spotless")
            }


            extensions.configure<KotlinAndroidProjectExtension> {
                configureKotlinAndroid(this)
            }

            extensions.configure<LibraryExtension> {
                configureAndroid(this)
                defaultConfig.targetSdk = libs.version("targetSdk").toInt()
            }

            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }

            configureDetekt()
            configureSpotless()


            dependencies {
                //add("lintChecks", project(":lint"))

                add("testImplementation", libs.library("androidx.test.core"))
                add("testImplementation", libs.library("androidx.test.junit"))
                add("testImplementation", libs.library("junit"))

                add("testImplementation", libs.library("kotlin.test"))
                add("testImplementation", libs.library("kotlinx.coroutines.test"))
            }
        }
    }
}
