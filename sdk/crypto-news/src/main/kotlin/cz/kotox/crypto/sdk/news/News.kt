package cz.kotox.crypto.sdk.news

import android.content.Context
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.news.domain.Article

public interface News {

    public class Builder(context: Context) : NewsBuilder(context)

    public suspend fun getNews(): Either<SdkError, List<Article>>
}
