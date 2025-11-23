package cz.kotox.crypto.sdk.internal.logger.impl

import android.os.Build
import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.math.min

private const val MAX_LOG_LENGTH = 4000
private const val MAX_TAG_LENGTH = 23

// TODO MJ - make it available out of the package for test only!
public class AndroidLogger(
    originalTag: String,
    override val loggerCallback: SDKLoggerCallback,
) : SDKLogger {
    override val canLog: Boolean get() = true

    private val trimmedTag =
        if (originalTag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= 26) {
            originalTag
        } else {
            originalTag.substring(0, MAX_TAG_LENGTH)
        }

    override fun log(
        priority: LogPriority,
        t: Throwable?,
        message: () -> String,
    ) {
        // Avoid message composition for noOp logger callback
        if (loggerCallback !is SDKLoggerCallbackNoOp) {
            val finalMessage = message() + (t?.getStackTraceString() ?: "")

            if (finalMessage.length < MAX_LOG_LENGTH) {
                logProcessedMessage(
                    priority = priority,
                    t = t,
                    message = finalMessage,
                )
                return
            }

            var i = 0
            val length = finalMessage.length
            while (i < length) {
                var newline = finalMessage.indexOf('\n', i)
                newline = if (newline != -1) newline else length
                do {
                    val end = min(newline, i + MAX_LOG_LENGTH)
                    val part = finalMessage.substring(i, end)
                    logProcessedMessage(
                        priority = priority,
                        t = t,
                        message = part,
                    )
                    i = end
                } while (i < newline)
                i++
            }
        }
    }

    private fun logProcessedMessage(
        priority: LogPriority,
        t: Throwable?,
        message: String,
    ) {
        loggerCallback.onLogMessage(
            tag = trimmedTag,
            priority = priority,
            t = t,
            message = message,
        )
    }

    private fun Throwable.getStackTraceString(): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        this.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}
