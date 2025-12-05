package com.example.chatapp.wds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS Divider Component
 *
 * A simple divider component that follows the WhatsApp Design System guidelines.
 * Uses WDS color tokens for consistent styling.
 *
 * @param modifier The modifier to be applied to the divider
 * @param color The color of the divider. Defaults to WDS colorDivider
 * @param thickness The thickness of the divider. Defaults to 1dp
 * @param startIndent The start padding of the divider. Defaults to 0dp
 * @param endIndent The end padding of the divider. Defaults to 0dp
 */
@Composable
fun WDSDivider(
    modifier: Modifier = Modifier,
    color: Color = WdsTheme.colors.colorDivider,
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp,
    endIndent: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = startIndent, end = endIndent)
            .height(thickness)
            .background(color)
    )
}

