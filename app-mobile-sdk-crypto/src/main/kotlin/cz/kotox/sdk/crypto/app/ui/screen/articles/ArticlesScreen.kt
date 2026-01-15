package cz.kotox.sdk.crypto.app.ui.screen.articles

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.kotox.crypto.sdk.news.domain.Article
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ArticlesScreen(
    onItemClick: (Article) -> Unit,
    viewModel: ArticlesViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val localState = state) {
        is ArticlesScreenState.Content -> {
            ArticlesContentScreen(
                state = localState,
                onItemClick = onItemClick,
                contentPadding = contentPadding,
                isRefreshing = localState.isRefreshing,
                onRefresh = viewModel::refresh,
            )
        }

        is ArticlesScreenState.Loading -> ArticlesLoadingView()

        is ArticlesScreenState.Error -> ArticlesErrorView(
            message = localState.message,
            onRetry = viewModel::refresh,
        )
    }
}
