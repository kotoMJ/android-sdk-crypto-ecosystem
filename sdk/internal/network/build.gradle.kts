import java.util.Properties

plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    alias(libs.plugins.buildLogic.android.ktor)
    `maven-publish`
    alias(libs.plugins.ksp)
}

val publishingName = "network"
val singleVariantName = "release"

val versionProps = Properties()
file("version.properties").inputStream().use { versionProps.load(it) }

android {
    namespace = "cz.kotox.crypto.sdk.internal.network"
    group = "cz.kotox.crypto.sdk.internal"
    version = versionProps.getProperty("sdk.version")

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
}

dependencies {
    implementation(projects.sdk.internal.common)
    implementation(projects.sdk.cryptoCommon)
    implementation(projects.sdk.internal.logger)

    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
}
