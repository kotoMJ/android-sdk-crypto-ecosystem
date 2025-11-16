package cz.kotox.sdk.crypto.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    systemUiParams: SystemUiParams = remember { SystemUiParams() },
    fab: @Composable () -> Unit = {}, // NEW: Slot for the FAB
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    SystemUiController(systemUiParams = systemUiParams)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp),
                hostState = snackbarHostState,
            ) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    modifier = Modifier.shadow(elevation = 0.dp),
                    actionOnNewLine = false,
                    shape = RoundedCornerShape(12.dp),
                )
            }
        },
        floatingActionButton = fab, // NEW: Assign the fab
    ) { paddingValues ->
        // NEW: Pass the Scaffold's padding to the content
        content(paddingValues)
    }
}
