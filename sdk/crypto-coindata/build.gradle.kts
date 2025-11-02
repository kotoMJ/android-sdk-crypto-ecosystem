plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    alias(libs.plugins.buildLogic.android.ktor)
    alias(libs.plugins.buildLogic.dokka)
    `maven-publish`
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

val publishingName = "coindata"
val singleVariantName = "release"

android {
    namespace = "cz.kotox.crypto.sdk.coindata"
    group = "cz.kotox.crypto.sdk"
    version = "0.0.3"
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

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    explicitApi()
}

dependencies {
    api(projects.sdk.cryptoCommon)

    implementation(projects.sdk.internal.logger)
    implementation(projects.sdk.internal.common)
    implementation(projects.sdk.internal.network)
    implementation(kotlin("stdlib"))

    implementation(libs.room.ktx)
    implementation(libs.room.runtime)

    ksp(libs.room.compiler)

    testImplementation(libs.bundles.test.unit)

    androidTestImplementation(libs.bundles.test.android)
    testImplementation(kotlin("test"))
}
