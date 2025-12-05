package com.example.chatapp.wds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS List Row Component
 *
 * A reusable component for displaying list items with an icon and text.
 * Matches WhatsApp Design System specifications.
 *
 * @param iconRes The drawable resource ID for the leading icon
 * @param text The text to display
 * @param onClick Click handler for the row
 * @param modifier Optional modifier for customization
 * @param subtitle Optional subtitle text below the main text
 * @param iconTint Optional color for the icon (defaults to content color)
 */
@Composable
fun WDSListRow(
    @DrawableRes iconRes: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    iconTint: androidx.compose.ui.graphics.Color? = null
) {
    // Cache theme lookups
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(horizontal = dimensions.wdsSpacingTriple), // 24dp horizontal only
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingDouble), // 16dp gap
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading icon
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(dimensions.wdsIconSizeMedium), // 24dp
            tint = iconTint ?: colors.colorContentDefault
        )

        // Text content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = if (subtitle != null) {
                Arrangement.spacedBy(dimensions.wdsSpacingQuarter) // 2dp gap
            } else {
                Arrangement.Center
            }
        ) {
            Text(
                text = text,
                style = typography.body1,
                color = colors.colorContentDefault,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Optional subtitle
            subtitle?.let { sub ->
                Text(
                    text = sub,
                    style = typography.body3,
                    color = colors.colorContentDeemphasized,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

