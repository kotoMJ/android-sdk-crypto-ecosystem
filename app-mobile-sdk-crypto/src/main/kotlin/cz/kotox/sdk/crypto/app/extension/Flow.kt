package cz.kotox.sdk.crypto.app.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

/**
 * Use WhileSubscribed(5000) to keep the upstream flow active for 5 seconds more after
 * the disappearance of the last collector.
 * That avoids restarting the upstream flow in certain situations such as configuration changes.
 * This is especially helpful when upstream flows are expensive to create and when these
 * operators are used in ViewModels.
 *
 * Based on https://medium.com/androiddevelopers/things-to-know-about-flows-sharein-and-statein-operators-20e6ccb2bc74
 */
fun <T> Flow<T>.stateInForUi(scope: CoroutineScope, initialValue: T) = onEach { state ->
    // Timber.d("UiState: $state")
}.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = initialValue,
)
