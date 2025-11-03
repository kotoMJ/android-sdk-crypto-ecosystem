import cz.kotox.crypto.sdk.extensions.getPropertyOrVariable
import java.util.Properties

plugins {
    alias(libs.plugins.buildLogic.sdk.android.library)
    alias(libs.plugins.buildLogic.android.ktor)
    alias(libs.plugins.buildLogic.dokka)
    `maven-publish`
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

val publishingName = "news"
val singleVariantName = "release"

val versionProps = Properties()
file("version.properties").inputStream().use { versionProps.load(it) }

android {
    namespace = "cz.kotox.crypto.sdk.news"
    group = "cz.kotox.crypto.sdk"
    version = versionProps.getProperty("sdk.version")
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
                val apiKeyProvider = project.getPropertyOrVariable("CRYPTO_SDK_NEWS_API_KEY")
                val quotedApiKeyProvider = apiKeyProvider.map { "\"$it\"" }

                buildType.buildConfigField(
                    "String",
                    "TEST_SDK_NEWS_SERVICE_APIKEY",
                    quotedApiKeyProvider.get(),
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
}
