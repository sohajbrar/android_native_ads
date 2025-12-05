package com.example.chatapp.wds.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS Chip Action
 *
 * Different perceived outcomes performed on the Chip.
 * Maps to WDS design system chip actions.
 */
enum class WDSChipAction {
    /** Default chip with no end addon */
    DEFAULT,
    
    /** Input chip - shows close icon when selected */
    INPUT,
    
    /** Close chip - always shows close icon */
    CLOSE,
    
    /** Dropdown chip - shows dropdown arrow */
    DROPDOWN
}

/**
 * WDS Chip Size
 *
 * Different Chip sizes.
 * Maps to WDS design system chip sizes.
 */
enum class WDSChipSize {
    /** Standard chip - 32dp height */
    DEFAULT,
    
    /** Large chip - 40dp height */
    LARGE
}

/**
 * WDS Chip Component
 *
 * A chip component that follows the WhatsApp Design System guidelines.
 * Supports text, icons, badges, actions, and selection states.
 *
 * Based on the WDS Chip component which provides a versatile UI component
 * for filters, selections, and input options.
 *
 * @param text The text to display on the chip
 * @param selected Whether the chip is selected
 * @param onClick The callback to be invoked when the chip is clicked
 * @param modifier The modifier to be applied to the chip
 * @param enabled Whether the chip is enabled
 * @param action The action type that determines end addon behavior
 * @param size The size of the chip
 * @param icon Optional icon to display before the text
 * @param badgeText Optional badge text to display after the main text
 * @param interactionSource The interaction source for the chip
 */
@Composable
fun WDSChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    action: WDSChipAction = WDSChipAction.DEFAULT,
    size: WDSChipSize = WDSChipSize.DEFAULT,
    icon: ImageVector? = null,
    badgeText: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val endAddon = getEndAddon(action, selected)
    val hasIcon = icon != null
    val hasAddon = badgeText != null || endAddon != null
    val iconSize = getIconSize(size)
    
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalfPlus)
            ) {
                Text(
                    text = text,
                    style = WdsTheme.typography.body2Emphasized
                )
                badgeText?.let { badge ->
                    Text(
                        text = badge,
                        style = WdsTheme.typography.body3,
                        color = if (selected) {
                            getChipContentColor(action, selected, enabled).copy(alpha = 0.7f)
                        } else {
                            WdsTheme.colors.colorContentDeemphasized
                        }
                    )
                }
            }
        },
        modifier = modifier.defaultMinSize(minHeight = getChipHeight(size)),
        enabled = enabled,
        leadingIcon = icon?.let { iconVector ->
            {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            }
        },
        trailingIcon = endAddon?.let { addonIcon ->
            {
                Icon(
                    imageVector = addonIcon,
                    contentDescription = when (action) {
                        WDSChipAction.INPUT, WDSChipAction.CLOSE -> "Close"
                        WDSChipAction.DROPDOWN -> "Dropdown"
                        else -> null
                    },
                    modifier = Modifier.size(iconSize)
                )
            }
        },
        shape = WdsTheme.shapes.circle,
        colors = getChipColors(action),
        border = getChipBorder(enabled),
        interactionSource = interactionSource
    )
}

/**
 * Get chip colors based on action
 */
