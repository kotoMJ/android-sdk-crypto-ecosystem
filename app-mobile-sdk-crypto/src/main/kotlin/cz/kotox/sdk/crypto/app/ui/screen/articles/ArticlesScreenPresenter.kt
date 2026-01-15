package cz.kotox.sdk.crypto.app.ui.screen.articles

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.crypto.sdk.news.domain.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

@Suppress("FunctionName")
internal fun ArticlesScreenPresenter(
    articlesFlow: Flow<Either<SdkError, List<Article>>?>,
): Flow<ArticlesScreenState> = articlesFlow.scan(ArticlesScreenState.Loading as ArticlesScreenState) { accumulator, result ->
    when (result) {
        null -> {
            if (accumulator is ArticlesScreenState.Content) {
                accumulator.copy(isRefreshing = true)
            } else {
                ArticlesScreenState.Loading
            }
        }

        else -> result.fold(
            { error -> ArticlesScreenState.Error(message = error.message ?: "Unknown Error") },
            { articles -> ArticlesScreenState.Content(articles = articles, isRefreshing = false) },
        )
    }
}
