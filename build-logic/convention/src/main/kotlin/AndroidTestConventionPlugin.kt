import com.android.build.gradle.TestExtension
import cz.kotox.crypto.sdk.configureDetekt
import cz.kotox.crypto.sdk.configureGradleManagedDevices
import cz.kotox.crypto.sdk.configureKotlinAndroid
import cz.kotox.crypto.sdk.configureSpotless
import cz.kotox.crypto.sdk.extensions.libs
import cz.kotox.crypto.sdk.extensions.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

@Suppress("unused")
class AndroidTestConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.test")
                apply("org.jetbrains.kotlin.android")
                apply("io.gitlab.arturbosch.detekt")
                apply("com.diffplug.spotless")
//                apply("org.jlleitschuh.gradle.ktlintJlleitschuh")
            }
            extensions.configure<KotlinAndroidProjectExtension> {
                configureKotlinAndroid(this)
            }

            extensions.configure<TestExtension> {
                defaultConfig.targetSdk = libs.version("targetSdk").toInt()
                configureGradleManagedDevices(this)
            }

            configureDetekt()
            configureSpotless()
        }
    }
}
