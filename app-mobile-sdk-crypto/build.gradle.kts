import java.util.Properties

plugins {
    alias(libs.plugins.buildLogic.sdk.android.application)
//    alias(libs.plugins.buildLogic.android.hilt)
}

val sdkSampleProperties = try {
    Properties().apply {
        load(
            rootProject.file("./app-mobile-sdk-crypto-sample/properties/app.crypto.properties")
                .reader(),
        )
    }
} catch (ignored: Throwable) {
    println(ignored.printStackTrace())
    println("Missing app.content.properties file in app-mobile-sdk-crypto-sample/properties/app.crypto.properties!")
    Properties()
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

    debugImplementation(libs.leakcanary)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.navigationCompose)

    implementation(libs.timber)
}
