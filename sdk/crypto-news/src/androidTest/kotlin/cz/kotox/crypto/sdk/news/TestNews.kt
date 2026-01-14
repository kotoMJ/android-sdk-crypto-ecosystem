package cz.kotox.crypto.sdk.news

import android.content.Context

object TestNews {
    fun provideNews(context: Context): News {
        return News.Builder(context).build()
    }
}
