package cz.kotox.sdk.crypto.app.ui.screen.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.crypto.sdk.news.News
import cz.kotox.sdk.crypto.app.extension.stateInForUi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ArticlesViewModel(
    private val news: News,
) : ViewModel() {

    val state: StateFlow<ArticlesScreenState> = ArticlesScreenPresenter(
        articlesFlow = flow {
            val result = news.getNews()
            emit(result.fold({ null }, { it }))
        },
    ).stateInForUi(
        scope = viewModelScope,
        initialValue = ArticlesScreenState.Loading,
    )
}
