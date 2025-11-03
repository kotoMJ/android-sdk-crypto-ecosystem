import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.GradleException
import java.io.File
import java.security.MessageDigest
import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets

/**
 * Manages SDK module versions from the root project.
 *
 * This plugin provides two tasks:
 * 1. `updateModuleVersions`: Scans all SDK modules, checks for content
 * changes in `src/main`, and automatically increments the `PATCH` version
 * in the module's `version.properties` file if a change is detected.
 * (For developers to run locally).
 *
 * 2. `checkModuleVersions`: Scans all SDK modules and fails the build
 * if any module has content changes that are not reflected in the
 * `sdk.content.hash` in `version.properties`.
 * (For CI to run).
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
                outputs.upToDateWhen { false } // Always run when called

                doLast {
                    val sdkModules = getSdkModules(this@with)
                    sdkModules.forEach { module ->
                        val hashInfo = getModuleHashInfo(module) ?: return@forEach

                        if (!hashInfo.isMatch) {
                            logger.lifecycle("[VersionBump] CHANGE DETECTED: ${module.path}")

                            val (major, minor, patch) = try {
                                hashInfo.currentVersion.split(".").map { it.toInt() }
                            } catch (e: Exception) {
                                logger.error("[VersionBump] ERROR: Invalid version format in ${module.path}: '${hashInfo.currentVersion}'")
                                return@forEach
                            }

                            val newVersion = "$major.$minor.${patch + 1}"

                            logger.lifecycle("  BUMPING: ${hashInfo.currentVersion} -> $newVersion")

                            // Write new version and hash back to the file
                            hashInfo.props.setProperty("sdk.version", newVersion)
                            hashInfo.props.setProperty("sdk.content.hash", hashInfo.currentHash)
                            FileOutputStream(hashInfo.versionFile).use {
                                hashInfo.props.store(it, "Version auto-bumped by updateModuleVersions task. DO NOT EDIT HASH MANUALLY.")
                            }
                        } else {
                            logger.lifecycle("[VersionBump] NO CHANGE: ${module.path} (version ${hashInfo.currentVersion})")
                        }
                    }
                }
            }

            tasks.register("checkModuleVersions") {
                group = "verification"
                description = "Fails the build if any module has un-bumped version changes."

                // This task is cacheable. It only depends on the version files
                // and the source files of the subprojects.
                inputs.files(getSdkModules(this@with).map { it.fileTree("src/main") })
                inputs.files(getSdkModules(this@with).map { it.file("version.properties") })

                doLast {
                    val failures = mutableListOf<String>()
                    val sdkModules = getSdkModules(this@with)

                    sdkModules.forEach { module ->
                        val hashInfo = getModuleHashInfo(module) ?: return@forEach
                        if (!hashInfo.isMatch) {
                            failures.add(
                                "  - ${module.path}: Code changed but version not bumped. " +
                                    "Please run './gradlew updateModuleVersions' and commit the result."
                            )
                        }
                    }

                    if (failures.isNotEmpty()) {
                        throw GradleException(
                            "Module version check failed:\n" + failures.joinToString("\n")
                        )
                    } else {
                        logger.lifecycle("Version check succeeded. All module versions are up-to-date.")
                    }
                }
            }
        }
    }

    /**
     * Finds the relevant SDK modules to check, excluding the BOM.
     */
    private fun getSdkModules(rootProject: Project): List<Project> {
        return rootProject.subprojects.filter {
            it.path.startsWith(":sdk:") && it.path != ":sdk:bom"
        }
    }

    /**
     * Data class to hold the result of a module hash check.
     */
    private data class ModuleHashInfo(
        val props: Properties,
        val versionFile: File,
        val currentVersion: String,
        val currentHash: String,
        val lastHash: String?,
        val isMatch: Boolean
    )

    /**
     * Calculates the content hash for a module's `src/main` directory
     * and compares it to the hash stored in `version.properties`.
     */
    private fun getModuleHashInfo(module: Project): ModuleHashInfo? {
        val versionFile = module.file("version.properties")
        val srcDir = module.file("src/main")

        if (!srcDir.exists()) {
            module.logger.warn("[VersionCheck] SKIPPING: ${module.path} (no 'src/main' directory found)")
            return null
        }

        // 1. Calculate the current content hash
        val md = MessageDigest.getInstance("SHA-256")


        // --- START OS INDEPENDENT FILE SYSTEM HASH COMPUTATION ---
        // Get all files, filter out system files, and SORT them to ensure
        // a deterministic order, regardless of the operating system.
        val filesToHash = module.fileTree(srcDir)
            .filter { it.isFile && it.name != ".DS_Store" }
            .sortedBy { it.path } // <-- THIS IS THE KEY FIX

        if (filesToHash.isEmpty() && module.path != ":sdk:bom") {
            module.logger.warn("[VersionCheck] SKIPPING: ${module.path} (no source files found in 'src/main')")
            return null
        }

        filesToHash.forEach { file ->
            // Read as UTF-8 and normalize line endings to LF (\n)
            // to ensure hash is the same on Windows, macOS, and Linux.
            val text = file.readText(StandardCharsets.UTF_8).replace("\r\n", "\n")
            md.update(text.toByteArray(StandardCharsets.UTF_8))
        }
        // --- END OS INDEPENDENT FILE SYSTEM HASH COMPUTATION ---

        val currentHash = md.digest().joinToString("") { "%02x".format(it) }

        // 2. Load current version.properties or create if missing
        val props = Properties()
        val (currentVersion, lastHash) = if (versionFile.exists()) {
            FileInputStream(versionFile).use { props.load(it) }
            props.getProperty("sdk.version") to props.getProperty("sdk.content.hash")
        } else {
            module.logger.warn("[VersionCheck] CREATING: ${versionFile.path}")
            "0.0.0" to "none" // Default for a brand new module
        }

        if (currentVersion == null) {
            throw GradleException("[VersionCheck] ERROR: ${versionFile.path} is missing 'sdk.version'")
        }

        return ModuleHashInfo(
            props = props,
            versionFile = versionFile,
            currentVersion = currentVersion,
            currentHash = currentHash,
            lastHash = lastHash,
            isMatch = (currentHash == lastHash)
        )
    }
}