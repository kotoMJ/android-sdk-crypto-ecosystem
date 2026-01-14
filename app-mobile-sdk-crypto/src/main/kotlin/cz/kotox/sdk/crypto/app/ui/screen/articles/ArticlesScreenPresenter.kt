package cz.kotox.sdk.crypto.app.ui.screen.articles

import cz.kotox.crypto.sdk.news.domain.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("FunctionName")
internal fun ArticlesScreenPresenter(
    articlesFlow: Flow<List<Article>?>,
): Flow<ArticlesScreenState> = articlesFlow.map { articles ->
    if (articles == null) {
        ArticlesScreenState.Loading
    } else {
        ArticlesScreenState.Content(
            articles = articles,
        )
    }
}
