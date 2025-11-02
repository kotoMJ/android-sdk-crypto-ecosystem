package cz.kotox.crypto.sdk

import cz.kotox.crypto.sdk.extensions.libs
import cz.kotox.crypto.sdk.extensions.version
import org.gradle.api.Project

private val Project.mobileVersionMajor
    get() = libs.version("mobileVersionMajor").toInt()

private val Project.mobileVersionMinor
    get() = libs.version("mobileVersionMinor").toInt()

private val Project.mobileVersionPatch
    get() = libs.version("mobileVersionPatch").toInt()

private val Project.mobileVersionBuild
    get() = libs.version("mobileVersionBuild").toInt()


private val Project.mobileInstantVersionMajor
    get() = libs.version("mobileInstantVersionMajor").toInt()

private val Project.mobileInstantVersionMinor
    get() = libs.version("mobileInstantVersionMinor").toInt()

private val Project.mobileInstantVersionPatch
    get() = libs.version("mobileInstantVersionPatch").toInt()

private val Project.mobileInstantVersionBuild
    get() = libs.version("mobileInstantVersionBuild").toInt()


/* Ensure installable version code is greater than the instant version code */
private const val INSTALLABLE_MOBILE_VERSION_MULTIPLICATION = 1000000000

val Project.mobileVersionCode: Int
    get() =
        INSTALLABLE_MOBILE_VERSION_MULTIPLICATION +
            mobileVersionMajor * 10000000 +
            mobileVersionMinor * 100000 +
            mobileVersionPatch * 1000 +
            mobileVersionBuild

val Project.mobileInstantVersionCode: Int
    get() =
        mobileInstantVersionMajor * 10000000 +
            mobileInstantVersionMinor * 100000 +
            mobileInstantVersionPatch * 1000 +
            mobileInstantVersionBuild

val Project.mobileVersionName: String
    get() = "$mobileVersionMajor.$mobileVersionMinor.$mobileVersionPatch"

val Project.mobileInstantVersionName: String
    get() = "$mobileInstantVersionMajor.$mobileInstantVersionMinor.$mobileInstantVersionPatch"
