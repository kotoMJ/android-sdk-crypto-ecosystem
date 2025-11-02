package cz.kotox.crypto.sdk.tracker.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.crypto.sdk.tracker.TestCoinTracker
import cz.kotox.crypto.sdk.tracker.internal.TrackerImpl
import cz.kotox.crypto.sdk.tracker.internal.dto.CoinbaseWebSocketMessage
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import junit.framework.Assert.fail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinbaseApiServiceTest {

    private lateinit var context: Context
    private lateinit var tracker: TrackerImpl

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        tracker = TestCoinTracker.provideTracker() as TrackerImpl
    }

    @Test
    fun testApiGetProducts() = runTest {
        val testTag = "[testApiGetProducts]"
        tracker.apiTestDelegate.getProducts().fold({
            println("$testTag APIERROR: $it")
            fail("getCoinMarkets failed to return value")
        }, {
            it.forEach { item ->
                println("$testTag VALUE: $item")
            }
            assertTrue("The returned list testApiGetProducts should not be empty.", it.isNotEmpty())
        })
    }

    /**
     * New test case for the WebSocket connection.
     * It subscribes to the "ticker" channel for "BTC-USD" and waits for the
     * first message to arrive, which confirms the connection is successful.
     */
    @Test
    fun testApiObserveMessages() = runTest {
        val testTag = "[testApiObserveMessages]"
        println("$testTag: Starting WebSocket test...")

        try {
            // .first() will launch the flow, wait for one item, and then cancel the flow.
            // If the connection fails or the flow is empty, it will throw an exception.
            val firstMessage = tracker.apiTestDelegate.observeMessages(
                productIds = listOf("BTC-USD"),
                channels = listOf("ticker", "heartbeat"), // ticker or subscriptions
            ).first() // Collects the first message and cancels

            println("$testTag VALUE: $firstMessage")

            // A non-null check is the simplest assertion
            assertNotNull("First message should not be null", firstMessage)

            // We can be more specific. The first message should be a success.
            // It's usually 'subscriptions', but could be 'ticker'.
            // We just need to ensure it's not an error.
            if (firstMessage is CoinbaseWebSocketMessage.Error) {
                fail("Received an error message: ${firstMessage.reason}")
            }

            assertTrue(
                "First message must be Subscriptions or Ticker",
                firstMessage is CoinbaseWebSocketMessage.Subscriptions || firstMessage is CoinbaseWebSocketMessage.Ticker,
            )
        } catch (e: Exception) {
            println("$testTag APIERROR: $e")
            e.printStackTrace()
            fail("testApiObserveMessages failed with exception: ${e.message}")
        }
    }
}
