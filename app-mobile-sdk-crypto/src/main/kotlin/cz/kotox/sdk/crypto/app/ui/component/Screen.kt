package cz.kotox.sdk.crypto.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    systemUiParams: SystemUiParams = remember { SystemUiParams() },
    content: @Composable ColumnScope.() -> Unit,
) {
    SystemUiController(systemUiParams = systemUiParams)

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                content = content,
            )
            SnackbarHost(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
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
        }
    }
}
