import cz.kotox.crypto.sdk.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension

class DokkaConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            val excludedPaths = setOf(
                ":sdk:bom",
                ":build-logic",
                ":docs"
            )

            if (project.path in excludedPaths || project.path.startsWith(":sdk:internal")) {
                println(">>> [DokkaPlugin] SKIPPING excluded module: ${project.path}")
                return
            }

            pluginManager.apply(libs.findPlugin("dokka").get().get().pluginId)

            // 3. Apply CHILD configuration
            extensions.configure<DokkaExtension> {
                print(">>> [DokkaPlugin] Configuring CHILD module: ${project.name}")

                moduleName.set(project.name.replaceFirstChar { it.titlecase() })

                dokkaSourceSets.named("main") {
                    val moduleDocFile = project.file("doc.md")
                    if (moduleDocFile.exists()) {
                        println(">>> [DokkaPlugin] ... found 'doc.md' in ${project.name}")
                        includes.from(moduleDocFile)
                    }else{
                        println(">>> [DokkaPlugin] ... NOT found 'doc.md' in ${project.name}")
                    }

                    skipEmptyPackages.set(false)
                    // Add your sourceLink block here
                }
            }
        }
    }
}
