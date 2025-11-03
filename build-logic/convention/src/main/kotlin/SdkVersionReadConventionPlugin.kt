import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.GradleException
import java.util.Properties
import java.io.FileInputStream

/**
 * Configures an SDK module's version.
 *
 * This plugin performs one main action:
 * 1. Reads the module's `version.properties` file (managed by
 * `SdkRootVersionManagerPlugin`) to find the version.
 * 2. Sets the `project.version`.
 *
 * This is used by the BOM to correctly read the module's version.
 * It must be applied to **each** SDK module.
 */
class SdkVersionReadConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // 1. Locate the version file
            val versionFile = project.file("version.properties")
            if (!versionFile.exists()) {
                throw GradleException(
                    "Module ${project.path} is missing a `version.properties` file. " +
                        "Run `./gradlew updateModuleVersions` from the root project to create one."
                )
            }

            // 2. Load the properties
            val versionProps = Properties()
            FileInputStream(versionFile).use { versionProps.load(it) }

            // 3. Get the version
            val moduleVersion = versionProps.getProperty("sdk.version")
                ?: throw GradleException(
                    "version.properties in ${project.path} must contain 'sdk.version'"
                )

            // 4. Apply to the project
            project.version = moduleVersion

            logger.lifecycle("[Version] Configured ${project.path} with version $moduleVersion")
        }
    }
}

