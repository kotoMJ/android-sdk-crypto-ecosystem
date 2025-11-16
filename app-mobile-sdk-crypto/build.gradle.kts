plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildLogic.sdk.android.application)
    alias(libs.plugins.buildLogic.koin)
}

android {
    namespace = "cz.kotox.sdk.crypto.app"

    defaultConfig {
        applicationId = "cz.kotox.sdk.crypto.app"

        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs["debug"]
        }
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
//    lintChecks(projects.lint)

    implementation(projects.sdk.cryptoCoindata)

//    implementation(platform(libs.kotox.crypto.bom))
//    implementation(libs.kotox.crypto.common)
//    implementation(libs.kotoc.crypto.coindata)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.android.material)
    implementation(libs.androidx.compose.material3)

    implementation(libs.coil.kt.compose)

    debugImplementation(libs.leakcanary)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.timber)
}
