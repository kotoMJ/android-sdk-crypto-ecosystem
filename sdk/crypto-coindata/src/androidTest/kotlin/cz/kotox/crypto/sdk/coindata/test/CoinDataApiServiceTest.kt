package cz.kotox.crypto.sdk.coindata.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.kotox.crypto.sdk.coindata.TestCoinData
import cz.kotox.crypto.sdk.coindata.internal.CoinDataImpl
import cz.kotox.crypto.sdk.common.fold
import junit.framework.Assert.assertTrue
import junit.framework.Assert.fail
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinDataApiServiceTest {

    private lateinit var context: Context
    private lateinit var coinData: CoinDataImpl

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        coinData = TestCoinData.provideCoinData() as CoinDataImpl
    }

    @Test
    fun testApiGetUsdMarkets() = runTest {
        val testTag = "[testApiGetUsdMarkets]"
        coinData.apiTestDelegate.getUsdMarkets().fold({
            println("$testTag APIERROR: $it")
            fail("getUsdMarkets failed to return value")
        }, {
            it.forEach { item ->
                println("$testTag VALUE: $item")
            }
            assertTrue("The returned list testApiGetUsdMarkets should not be empty.", it.isNotEmpty())
        })
    }

    @Test
    fun testApiGetUsdCoinDetail() = runTest {
        val testTag = "[testApiGetUsdCoinDetail]"
        coinData.apiTestDelegate.getUsdCoinDetail().fold({
            println("$testTag APIERROR: $it")
            fail("getUsdCoinDetail failed to return value")
        }, {
            println("$testTag VALUE: $it")
        })
    }
}
