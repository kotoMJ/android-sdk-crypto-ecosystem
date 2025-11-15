package cz.kotox.sdk.crypto.app.ui.screen.currency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

@Composable
internal fun CurrencyScreen(
    // viewModel: CoinsViewModel = koinViewModel(),
) {
    // val state by viewModel.state.collectAsStateWithLifecycle()

//    when (val localState = state) {
//        is CoinsScreenState.Content -> {
//            CoinsContentScreen(
//                state = localState,
//            )
//        }
//
//        CoinsScreenState.Loading -> {}
//    }

    CurrencyContentScreen()
}
