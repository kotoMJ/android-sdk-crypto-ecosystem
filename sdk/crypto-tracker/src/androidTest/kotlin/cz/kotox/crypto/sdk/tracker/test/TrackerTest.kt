package cz.kotox.crypto.sdk.tracker.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.tracker.Tracker
import cz.kotox.crypto.sdk.tracker.TrackerBuilder
import cz.kotox.crypto.sdk.tracker.internal.utils.logD
import cz.kotox.crypto.sdk.tracker.internal.utils.logE
import junit.framework.Assert.assertTrue
import junit.framework.Assert.fail
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackerTest {

    private lateinit var context: Context
    private lateinit var tracker: Tracker

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        tracker = TrackerBuilder().setLoggerCallback(
            sdkLoggerCallback = object : SDKLoggerCallback {
                override fun onLogMessage(
                    tag: String,
                    priority: LogPriority,
                    t: Throwable?,
                    message: String,
                ) {
                    println(message = message)
                }
            },
        ).build()
    }

    @Test
    fun testGetCoinMarkets() = runTest {
        val testTag = "[testGetCoinMarkets]"
        tracker.getTradingProducts().fold({
            logE(null) { "$testTag ERROR: $it" }
            fail("getCoinMarkets failed to return value")
        }, {
            it.forEach { item ->
                logD { "$testTag VALUE: $item" }
            }
            assertTrue("The returned list testGetCoinMarkets should not be empty.", it.isNotEmpty())
        })
    }
}
