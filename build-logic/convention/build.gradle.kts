import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "cz.kotox.crypto.sdk.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    compileOnly(libs.dokka.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidTest") {
            id = "kotox.crypto.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }

        register("androidRoom") {
            id = "kotox.crypto.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }

        register("koin") {
            id = "kotox.crypto.koin"
            implementationClass = "KoinConventionPlugin"
        }

        register("kotlinLibrary") {
            id = "kotox.crypto.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }

        register("sdkAndroidLibrary") {
            id = "kotox.crypto.sdk.android.library"
            implementationClass = "SdkAndroidLibraryConventionPlugin"
        }

        register("sdkAndroidUiLibrary") {
            id = "kotox.crypto.sdk.android.ui.library"
            implementationClass = "SdkAndroidUiLibraryConventionPlugin"
        }

        register("sdkAndroidApplication") {
            id = "kotox.crypto.sdk.android.application"
            implementationClass = "SdkAndroidApplicationConventionPlugin"
        }

        register("androidKtor") {
            id = "kotox.crypto.sdk.kmp.ktor"
            implementationClass = "AndroidKtorConventionPlugin"
        }

        register("dokka") {
            id = "kotox.crypto.sdk.dokka"
            implementationClass = "DokkaConventionPlugin"
        }

        register("sdkVersionRead") {
            id = "kotox.crypto.sdk.version.read"
            implementationClass = "SdkVersionReadConventionPlugin"
        }

        register("sdkVersionUpdate") {
            id = "kotox.crypto.sdk.version.update"
            implementationClass = "SdkVersionUpdateConventionPlugin"
        }
    }
}
