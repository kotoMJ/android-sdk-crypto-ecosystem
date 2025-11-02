plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    `maven-publish`
}

val publishingName = "logger"
val singleVariantName = "release"

android {
    namespace = "cz.kotox.crypto.sdk.internal.logger"
    group = "cz.kotox.crypto.sdk.internal"
    version = "0.0.3"

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
