package cz.kotox.crypto.sdk.internal.network

import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

public class KtorfitFactory(
    private val config: KtorConfig,
    private val sdkLogger: SDKLogger,
) {

    public val sdkJson = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        classDiscriminator = "type" // Important for WSS DTOs
        encodeDefaults = true
        serializersModule = SerializersModule {
            // Whenever @Contextual Long is found, this specific instance is used.
            SafeLongSerializer(
                logger = sdkLogger,
                strictMode = BuildConfig.DEBUG, // Fail in DEBUG mode, log.error otherwise
            )
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

        if (true || config.isLoggingEnabled) {
            install(Logging) {
                level = LogLevel.ALL

                logger = object : Logger {
                    override fun log(message: String) {
                        sdkLogger.log(
                            priority = LogPriority.VERBOSE,
                            t = null,
                            { "[KtorLogger] $message" },
                        )
                        // Log.v("KtorLogger", message)
                    }
                }
            }
        }
    }

    public val ktorfit: Ktorfit = Ktorfit.Builder()
        .baseUrl(config.baseUrl)
        .httpClient(httpClient)
        .build()
}
