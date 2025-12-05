package com.example.chatapp.wds.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS FAB Style
 *
 * Defines the visual style of the FAB
 */
enum class WDSFabStyle {
    PRIMARY,     // Accent color background
    SECONDARY    // Surface container background
}

/**
 * WDS FAB Size
 *
 * Defines the size of the FAB
 */
enum class WDSFabSize {
    SMALL,       // Small FAB (40dp)
    NORMAL,      // Normal FAB (56dp)
    LARGE        // Large FAB (96dp)
}

/**
 * WDS Floating Action Button Component
 *
 * A floating action button component that follows the WhatsApp Design System guidelines.
 * Supports different styles and sizes.
 *
 * @param onClick The callback to be invoked when the FAB is clicked
 * @param icon The icon to display in the FAB
 * @param modifier The modifier to be applied to the FAB
 * @param style The visual style of the FAB
 * @param size The size of the FAB
 * @param contentDescription The content description for accessibility
 * @param interactionSource The interaction source for the FAB
 */
@Composable
fun WDSFab(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    style: WDSFabStyle = WDSFabStyle.PRIMARY,
    size: WDSFabSize = WDSFabSize.NORMAL,
    contentDescription: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val containerColor = when (style) {
        WDSFabStyle.PRIMARY -> WdsTheme.colors.colorAccent
        WDSFabStyle.SECONDARY -> WdsTheme.colors.colorSurfaceElevatedDefault
    }

    val contentColor = when (style) {
        WDSFabStyle.PRIMARY -> WdsTheme.colors.colorContentOnAccent
        WDSFabStyle.SECONDARY -> WdsTheme.colors.colorContentDefault
    }

    val shape: Shape = when (size) {
        WDSFabSize.SMALL -> WdsTheme.shapes.singlePlus
        WDSFabSize.NORMAL -> WdsTheme.shapes.double
        WDSFabSize.LARGE -> WdsTheme.shapes.triple
    }

    val iconSize = when (size) {
        WDSFabSize.SMALL -> 20.dp
        WDSFabSize.NORMAL -> 24.dp
        WDSFabSize.LARGE -> 36.dp
    }

    when (size) {
        WDSFabSize.SMALL -> {
            SmallFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                shape = shape,
                containerColor = containerColor,
                contentColor = contentColor,
                interactionSource = interactionSource
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
        WDSFabSize.NORMAL -> {
            FloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                shape = shape,
                containerColor = containerColor,
                contentColor = contentColor,
                interactionSource = interactionSource
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
        WDSFabSize.LARGE -> {
            LargeFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                shape = shape,
                containerColor = containerColor,
                contentColor = contentColor,
                interactionSource = interactionSource
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}
