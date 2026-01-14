package cz.kotox.crypto.sdk.news.internal.usecase

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.news.domain.Article
import cz.kotox.crypto.sdk.news.internal.data.repository.NewsRepository

internal class GetArticlesUseCase(
    private val repository: NewsRepository,
) {

    internal suspend fun execute(): Either<SdkError, List<Article>> {
        return repository.getNews()
    }
}
