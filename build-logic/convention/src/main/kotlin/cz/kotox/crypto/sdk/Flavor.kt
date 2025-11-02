package cz.kotox.crypto.sdk

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project

@Suppress("EnumEntryName")
enum class FlavorDimension {
    env
}

@Suppress("EnumEntryName")
enum class Flavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
) {
    develop(FlavorDimension.env, ".develop"),
    staging(FlavorDimension.env, ".staging"),
    production(FlavorDimension.env)
}

@Suppress("UnusedReceiverParameter")
fun Project.configureFlavors(
    extension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: Flavor) -> Unit = {},
) {
    extension.apply {
        flavorDimensions += FlavorDimension.env.name
        productFlavors {
            Flavor.values().forEach {
                create(it.name) {
                    dimension = it.dimension.name
                    flavorConfigurationBlock(this, it)
                }
            }
        }
    }
}
