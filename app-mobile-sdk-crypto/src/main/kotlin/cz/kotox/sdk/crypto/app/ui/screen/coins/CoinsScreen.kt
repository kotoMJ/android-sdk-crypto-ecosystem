package cz.kotox.sdk.crypto.app.ui.screen.coins

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
internal fun CoinsScreen() {
    // val state by viewModel.state.collectAsStateWithLifecycle()

    var bottomSheetVisible by remember { mutableStateOf(false) }

    BackHandler(bottomSheetVisible) {
        bottomSheetVisible = false
    }

//    CoinsContentScreen(
//        //state = state,
//    )
}
