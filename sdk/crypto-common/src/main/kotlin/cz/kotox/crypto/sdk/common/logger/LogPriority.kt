package cz.kotox.crypto.sdk.common.logger

public enum class LogPriority(
    public val priorityInt: Int,
) {
    VERBOSE(2),
    DEBUG(3),
    INFO(4),
    WARN(5),
    ERROR(6),
    ASSERT(7),
}
