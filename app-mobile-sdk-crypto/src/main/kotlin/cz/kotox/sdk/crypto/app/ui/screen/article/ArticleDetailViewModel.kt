package cz.kotox.sdk.crypto.app.ui.screen.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kotox.sdk.crypto.app.extension.stateInForUi
import cz.kotox.sdk.crypto.app.ui.ArticleDetailScreenRoute
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ArticleDetailViewModel(
    navKey: ArticleDetailScreenRoute,
) : ViewModel() {

    // We pass the article directly from the NavKey into the Presenter.
    // flowOf() creates a flow that emits this single value immediately.
    val state: StateFlow<ArticleDetailScreenState> = ArticleDetailScreenPresenter(
        articleFlow = flowOf(navKey.article),
    ).stateInForUi(
        scope = viewModelScope,
        // Since we have the data immediately, we can start with Content state
        // instead of Loading to avoid any flicker.
        initialValue = ArticleDetailScreenState.Content(navKey.article),
    )
}
