package cz.kotox.sdk.crypto.app.ui.screen.article

import androidx.compose.runtime.Immutable
import cz.kotox.crypto.sdk.news.domain.Article

@Immutable
sealed interface ArticleDetailScreenState {
    data object Loading : ArticleDetailScreenState

    data class Content(
        val article: Article,
    ) : ArticleDetailScreenState
}
