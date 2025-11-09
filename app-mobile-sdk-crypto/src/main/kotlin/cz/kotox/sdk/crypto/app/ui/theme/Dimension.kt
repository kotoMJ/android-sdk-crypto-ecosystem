@file:Suppress("MatchingDeclarationName")

package cz.kotox.sdk.crypto.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class SDKDimensions(
    val snackbarPaddingBottom: Dp,
)

val SDKAppDimensions = SDKDimensions(
    snackbarPaddingBottom = 16.dp,
)
