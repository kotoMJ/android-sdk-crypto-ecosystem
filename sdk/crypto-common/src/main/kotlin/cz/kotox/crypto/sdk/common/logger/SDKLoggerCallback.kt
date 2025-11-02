package cz.kotox.crypto.sdk.common.logger

public interface SDKLoggerCallback {
    public fun onLogMessage(
        tag: String,
        priority: LogPriority,
        t: Throwable? = null,
        message: String,
    )
}
