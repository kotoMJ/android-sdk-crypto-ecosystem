import java.util.Properties

plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    `maven-publish`
    alias(libs.plugins.ksp)
}

val publishingName = "common"
val singleVariantName = "release"

val versionProps = Properties()
file("version.properties").inputStream().use { versionProps.load(it) }

android {
    namespace = "cz.kotox.crypto.sdk.internal.common"
    group = "cz.kotox.crypto.sdk.internal"
    version = versionProps.getProperty("sdk.version")
    buildFeatures.buildConfig = true

    buildTypes {

        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    publishing {
        singleVariant(singleVariantName) {
            // withSourcesJar()
        }
    }

    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
    testImplementation(project(":sdk:internal:common"))
}