@Composable
private fun getChipColors(action: WDSChipAction): SelectableChipColors {
    val colors = WdsTheme.colors
    
    return when (action) {
        WDSChipAction.DEFAULT -> FilterChipDefaults.filterChipColors(
            // Unselected: Surface Default background, Content Deemphasized text
            containerColor = colors.colorSurfaceDefault,
            labelColor = colors.colorContentDeemphasized,
            iconColor = colors.colorContentDeemphasized,
            // Selected: Accent Deemphasized background, Accent Emphasized text
            selectedContainerColor = colors.colorAccentDeemphasized,
            selectedLabelColor = colors.colorAccentEmphasized,
            selectedLeadingIconColor = colors.colorAccentEmphasized,
            selectedTrailingIconColor = colors.colorAccentEmphasized,
            // Disabled
            disabledContainerColor = Color.Transparent,
            disabledLabelColor = colors.colorContentDisabled,
            disabledLeadingIconColor = colors.colorContentDisabled,
            disabledTrailingIconColor = colors.colorContentDisabled,
            disabledSelectedContainerColor = Color.Transparent
        )
        
        WDSChipAction.INPUT -> FilterChipDefaults.filterChipColors(
            // Unselected: Transparent background, Content Default text
            containerColor = Color.Transparent,
            labelColor = colors.colorContentDefault,
            iconColor = colors.colorContentDefault,
            // Selected: Accent Deemphasized background, Accent Emphasized text
            selectedContainerColor = colors.colorAccentDeemphasized,
            selectedLabelColor = colors.colorAccentEmphasized,
            selectedLeadingIconColor = colors.colorAccentEmphasized,
            selectedTrailingIconColor = colors.colorAccentEmphasized,
            // Disabled
            disabledContainerColor = Color.Transparent,
            disabledLabelColor = colors.colorContentDisabled,
            disabledLeadingIconColor = colors.colorContentDisabled,
            disabledTrailingIconColor = colors.colorContentDisabled,
            disabledSelectedContainerColor = Color.Transparent
        )
        
        WDSChipAction.CLOSE, WDSChipAction.DROPDOWN -> FilterChipDefaults.filterChipColors(
            // Unselected: Transparent background, Content Default text
            containerColor = Color.Transparent,
            labelColor = colors.colorContentDefault,
            iconColor = colors.colorContentDefault,
            // Selected: Surface Default background, Content Default text
            selectedContainerColor = colors.colorSurfaceDefault,
            selectedLabelColor = colors.colorContentDefault,
            selectedLeadingIconColor = colors.colorContentDefault,
            selectedTrailingIconColor = colors.colorContentDefault,
            // Disabled
            disabledContainerColor = Color.Transparent,
            disabledLabelColor = colors.colorContentDisabled,
            disabledLeadingIconColor = colors.colorContentDisabled,
            disabledTrailingIconColor = colors.colorContentDisabled,
            disabledSelectedContainerColor = Color.Transparent
        )
    }
}

/**
 * Get chip content color based on action and state
 */
@Composable
private fun getChipContentColor(
    action: WDSChipAction,
    selected: Boolean,
    enabled: Boolean
): Color {
    val colors = WdsTheme.colors
    
    if (!enabled) return colors.colorContentDisabled
    
    return when (action) {
        WDSChipAction.DEFAULT -> if (selected) {
            colors.colorAccentEmphasized
        } else {
            colors.colorContentDeemphasized
        }
        WDSChipAction.INPUT -> if (selected) {
            colors.colorAccentEmphasized
        } else {
            colors.colorContentDefault
        }
        WDSChipAction.CLOSE, WDSChipAction.DROPDOWN -> colors.colorContentDefault
    }
}

/**
 * Get chip border based on enabled state
 */
@Composable
private fun getChipBorder(enabled: Boolean): BorderStroke {
    val borderColor = if (enabled) {
        WdsTheme.colors.colorOutlineDeemphasized
    } else {
        WdsTheme.colors.colorContentDisabled
    }
    
    return BorderStroke(width = 1.dp, color = borderColor)
}

/**
 * Get chip height based on size
 */
private fun getChipHeight(size: WDSChipSize): Dp = when (size) {
    WDSChipSize.DEFAULT -> 32.dp
    WDSChipSize.LARGE -> 40.dp
}

/**
 * Get icon size based on chip size
 */
private fun getIconSize(size: WDSChipSize): Dp = 16.dp  // wds_icon_small for all sizes

/**
 * Get end addon icon based on action and selected state
 */
private fun getEndAddon(action: WDSChipAction, selected: Boolean): ImageVector? {
    return when (action) {
        WDSChipAction.DEFAULT -> null
        WDSChipAction.INPUT -> if (selected) Icons.Default.Close else null
        WDSChipAction.CLOSE -> Icons.Default.Close
        WDSChipAction.DROPDOWN -> Icons.Default.ArrowDropDown
    }
}