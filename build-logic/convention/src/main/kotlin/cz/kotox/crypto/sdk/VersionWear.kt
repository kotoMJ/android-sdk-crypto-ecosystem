package cz.kotox.crypto.sdk

import cz.kotox.crypto.sdk.extensions.libs
import cz.kotox.crypto.sdk.extensions.version
import org.gradle.api.Project

private val Project.wearVersionMajor
    get() = libs.version("wearVersionMajor").toInt()

private val Project.wearVersionMinor
    get() = libs.version("wearVersionMinor").toInt()

private val Project.wearVersionPatch
    get() = libs.version("wearVersionPatch").toInt()

private val Project.wearVersionBuild
    get() = libs.version("wearVersionBuild").toInt()


val Project.wearVersionCode: Int
    get() =
        wearVersionMajor * 10000000 +
            wearVersionMinor * 100000 +
            wearVersionPatch * 1000 +
            wearVersionBuild

val Project.wearVersionName: String
    get() = "$wearVersionMajor.$wearVersionMinor.$wearVersionPatch"

