import java.io.FileInputStream
import java.util.Properties

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// https://docs.gradle.org/current/userguide/declaring_repositories.html#sub:centralized-repository-declaration
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS


    //Use kotox github registry from kotox SDK repository

    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "CRYPTO-SDK"

include(
    ":sdk:bom",
    ":sdk:crypto-common",
    ":sdk:internal:common",
    ":sdk:internal:logger",
    ":sdk:internal:network",
    ":sdk:crypto-coindata",
    ":sdk:crypto-news",
    ":sdk:crypto-tracker",
    ":docs"
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":sdk:internal:logger")

/**
 * Quick-fix for: Unable to make progress running work.
 * There are items queued for execution but none of them can be started
 * https://github.com/gradle/gradle/issues/21325
 * https://github.com/gradle/gradle/issues/25747
 */
gradle.startParameter.excludedTaskNames.addAll(
    gradle.startParameter.taskNames.filter { it.contains("testClasses") },
)
