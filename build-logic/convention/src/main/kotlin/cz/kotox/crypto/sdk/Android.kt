package cz.kotox.crypto.sdk

import com.android.build.api.dsl.CommonExtension
import cz.kotox.crypto.sdk.extensions.javaVersion
import cz.kotox.crypto.sdk.extensions.libs
import cz.kotox.crypto.sdk.extensions.version
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get

internal fun Project.configureAndroid(
    extension: CommonExtension<*, *, *, *, *, *>,
) {
    extension.apply {
        val javaVersion = JavaVersion.toVersion(libs.javaVersion)

        compileSdk = libs.version("compileSdk").toInt()

        defaultConfig {
            minSdk = libs.version("minSdk").toInt()
        }

        compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        sourceSets["main"].java.srcDir("src/main/java")
    }

}