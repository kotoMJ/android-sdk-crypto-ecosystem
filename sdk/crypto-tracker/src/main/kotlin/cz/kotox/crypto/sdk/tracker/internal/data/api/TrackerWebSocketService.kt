package cz.kotox.crypto.sdk.tracker.internal.data.api

import cz.kotox.crypto.sdk.tracker.internal.dto.CoinbaseSubscribeMessage
import cz.kotox.crypto.sdk.tracker.internal.dto.CoinbaseWebSocketMessage
import cz.kotox.crypto.sdk.tracker.internal.utils.logD
import cz.kotox.crypto.sdk.tracker.internal.utils.logE
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * Ktor-based implementation for the Coinbase WebSocket feed.
 */
internal class TrackerWebSocketService(
    private val client: HttpClient,
) : CoinbaseWebSocketApi {

    private val webSocketUrl = "wss://ws-feed.exchange.coinbase.com"

    @SuppressWarnings("TooGenericExceptionCaught")
    override fun observeMessages(
        productIds: List<String>,
        channels: List<String>,
    ): Flow<CoinbaseWebSocketMessage> = flow {
        logD { "TrackerWebSocketService: Connecting to $webSocketUrl" }

        val session = client.webSocketSession(webSocketUrl)

        try {
            // Launch coroutine to send the message
            session.launch {
                val subscribeMessage = CoinbaseSubscribeMessage(
                    productIds = productIds,
                    channels = channels,
                )

                logD { "TrackerWebSocketService: Sending message via sendSerialized: $subscribeMessage" }
                session.sendSerialized(subscribeMessage)
                logD { "TrackerWebSocketService: Sent subscription." }
            }

            // 2. Listen for incoming messages
            // We can now use receiveDeserialized directly
            while (session.isActive) {
                try {
                    val message = session.receiveDeserialized<CoinbaseWebSocketMessage>()
                    logD { "TrackerWebSocketService: Received message: $message" }
                    emit(message)
                } catch (e: CancellationException) {
                    // This is expected when the flow collector (e.g. .first()) cancels.
                    // We must re-throw it to properly close the session and stop the loop.
                    logE(e) { "TrackerWebSocketService: Flow cancelled by collector." }
                    throw e
                } catch (e: Exception) {
                    logE(e) { "TrackerWebSocketService: Failed to deserialize message" }
                    // Emit an Unknown message so the collector is aware
                    emit(CoinbaseWebSocketMessage.Unknown(type = "deserialization_error"))
                }
            }
        } catch (e: Exception) {
            logE(e) { "TrackerWebSocketService: Error during WebSocket session" }
            throw e // Re-throw to make the Flow fail
        } finally {
            // This will be called when the flow is cancelled or the socket closes
            if (session.isActive) {
                session.close(CloseReason(CloseReason.Codes.NORMAL, "Client disconnecting"))
            }
            logD { "TrackerWebSocketService: Disconnected." }
        }
    }
}
