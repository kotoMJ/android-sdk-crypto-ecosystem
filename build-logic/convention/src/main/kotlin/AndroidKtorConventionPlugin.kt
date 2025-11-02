import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import cz.kotox.crypto.sdk.extensions.library
import cz.kotox.crypto.sdk.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

@Suppress("unused")
class AndroidKtorConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("de.jensklingenberg.ktorfit")
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            val androidPluginHandler = {
                dependencies {
                    add("implementation", libs.library("network.ktor.core"))
                    add("implementation", libs.library("network.ktor.cio"))
                    add("implementation", libs.library("network.ktor.logging"))
                    add("implementation", libs.library("network.ktor.negotiation"))
                    add("implementation", libs.library("network.ktor.serialization"))
                    add("implementation", libs.library("network.ktor.wss"))
                    add("implementation", libs.library("network.ktorfit.lib"))

                    add("ksp", libs.library("network.ktorfit.ksp"))

                    add("implementation", libs.library("kotlinx.serialization.json"))
                }
            }

            plugins.withType<LibraryPlugin> {
                androidPluginHandler()
            }
            plugins.withType<AppPlugin> {
                androidPluginHandler()
            }
        }
    }
}