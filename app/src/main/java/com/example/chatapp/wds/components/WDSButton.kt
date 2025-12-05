package com.example.chatapp.wds.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

enum class WDSButtonVariant {
    FILLED,
    TONAL,
    OUTLINE,
    BORDERLESS
}

enum class WDSButtonAction {
    NORMAL,
    DESTRUCTIVE,
    MEDIA
}

enum class WDSButtonSize {
    SMALL,
    NORMAL,
    LARGE
}

@Composable
fun WDSButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    variant: WDSButtonVariant = WDSButtonVariant.FILLED,
    action: WDSButtonAction = WDSButtonAction.NORMAL,
    size: WDSButtonSize = WDSButtonSize.NORMAL,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val colors = getButtonColors(variant, action)
    val border = getButtonBorder(variant, action, enabled)
    val shape = getButtonShape(size)
    val contentPadding = getButtonPadding(size, isIconOnly = text == null && icon != null)
    val minHeight = getButtonHeight(size)
    val iconSize = getIconSize(size)
    val isIconOnly = text == null && icon != null

    Button(
        onClick = onClick,
        modifier = if (isIconOnly) {
            // Icon-only buttons should be square (equal width and height)
            modifier.size(minHeight)
        } else {
            modifier.defaultMinSize(minHeight = minHeight)
        },
        enabled = enabled,
        shape = shape,
        colors = colors,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
                if (text != null) {
                    Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingHalf))
                }
            }
            text?.let {
                Text(
                    text = it,
                    style = WdsTheme.typography.body2Emphasized
                )
            }
        }
    }
}

@Composable
private fun getButtonColors(variant: WDSButtonVariant, action: WDSButtonAction) = when (variant) {
    WDSButtonVariant.FILLED -> when (action) {
        WDSButtonAction.NORMAL -> ButtonDefaults.buttonColors(
            containerColor = WdsTheme.colors.colorAccent,
            contentColor = WdsTheme.colors.colorContentOnAccent,
            disabledContainerColor = WdsTheme.colors.colorSurfaceEmphasized,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
        WDSButtonAction.DESTRUCTIVE -> ButtonDefaults.buttonColors(
            containerColor = WdsTheme.colors.colorNegative,
            contentColor = WdsTheme.colors.colorContentOnAccent,
            disabledContainerColor = WdsTheme.colors.colorSurfaceEmphasized,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
        WDSButtonAction.MEDIA -> ButtonDefaults.buttonColors(
            containerColor = WdsTheme.colors.colorSurfaceElevatedDefault,
            contentColor = WdsTheme.colors.colorContentDefault,
            disabledContainerColor = WdsTheme.colors.colorSurfaceEmphasized,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
    }
    WDSButtonVariant.TONAL -> when (action) {
        WDSButtonAction.NORMAL -> ButtonDefaults.buttonColors(
            containerColor = WdsTheme.colors.colorAccentDeemphasized,
            contentColor = WdsTheme.colors.colorAccentEmphasized,
            disabledContainerColor = WdsTheme.colors.colorSurfaceEmphasized,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
        WDSButtonAction.DESTRUCTIVE -> ButtonDefaults.buttonColors(
            containerColor = WdsTheme.colors.colorNegativeDeemphasized,
            contentColor = WdsTheme.colors.colorNegativeEmphasized,
            disabledContainerColor = WdsTheme.colors.colorSurfaceEmphasized,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
        WDSButtonAction.MEDIA -> ButtonDefaults.buttonColors(
            containerColor = WdsTheme.colors.colorSurfaceEmphasized,
            contentColor = WdsTheme.colors.colorContentDefault,
            disabledContainerColor = WdsTheme.colors.colorSurfaceEmphasized,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
    }
    WDSButtonVariant.OUTLINE -> when (action) {
        WDSButtonAction.NORMAL -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = WdsTheme.colors.colorContentActionEmphasized,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
        WDSButtonAction.DESTRUCTIVE -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = WdsTheme.colors.colorNegativeEmphasized,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
        WDSButtonAction.MEDIA -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = WdsTheme.colors.colorContentActionEmphasized,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
    }
    WDSButtonVariant.BORDERLESS -> when (action) {
        WDSButtonAction.NORMAL -> ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = WdsTheme.colors.colorContentActionEmphasized,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
        WDSButtonAction.DESTRUCTIVE -> ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = WdsTheme.colors.colorNegativeEmphasized,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
        WDSButtonAction.MEDIA -> ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = WdsTheme.colors.colorContentActionEmphasized,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = WdsTheme.colors.colorContentDisabled
        )
    }
}

@Composable
private fun getButtonBorder(variant: WDSButtonVariant, action: WDSButtonAction, enabled: Boolean): BorderStroke? = when (variant) {
    WDSButtonVariant.OUTLINE -> {
        val borderColor = WdsTheme.colors.colorOutlineDeemphasized
        BorderStroke(width = 1.dp, color = if (enabled) borderColor else borderColor.copy(alpha = 0.38f))
    }
    else -> null
}

@Composable
private fun getButtonShape(size: WDSButtonSize): Shape = WdsTheme.shapes.circle

@Composable
private fun getButtonPadding(size: WDSButtonSize, isIconOnly: Boolean): PaddingValues {
    // Icon-only buttons need minimal padding to be perfectly round
    return if (isIconOnly) {
        PaddingValues(0.dp)
    } else {
        when (size) {
            WDSButtonSize.SMALL -> PaddingValues(
                horizontal = WdsTheme.dimensions.wdsSpacingSinglePlus,
                vertical = WdsTheme.dimensions.wdsSpacingHalf
            )
            WDSButtonSize.NORMAL -> PaddingValues(
                horizontal = WdsTheme.dimensions.wdsSpacingDouble,
                vertical = WdsTheme.dimensions.wdsSpacingSingle
            )
            WDSButtonSize.LARGE -> PaddingValues(
                horizontal = WdsTheme.dimensions.wdsSpacingDoublePlus,
                vertical = WdsTheme.dimensions.wdsSpacingSinglePlus
            )
        }
    }
}

private fun getButtonHeight(size: WDSButtonSize): Dp = when (size) {
    WDSButtonSize.SMALL -> 32.dp
    WDSButtonSize.NORMAL -> 40.dp
    WDSButtonSize.LARGE -> 48.dp
}

private fun getIconSize(size: WDSButtonSize): Dp = when (size) {
    WDSButtonSize.SMALL -> 16.dp
    WDSButtonSize.NORMAL -> 20.dp
    WDSButtonSize.LARGE -> 24.dp
}
