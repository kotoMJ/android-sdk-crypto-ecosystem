package cz.kotox.sdk.crypto.app.ui.screen.articles

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.crypto.sdk.news.domain.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("FunctionName")
internal fun ArticlesScreenPresenter(
    articlesFlow: Flow<Either<SdkError, List<Article>>?>,
): Flow<ArticlesScreenState> = articlesFlow.map { articles ->
    articles?.fold(
        { error -> ArticlesScreenState.Error(message = error.message ?: "Unknown Error") },
        { articles -> ArticlesScreenState.Content(articles = articles) },
    )
        ?: ArticlesScreenState.Loading
}
