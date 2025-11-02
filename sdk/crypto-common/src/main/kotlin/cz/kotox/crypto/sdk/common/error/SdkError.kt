package cz.kotox.crypto.sdk.common.error

public open class SdkError(
    message: String,
    cause: Throwable?,
) : Exception(message, cause)
