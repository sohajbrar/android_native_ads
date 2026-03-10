package com.example.chatapp.wds.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

/**
 * Standard WDS card border with reduced opacity for a softer appearance.
 *
 * Use this instead of [androidx.compose.material3.CardDefaults.outlinedCardBorder]
 * for all outlined cards to keep a consistent, lighter stroke across the app.
 */
@Composable
fun wdsCardBorder(): BorderStroke = BorderStroke(
    width = 1.dp,
    color = WdsTheme.colors.colorDivider.copy(alpha = 0.3f)
)
