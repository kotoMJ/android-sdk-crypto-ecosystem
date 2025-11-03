import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.GradleException
import java.security.MessageDigest
import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Manages SDK module versions from the root project.
 *
 * This plugin provides the `updateModuleVersions` task, which scans all
 * SDK subprojects, checks for content changes in `src/main`, and
 * automatically increments the `PATCH` version in the module's
 * `version.properties` file if a change is detected.
 *
 * It must be applied to the **root** project.
 */
class SdkVersionUpdateConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        // This plugin must be applied to the root project
        if (target != target.rootProject) {
            throw GradleException(
                "SdkRootVersionManagerPlugin must be applied to the root project."
            )
        }

        with(target) {
            tasks.register("updateModuleVersions") {
                group = "versioning"
                description = "Checks all SDK modules for content changes and bumps patch version if needed."

                // This task does not have inputs or outputs in the traditional sense,
                // as it modifies source files (version.properties).
                // It should always run when explicitly called.
                outputs.upToDateWhen { false }

                doLast {
                    // Find all modules this task should operate on
                    val sdkModules = subprojects.filter {
                        it.path.startsWith(":sdk:") && it.path != ":sdk:bom"
                    }

                    if (sdkModules.isEmpty()) {
                        logger.warn("[VersionBump] No SDK modules found (e.g., projects starting with ':sdk:').")
                    }

                    sdkModules.forEach { module ->
                        // Run the hashing and bumping logic for each module
                        checkAndBumpVersion(module)
                    }
                }
            }
        }
    }

    /**
     * Calculates the content hash for a module's `src/main` directory
     * and bumps the version in `version.properties` if the hash has changed.
     */
    private fun checkAndBumpVersion(module: Project) {
        val versionFile = module.file("version.properties")
        val srcDir = module.file("src/main")

        if (!srcDir.exists()) {
            module.logger.warn("[VersionBump] SKIPPING: ${module.path} (no 'src/main' directory found)")
            return
        }

        // 1. Calculate the current content hash
        val md = MessageDigest.getInstance("SHA-256")
        module.fileTree(srcDir).filter { it.isFile }.forEach { file ->
            // Normalize line endings to ensure consistent hashing across OS (Windows/Mac/Linux)
            val text = file.readText().replace("\r\n", "\n")
            md.update(text.toByteArray())
        }
        val currentHash = md.digest().joinToString("") { "%02x".format(it) }

        // 2. Load current version.properties or create if missing
        val props = Properties()
        val (currentVersion, lastHash) = if (versionFile.exists()) {
            FileInputStream(versionFile).use { props.load(it) }
            props.getProperty("sdk.version") to props.getProperty("sdk.content.hash")
        } else {
            module.logger.lifecycle("[VersionBump] CREATING: ${versionFile.path}")
            "0.0.0" to "none" // Default for a brand new module
        }

        if (currentVersion == null) {
            module.logger.error("[VersionBump] ERROR: ${versionFile.path} is missing 'sdk.version'")
            return
        }

        // 3. Compare and Bump!
        if (currentHash != lastHash) {
            module.logger.lifecycle("[VersionBump] CHANGE DETECTED: ${module.path}")

            val (major, minor, patch) = try {
                currentVersion.split(".").map { it.toInt() }
            } catch (e: Exception) {
                module.logger.error("[VersionBump] ERROR: Invalid version format in ${module.path}: '$currentVersion'")
                return
            }

            val newVersion = "$major.$minor.${patch + 1}"

            module.logger.lifecycle("  BUMPING: $currentVersion -> $newVersion")

            // 4. Write new version and hash back to the file
            props.setProperty("sdk.version", newVersion)
            props.setProperty("sdk.content.hash", currentHash)
            FileOutputStream(versionFile).use {
                props.store(it, "Version auto-bumped by updateModuleVersions task. DO NOT EDIT HASH MANUALLY.")
            }
        } else {
            module.logger.lifecycle("[VersionBump] NO CHANGE: ${module.path} (version $currentVersion)")
        }
    }
}