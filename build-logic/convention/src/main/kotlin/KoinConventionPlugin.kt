import cz.kotox.crypto.sdk.extensions.library
import cz.kotox.crypto.sdk.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

@Suppress("unused")
class KoinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<KotlinAndroidProjectExtension>() {

                dependencies {
                    add("implementation", libs.library("koin.core"))
                    add("implementation", libs.library("koin.core.viewmodel"))

                    add("implementation", libs.library("koin.android"))

                    add("implementation", libs.library("koin.compose"))
                    add("implementation", libs.library("koin.composeVM"))

                    add("implementation", libs.library("koin.annotations"))
                    add("ksp", libs.library("koin.compiler"))
                }

            }
        }
    }
}
