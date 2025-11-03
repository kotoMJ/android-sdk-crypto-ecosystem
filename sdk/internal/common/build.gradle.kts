plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    alias(libs.plugins.buildLogic.sdk.version.read)
    `maven-publish`
    alias(libs.plugins.ksp)
}

val publishingName = "common"
val singleVariantName = "release"

android {
    namespace = "cz.kotox.crypto.sdk.internal.common"
    group = "cz.kotox.crypto.sdk.internal"
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
