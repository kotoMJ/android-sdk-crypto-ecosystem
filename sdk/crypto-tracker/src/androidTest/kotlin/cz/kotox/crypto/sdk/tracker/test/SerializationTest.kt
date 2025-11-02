package cz.kotox.crypto.sdk.tracker.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.kotox.crypto.sdk.tracker.internal.dto.CoinbaseSubscribeMessage
import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertTrue // Using kotlin.test assertions

@RunWith(AndroidJUnit4::class)
class SerializationTest {

    // 1. Json configuration WITHOUT classDiscriminator (like our 'outgoingJsonEncoder')
    private val simpleJsonEncoder = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true // Make output readable
        encodeDefaults = true // <--- ADD THIS
        // NO classDiscriminator
    }

    // 2. Json configuration WITH classDiscriminator (like KtorfitFactory's 'appJson')
    private val discriminatingJsonEncoder = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true // Make output readable
        encodeDefaults = true // <--- ADD THIS
        classDiscriminator = "type" // The potential culprit
    }

    private val testMessage = CoinbaseSubscribeMessage(
        productIds = listOf("BTC-USD"),
        channels = listOf("ticker"),
    )

    @Test
    fun testSerialization_WithoutDiscriminator() {
        println("--- Testing serialization WITHOUT classDiscriminator ---")
        val jsonString = simpleJsonEncoder.encodeToString(testMessage)
        println("Output JSON:\n$jsonString")

        // Assertion: Check if the 'type' field is present and correct
        assertTrue(
            jsonString.contains("\"type\": \"subscribe\""),
            "JSON string should contain '\"type\": \"subscribe\"' when serialized without discriminator.",
        )
        println("Assertion PASSED: 'type' field is present.")
    }

    @Test
    fun testSerialization_WithDiscriminator() {
        println("\n--- Testing serialization WITH classDiscriminator = 'type' ---")
        val jsonString = discriminatingJsonEncoder.encodeToString(testMessage)
        println("Output JSON:\n$jsonString")

        // Assertion: Check if the 'type' field is present and correct
        // Based on our WebSocket logs, we expect this assertion to FAIL
        assertTrue(
            jsonString.contains("\"type\": \"subscribe\""),
            "JSON string should contain '\"type\": \"subscribe\"' even when serialized WITH discriminator.",
        )
        println("Assertion PASSED: 'type' field is present.")
    }
}
