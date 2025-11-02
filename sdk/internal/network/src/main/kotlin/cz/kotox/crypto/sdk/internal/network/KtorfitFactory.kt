package cz.kotox.crypto.sdk.internal.network

import android.util.Log
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

public class KtorfitFactory(private val config: KtorConfig) {

    public val sdkJson = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        classDiscriminator = "type" // Important for WSS DTOs
        encodeDefaults = true
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
                        Log.v("KtorLogger", message)
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
