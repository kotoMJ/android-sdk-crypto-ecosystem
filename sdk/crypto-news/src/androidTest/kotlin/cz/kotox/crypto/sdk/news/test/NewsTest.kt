package cz.kotox.crypto.sdk.news.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.news.News
import cz.kotox.crypto.sdk.news.NewsBuilder
import cz.kotox.crypto.sdk.news.internal.utils.logD
import cz.kotox.crypto.sdk.news.internal.utils.logE
import junit.framework.Assert.fail
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsTest {

    private lateinit var context: Context
    private lateinit var news: News

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        news = NewsBuilder(context = context).setLoggerCallback(
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
    fun testGetArticles() = runTest {
        val testTag = "[testGetCoinMarkets]"
        news.getNews().fold({
            logE(null) { "$testTag ERROR: $it" }
            fail("getCoinMarkets failed to return value")
        }, {
            it.forEach { item ->
                logD { "$testTag VALUE: $item" }
            }
        })
    }
}
