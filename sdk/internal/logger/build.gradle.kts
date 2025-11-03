plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    alias(libs.plugins.buildLogic.sdk.version.read)
    `maven-publish`
}

val publishingName = "logger"
val singleVariantName = "release"

android {
    namespace = "cz.kotox.crypto.sdk.internal.logger"
    group = "cz.kotox.crypto.sdk.internal"

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
