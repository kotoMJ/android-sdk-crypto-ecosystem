package cz.kotox.sdk.crypto.app.ui.screen.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.crypto.sdk.news.News
import cz.kotox.sdk.crypto.app.extension.stateInForUi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ArticlesViewModel(
    private val news: News,
) : ViewModel() {

    private val refreshSignal = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    val state: StateFlow<ArticlesScreenState> = refreshSignal
        .flatMapLatest {
            flow {
                val result = news.getNews()
                emit(result.fold({ Either.Error(it) }, { Either.Value(it) }))
            }
        }
        .let { ArticlesScreenPresenter(it) }
        .stateInForUi(
            scope = viewModelScope,
            initialValue = ArticlesScreenState.Loading,
        )

    fun refresh() {
        refreshSignal.tryEmit(Unit)
    }
}
