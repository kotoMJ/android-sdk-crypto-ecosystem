import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.androidx.baselineProfile) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.easyLauncher) apply false
    alias(libs.plugins.firebase.appDistribution) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.gms.googleServices) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktorfit) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.gradleVersions)
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    alias(libs.plugins.buildLogic.sdk.version.update)
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA")
            .any { version.uppercase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }

    // disallow release candidates as upgradable versions from stable versions
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

subprojects {
    tasks.withType<Test> {
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
    }
}
