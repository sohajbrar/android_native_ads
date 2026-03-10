package com.example.chatapp.wds.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS Supported Button
 *
 * A small pill-shaped button used as a trailing action in section headers.
 * Uses colorSurfaceHighlight background and body2Emphasized text styling.
 *
 * @param text The button label text
 * @param onClick Callback when the button is pressed
 * @param modifier Optional modifier
 */
@Composable
fun WDSSupportedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Surface(
        shape = WdsTheme.shapes.circle,
        color = colors.colorSurfaceHighlight,
        modifier = modifier
            .height(32.dp)
            .clickableWithSound(role = Role.Button) { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
        ) {
            Text(
                text = text,
                style = typography.body2Emphasized,
                color = colors.colorContentDefault
            )
        }
    }
}

/**
 * WDS Section Divider
 *
 * A section header row with a title on the left and an optional trailing
 * [WDSSupportedButton] on the right. Used to separate content sections
 * in list-based screens.
 *
 * @param title The section title text
 * @param modifier Optional modifier
 * @param actionText Optional text for the trailing supported button
 * @param onActionClick Callback when the trailing button is pressed
 */
@Composable
fun WDSSectionDivider(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensions.wdsSpacingDouble,
                end = dimensions.wdsSpacingDouble,
                top = dimensions.wdsSpacingDouble,
                bottom = dimensions.wdsSpacingSingle
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = typography.body2,
            color = colors.colorContentDeemphasized
        )

        if (actionText != null) {
            WDSSupportedButton(
                text = actionText,
                onClick = onActionClick
            )
        }
    }
}
