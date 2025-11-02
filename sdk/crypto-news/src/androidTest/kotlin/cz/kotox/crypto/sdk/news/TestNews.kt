package cz.kotox.crypto.sdk.news

object TestNews {
    fun provideNews(): News {
        return News.Builder(newsServiceApiKey = BuildConfig.TEST_SDK_NEWS_SERVICE_APIKEY).build()
    }
}
