package cz.kotox.sdk.crypto.app.ui.screen.article

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun ArticleDetailScreen(
    viewModel: ArticleDetailViewModel,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state is ArticleDetailScreenState.Content) {
        ArticleDetailContentScreen(
            article = (state as ArticleDetailScreenState.Content).article,
            onBackClick = onBackClick,
        )
    }
}
