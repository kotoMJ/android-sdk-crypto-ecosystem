import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.util.Properties

//import org.gradle.kotlin.dsl.pluginsConfiguration

buildscript {
    dependencies {
        classpath(libs.dokka.versioning)
    }
}

plugins {
    alias(libs.plugins.dokka)
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    "dokka"(project(":sdk:crypto-common"))
    dokka(project(":sdk:crypto-coindata"))
    dokka(project(":sdk:crypto-news"))
    dokka(project(":sdk:crypto-tracker"))

    dokkaHtmlPlugin(libs.dokka.versioning)
}

//TODO MJ - do not copy paste those helper functions.
val sdkVersion = getLocalPropertyValue("sdk.bom.version")
    ?: getLatestBomVersion(versionTagPrefix = "SDK_BOM_") // YYYY.MM.DD

// Configure the final, aggregated website
dokka {
    //moduleName.set("CRYPTO SDK")

    dokkaPublications.configureEach {
        // Add the root README.md from the SDK folder
        includes.from(rootProject.file("doc.md"))
    }

//    tasks.dokkaHtml {
//        pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
//            // Configure the versioning plugin by its type
//            version = sdkVersion
//            olderVersionsDir = layout.buildDirectory.dir("dokka/html").get().asFile
//            moduleName = "CRYPTO SDK"
//            renderVersionsNavigationOnAllPages = true
//            // You can also configure the HTML plugin (DokkaBase) here
//            // But the way you have it in `dokkaPublications.named("html")` is also correct
//        }
//    }

    // This configures the final, published website
    dokkaPublications.configureEach {
        // This is the main title for the entire site
        moduleName.set("CRYPTO SDK")

        // --- Versioning Configuration ---

        // 1. Set the output directory to include the version
        outputDirectory.set(layout.buildDirectory.dir("dokka/html/$sdkVersion"))

        version = sdkVersion


    }

    /**
     * FIXME MJ - finish setup of versioning plugin. https://github.com/Kotlin/dokka/tree/master/dokka-subprojects/plugin-versioning
     * The problem here is that all examples it's usage are in dokka V1, but we are using dokka V2 now.
     */

//    // 2. This configures the final "html" publication
//    dokkaPublications.named("html") {
//
//        // This is the main title for the entire site
//        moduleName.set("CRYPTO SDK")
//
//        // This sets the output directory for this specific version
//        outputDirectory.set(layout.buildDirectory.dir("dokka/html/$sdkVersion"))
//
//        version = sdkVersion
//
//    }
}

fun getLatestBomVersion(versionTagPrefix: String): String {
    ByteArrayOutputStream().use { outputStream ->
        project.exec {
            commandLine =
                "git describe --tags --abbrev=0 --match=$versionTagPrefix* HEAD".split(" ")
            standardOutput = outputStream
        }
        val bomVersion = outputStream.toString().trim().substringAfter(versionTagPrefix)
        println("Reading Git Tag based BOM version: $bomVersion")
        return bomVersion
    }
    // return "2024.07.18"
}

fun getLocalPropertyValue(
    key: String,
    file: String = "local.properties",
): String? {
    try {
        val prop =
            Properties().apply {
                load(FileInputStream(File(rootProject.rootDir, file)))
            }
        val propertyValue = prop.getProperty(key)
        println("Reading local property $key: $propertyValue")
        return propertyValue
    } catch (t: Throwable) {
        println("Unable to locate property $key in local file $file")
        return null
    }
}