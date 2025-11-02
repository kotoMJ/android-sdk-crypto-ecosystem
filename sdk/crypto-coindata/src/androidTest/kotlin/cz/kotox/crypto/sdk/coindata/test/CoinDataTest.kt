package cz.kotox.crypto.sdk.coindata.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.kotox.crypto.sdk.coindata.CoinData
import cz.kotox.crypto.sdk.coindata.CoinDataBuilder
import cz.kotox.crypto.sdk.coindata.internal.utils.logD
import cz.kotox.crypto.sdk.coindata.internal.utils.logE
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import junit.framework.Assert.fail
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinDataTest {

    private lateinit var context: Context
    private lateinit var coinData: CoinData

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        coinData = CoinDataBuilder().setLoggerCallback(
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
        coinData.getCoinMarkets("usd").fold({
            logE(null) { "$testTag ERROR: $it" }
            fail("getCoinMarkets failed to return value")
        }, {
            it.forEach { item ->
                logD { "$testTag VALUE: $item" }
            }
        })
    }

    @Test
    fun testGetCoinDetail() = runTest {
        val testTag = "[testGetCoinMarkets]"
        coinData.getCoinDetail("usd").fold({
            logE(null) { "$testTag ERROR: $it" }
            fail("getCoinMarkets failed to return value")
        }, {
            logD { "$testTag VALUE: $it" }
        })
    }
}
