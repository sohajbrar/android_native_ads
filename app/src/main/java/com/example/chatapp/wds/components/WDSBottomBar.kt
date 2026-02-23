package com.example.chatapp.wds.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS Bottom Bar – a step-navigation bottom bar with a label and circular action button.
 *
 * Used in multi-step flows (e.g. broadcast draft) to show the current context
 * on the left and a forward-navigation action on the right.
 *
 * @param label Text displayed on the left side (e.g. audience/thread name)
 * @param onClick Callback when the action button is pressed
 * @param modifier Optional modifier
 * @param icon Icon displayed in the action button (defaults to forward arrow)
 * @param contentDescription Accessibility description for the action button
 */
@Composable
fun WDSBottomBar(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowForward,
    contentDescription: String? = "Next"
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colors.colorSurfaceDefault
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = 20.dp,
                    top = dimensions.wdsSpacingSingle,
                    end = dimensions.wdsSpacingSingle,
                    bottom = dimensions.wdsSpacingSingle
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = typography.body2Emphasized,
                color = colors.colorContentDefault,
                modifier = Modifier.weight(1f, fill = false)
            )

            FloatingActionButton(
                onClick = onClick,
                modifier = Modifier.size(dimensions.wdsTouchTargetComfortable),
                containerColor = colors.colorAccent,
                contentColor = colors.colorContentOnAccent,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "WDS Bottom Bar - Light")
@Composable
private fun WDSBottomBarPreviewLight() {
    WdsTheme(darkTheme = false) {
        WDSBottomBar(
            label = "New order",
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "WDS Bottom Bar - Dark")
@Composable
private fun WDSBottomBarPreviewDark() {
    WdsTheme(darkTheme = true) {
        WDSBottomBar(
            label = "New order",
            onClick = {}
        )
    }
}
