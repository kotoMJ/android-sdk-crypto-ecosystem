plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    alias(libs.plugins.buildLogic.sdk.version.read)
    alias(libs.plugins.buildLogic.android.ktor)
    alias(libs.plugins.buildLogic.dokka)
    `maven-publish`
    alias(libs.plugins.ksp)
}

val publishingName = "monitoring"
val singleVariantName = "release"

android {
    namespace = "cz.kotox.crypto.sdk.monitoring"
    group = "cz.kotox.crypto.sdk"
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

//    ksp {
//        arg("room.generateKotlin", "true")
//    }
}

kotlin {
    explicitApi()
}

dependencies {
    api(projects.sdk.cryptoCommon)

    implementation(projects.sdk.internal.logger)
    implementation(projects.sdk.internal.common)
    implementation(kotlin("stdlib"))

    testImplementation(libs.bundles.test.unit)
    androidTestImplementation(libs.bundles.test.android)
}
