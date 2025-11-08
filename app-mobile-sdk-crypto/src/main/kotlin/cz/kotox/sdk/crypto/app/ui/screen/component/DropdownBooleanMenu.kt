package cz.kotox.sdk.crypto.app.ui.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DropdownBooleanMenu(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (Boolean?) -> Unit,
) {
    val items = arrayOf(null, true, false)
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .wrapContentHeight(),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            value = items[selectedIndex]?.toString() ?: "",
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = OutlinedTextFieldDefaults.colors().focusedContainerColor,
                disabledTextColor = OutlinedTextFieldDefaults.colors().focusedTextColor,
                disabledLabelColor = OutlinedTextFieldDefaults.colors().focusedLabelColor,
                disabledLeadingIconColor = OutlinedTextFieldDefaults.colors().focusedLeadingIconColor,
                disabledTrailingIconColor = OutlinedTextFieldDefaults.colors().focusedTrailingIconColor,
                disabledPlaceholderColor = OutlinedTextFieldDefaults.colors().focusedPlaceholderColor,
                disabledBorderColor = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor,
            ),
            trailingIcon = {
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {
                        selectedIndex = 0
                        expanded = false
                        onClick(null)
                    },
                ) {
                    Icon(
                        Icons.Filled.Clear,
                        "contentDescription",
                        tint = Color.Black,
                    )
                }
            },
            onValueChange = {},
            label = { Text(text) },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.LightGray,
                ),
        ) {
            items.forEachIndexed { index, boolean ->
                DropdownMenuItem(
                    text = { Text(boolean?.toString() ?: "") },
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        onClick(boolean)
                    },
                )
            }
        }
    }
}
