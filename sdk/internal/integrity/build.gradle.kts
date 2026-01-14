import cz.kotox.crypto.sdk.extensions.getPropertyOrVariable

plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    alias(libs.plugins.buildLogic.sdk.version.read)
    alias(libs.plugins.buildLogic.android.ktor)
    alias(libs.plugins.buildLogic.dokka)
    `maven-publish`
    alias(libs.plugins.ksp)
}

val publishingName = "integrity"
val singleVariantName = "release"

android {
    namespace = "cz.kotox.crypto.sdk.internal.integrity"
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

        forEach { buildType ->

            if (buildType.name == "debug") {
                val apiKeyProvider = project.getPropertyOrVariable("BFF_CRYPTO_ADMIN_BYPASS_SECRET")
                val quotedSecret = apiKeyProvider.map { "\"$it\"" }.getOrElse("\"\"")

                buildType.buildConfigField(
                    "String",
                    "BFF_CRYPTO_ADMIN_BYPASS_SECRET",
                    quotedSecret,
                )
            } else {
                buildType.buildConfigField(
                    "String",
                    "BFF_CRYPTO_ADMIN_BYPASS_SECRET",
                    "\"\"",
                )
            }
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
    implementation(projects.sdk.cryptoCommon)
    implementation(projects.sdk.internal.logger)
    implementation(projects.sdk.internal.common)
    implementation(kotlin("stdlib"))

    implementation(libs.android.play.integrity)
    implementation(libs.coroutines.play.services)

    ksp(libs.room.compiler)

    testImplementation(libs.bundles.test.unit)

    androidTestImplementation(libs.bundles.test.android)
}
