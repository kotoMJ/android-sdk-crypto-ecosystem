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
    }

    publishing {
        singleVariant(singleVariantName) {
            // withSourcesJar()
        }
    }
}

dependencies {
    implementation(projects.sdk.internal.common)
    implementation(projects.sdk.internal.integrity)
    implementation(projects.sdk.internal.logger)
    implementation(projects.sdk.cryptoCommon)

    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)

    implementation(platform(libs.sentry.bom))
    implementation(libs.sentry.ktor.client)
}
