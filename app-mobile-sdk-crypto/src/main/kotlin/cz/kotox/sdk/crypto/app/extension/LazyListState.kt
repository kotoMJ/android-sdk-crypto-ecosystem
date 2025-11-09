package cz.kotox.sdk.crypto.app.extension

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.isScrolledToStart() = firstVisibleItemIndex == 0
fun LazyListState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
