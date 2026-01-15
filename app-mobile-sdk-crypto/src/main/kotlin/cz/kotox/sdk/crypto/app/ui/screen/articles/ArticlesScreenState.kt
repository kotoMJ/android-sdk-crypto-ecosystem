package cz.kotox.sdk.crypto.app.ui.screen.articles

import androidx.compose.runtime.Immutable
import cz.kotox.crypto.sdk.news.domain.Article

@Immutable
sealed interface ArticlesScreenState {

    data class Error(val message: String) : ArticlesScreenState

    data object Loading : ArticlesScreenState

    data class Content(
        val articles: List<Article>,
        val isRefreshing: Boolean = false,
    ) : ArticlesScreenState
}
