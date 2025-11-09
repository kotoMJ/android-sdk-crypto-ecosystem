package cz.kotox.sdk.crypto.app.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.material3.NavigationBar as Material3NavigationBar
import androidx.compose.material3.NavigationBarItem as Material3NavigationBarItem

@Composable
fun SDKAppNavigationBar(
    modifier: Modifier = Modifier,
    elevation: Dp = NavigationBarDefaults.Elevation,
    content: @Composable RowScope.() -> Unit,
) {
    Material3NavigationBar(
        modifier = modifier,
        tonalElevation = elevation,
        content = content,
    )
}

@Composable
fun RowScope.SDKAppNavigationBarItem(
    iconPainter: Painter,
    title: String,
    selected: () -> Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Material3NavigationBarItem(
        modifier = modifier,
        selected = selected(),
        onClick = onClick,
        icon = {
            Icon(
                painter = iconPainter,
                contentDescription = title,
            )
        },
        label = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        },
    )
}
