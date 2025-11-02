import java.io.FileInputStream
import java.util.Properties

plugins {
    kotlin("jvm") //important for dokka to be activated in root module.
    `maven-publish`
}

subprojects {
    afterEvaluate {
        if (gradle.startParameter.taskNames.any { it.startsWith("publish") }) {

            val subproject = this

            val singleVariantName = "release"

            val modulesToSkip = listOf("bom", "internal")

            //BOM module has it's own publishing definition of publication
            if (subproject.name !in modulesToSkip) {

                subproject.pluginManager.apply("maven-publish")

                subproject.extensions.configure<PublishingExtension>("publishing") {
                    publications {
                        create<MavenPublication>(subproject.name) {
                            groupId = this.groupId
                            artifactId = this.name
                            version = this.version

                            //Use afterEvaluate here to wait for aar to be at disposal!
                            afterEvaluate {
                                publications.named(subproject.name) {
                                    from(subproject.components.findByName(singleVariantName))
                                }
                            }
                        }

                        repositories {
                            maven {
                                name = "GitLab"
                                url = uri(
                                    "https://maven.pkg.github.com/kotoMJ/android-sdk-crypto-ecosystem",
                                )

                                credentials {
                                    username = getLocalPropertyValue("gpr.user")?: System.getenv("GITHUB_ACTOR")
                                    password = getLocalPropertyValue("gpr.key")?: System.getenv("GITHUB_TOKEN")
                                }

                            }
                        }
                    }

                }
            }
        }
    }
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
