package com.example.chatapp.wds.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS Context Menu Item
 *
 * Represents a single item in the context menu with an icon and text.
 *
 * @param icon The icon to display (should be an outline variant)
 * @param text The text to display
 * @param onClick The callback when the item is clicked
 */
data class WDSContextMenuItem(
    val icon: ImageVector,
    val text: String,
    val onClick: () -> Unit
)

/**
 * WDS Context Menu Component
 *
 * A context menu that follows the WhatsApp Design System guidelines.
 * Displays a list of menu items with icons in a card with elevation.
 *
 * Based on the WDS Context Menu component specification:
 * - Min width: 200dp, Max width: 260dp
 * - Border radius: 12dp
 * - Elevation with shadow
 * - Items: 48dp height, 12dp padding, 12dp gap
 * - Icons: 24dp, outline style
 * - Text: Body 1 style
 *
 * @param expanded Whether the menu is visible
 * @param onDismissRequest Callback when the menu should be dismissed
 * @param offset The offset from the anchor point
 * @param items List of menu items to display
 * @param modifier The modifier to be applied to the menu
 */
@Composable
fun WDSContextMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    offset: IntOffset = IntOffset(0, 0),
    items: List<WDSContextMenuItem>,
    modifier: Modifier = Modifier
) {
    if (expanded) {
        Popup(
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(focusable = true),
            offset = offset
        ) {
            Surface(
                modifier = modifier
                    .widthIn(min = 200.dp, max = 260.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = WdsTheme.colors.colorAlwaysBlack.copy(alpha = 0.15f),
                        spotColor = WdsTheme.colors.colorAlwaysBlack.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(12.dp),
                color = WdsTheme.colors.colorSurfaceElevatedDefault,
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items.forEach { item ->
                        WDSContextMenuListItem(
                            icon = item.icon,
                            text = item.text,
                            onClick = {
                                item.onClick()
                                onDismissRequest()
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual list item in the context menu
 */
@Composable
private fun WDSContextMenuListItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .height(24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = WdsTheme.colors.colorContentDeemphasized
        )
        
        Text(
            text = text,
            style = WdsTheme.typography.body1,
            color = WdsTheme.colors.colorContentDefault,
            maxLines = 1
        )
    }
}
