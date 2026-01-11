import cz.kotox.crypto.sdk.extensions.getPropertyOrVariable

plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    alias(libs.plugins.buildLogic.sdk.version.read)
    alias(libs.plugins.buildLogic.android.ktor)
    `maven-publish`
    alias(libs.plugins.ksp)
}

val publishingName = "network"
val singleVariantName = "release"

android {
    namespace = "cz.kotox.crypto.sdk.internal.network"
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

        buildTypes {
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
