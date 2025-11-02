package cz.kotox.crypto.sdk.news.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.crypto.sdk.news.TestNews
import cz.kotox.crypto.sdk.news.internal.NewsImpl
import junit.framework.Assert.assertFalse
import junit.framework.Assert.fail
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsApiServiceTest {

    private lateinit var context: Context
    private lateinit var news: NewsImpl

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        news = TestNews.provideNews() as NewsImpl
    }

    @Test
    fun testApiNews() = runTest {
        val testTag = "[testApiNews]"
        news.apiTestDelegate.getNews(query = "Bitcoin").fold({
            println("$testTag APIERROR: $it")
            fail("testApiNews failed to return value")
        }, {
            it.articles?.forEach { item ->
                println("$testTag VALUE: $item")
            }
            assertFalse("The returned list testApiNews should not be empty.", it.articles.isNullOrEmpty())
        })
    }
}
