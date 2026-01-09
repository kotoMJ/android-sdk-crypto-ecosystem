package cz.kotox.sdk.crypto.app.ui.screen.article

import cz.kotox.crypto.sdk.news.domain.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("FunctionName")
internal fun ArticleDetailScreenPresenter(
    articleFlow: Flow<Article?>,
): Flow<ArticleDetailScreenState> = articleFlow.map { article ->
    if (article == null) {
        ArticleDetailScreenState.Loading
    } else {
        ArticleDetailScreenState.Content(
            article = article,
        )
    }
}
