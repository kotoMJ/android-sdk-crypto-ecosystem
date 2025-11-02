import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import cz.kotox.crypto.sdk.configureAndroid
import cz.kotox.crypto.sdk.configureAndroidCompose
import cz.kotox.crypto.sdk.configureDetekt
import cz.kotox.crypto.sdk.configureGradleManagedDevices
import cz.kotox.crypto.sdk.configureKotlinAndroid
import cz.kotox.crypto.sdk.configureKotlinCompose
import cz.kotox.crypto.sdk.configureMobileCompose
import cz.kotox.crypto.sdk.configureSigning
import cz.kotox.crypto.sdk.configureSpotless
import cz.kotox.crypto.sdk.extensions.libs
import cz.kotox.crypto.sdk.extensions.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

@Suppress("unused")
class SdkAndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureBasePlugins()
            configureBaseAppModule()
            configureDetekt()
            configureSpotless()
        }
    }

    private fun Project.configureBaseAppModule() {

        extensions.configure<BaseAppModuleExtension> {
            configureAndroid(this)
            defaultConfig.targetSdk = libs.version("targetSdk").toInt()
            configureGradleManagedDevices(this)
            configureAndroidCompose(this)
            configureMobileCompose(this)
            configureSigning(this)
        }

        extensions.configure<KotlinAndroidProjectExtension> {
            configureKotlinCompose(this)
            configureKotlinAndroid(this)
        }
    }

    private fun Project.configureBasePlugins() {
        pluginManager.apply {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.plugin.compose")
            apply("org.jetbrains.kotlin.android")
            apply("io.gitlab.arturbosch.detekt")
            apply("com.diffplug.spotless")
            apply("kotlin-parcelize")
        }
    }
}
