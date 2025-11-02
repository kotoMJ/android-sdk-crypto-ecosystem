import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.util.Properties

plugins {
    `maven-publish`
    `java-platform`
}

group = "cz.kotox.crypto.sdk"


javaPlatform {
    allowDependencies()
}

dependencies {
    api(projects.sdk.cryptoTracker)
    api(projects.sdk.cryptoCoindata)
    api(projects.sdk.cryptoNews)
}

project.afterEvaluate {
    if (gradle.startParameter.taskNames.any { it.startsWith("publish") }) {

        publishing {
            publications {

                create<MavenPublication>("bom") {
                    groupId = this.groupId
                    artifactId = this.name
                    version = getLocalPropertyValue("sdk.bom.version")
                        ?: getLatestBomVersion(versionTagPrefix = "SDK_BOM_") // YYYY.MM.DD

                    pom.withXml {
                        val dependenciesManagementNode =
                            asNode().appendNode("dependencyManagement").appendNode("dependencies")

                        configurations.api.get()
                            .allDependencies
                            .forEach {
                                println(">>>>_ bom dependency: ${it.name}")
                                val dependencyNode =
                                    dependenciesManagementNode.appendNode("dependency")
                                dependencyNode.appendNode("groupId", it.group)
                                dependencyNode.appendNode("artifactId", it.name)
                                dependencyNode.appendNode("version", it.version)
                            }
                    }
                }
            }
            repositories {
                maven {
                    name = "GitHub"
                    url =
                        uri(
                            "https://maven.pkg.github.com/kotoMJ/android-sdk-ecosystem",
                        )

                    credentials  {
                        username = getLocalPropertyValue("gpr.user")?: System.getenv("GITHUB_ACTOR")
                        password = getLocalPropertyValue("gpr.key")?: System.getenv("GITHUB_TOKEN")
                    }

                }
            }
        }
    }
}

fun getLatestBomVersion(versionTagPrefix: String): String {
    ByteArrayOutputStream().use { outputStream ->
        project.exec {
            commandLine =
                "git describe --tags --abbrev=0 --match=$versionTagPrefix* HEAD".split(" ")
            standardOutput = outputStream
        }
        val bomVersion = outputStream.toString().trim().substringAfter(versionTagPrefix)
        println("Reading Git Tag based BOM version: $bomVersion")
        return bomVersion
    }
    // return "2024.07.18"
}

fun getLocalPropertyValue(
    key: String,
    file: String = "local.properties",
): String? {
    try {
        val prop =
            Properties().apply {
                load(FileInputStream(File(rootProject.rootDir, file)))
            }
        val propertyValue = prop.getProperty(key)
        println("Reading local property $key: $propertyValue")
        return propertyValue
    } catch (t: Throwable) {
        println("Unable to locate property $key in local file $file")
        return null
    }
}
