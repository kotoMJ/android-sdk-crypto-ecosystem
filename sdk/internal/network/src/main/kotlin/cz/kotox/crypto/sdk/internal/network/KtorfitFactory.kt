package cz.kotox.crypto.sdk.internal.network

import cz.kotox.crypto.sdk.common.configuration.NetworkLogMode
import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.internal.network.utils.MODULE_IDENTIFIER
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

public class KtorfitFactory(
    private val config: KtorConfig,
    private val sdkLoggerCallback: SDKLoggerCallback,
) {

    init {
        installNetworkLogger(loggerCallback = sdkLoggerCallback)
    }

    public val sdkJson = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        classDiscriminator = "type" // Important for WSS DTOs
        encodeDefaults = true

        // We dynamically add the StrictModeMarker to the context ONLY if strict mode is enabled.
        serializersModule = SerializersModule {
            if (config.strictModePolicy.strictSerialization) {
                contextual(StrictSerializationMarker::class, StrictSerializationMarker.serializer())
            }
        }
    }

    val httpClient = HttpClient(CIO) {
        engine {
            requestTimeout = config.networkTimeout.inWholeMilliseconds
        }

        install(ContentNegotiation) {
            json(sdkJson)
        }

        // Install WebSockets plugin
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(sdkJson)
        }

        if (config.isLoggingEnabled) {
            install(Logging) {
                level = when (config.loggingPolicy.networkLogMode) {
                    NetworkLogMode.BASIC -> LogLevel.INFO
                    NetworkLogMode.HEADERS -> LogLevel.HEADERS
                    NetworkLogMode.BODY -> LogLevel.ALL
                }

                val sdkLogPriority = when (config.loggingPolicy.networkLogMode) {
                    NetworkLogMode.BASIC -> LogPriority.INFO
                    NetworkLogMode.HEADERS -> LogPriority.DEBUG
                    NetworkLogMode.BODY -> LogPriority.VERBOSE
                }

                logger = object : Logger {
                    override fun log(message: String) {
                        sdkLoggerCallback.onLogMessage(
                            tag = "[KtorLogger]",
                            priority = sdkLogPriority,
                            t = null,
                            message = message,
                        )
                    }
                }
            }
        }

        if (BuildConfig.DEBUG && BuildConfig.BFF_CRYPTO_ADMIN_BYPASS_SECRET.isNotBlank()) {
            install(DefaultRequest) {
                // Apply the header to ALL requests made by this client
                header("X-Kotox-Bypass-Key", BuildConfig.BFF_CRYPTO_ADMIN_BYPASS_SECRET)
            }
        }
    }

    public val ktorfit: Ktorfit = Ktorfit.Builder()
        .baseUrl(config.baseUrl)
        .httpClient(httpClient)
        .build()

    private fun installNetworkLogger(loggerCallback: SDKLoggerCallback) {
        if (loggerCallback is SDKLoggerCallbackNoOp) {
            SDKLogger.uninstall(MODULE_IDENTIFIER)
        } else {
            SDKLogger.install(tag = MODULE_IDENTIFIER, loggerCallback = loggerCallback)
        }
    }
}
