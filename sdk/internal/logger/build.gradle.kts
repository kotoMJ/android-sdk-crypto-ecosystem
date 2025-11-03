import java.util.Properties

plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    `maven-publish`
}

val publishingName = "logger"
val singleVariantName = "release"

val versionProps = Properties()
file("version.properties").inputStream().use { versionProps.load(it) }

android {
    namespace = "cz.kotox.crypto.sdk.internal.logger"
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
    implementation(projects.sdk.cryptoCommon)
}
