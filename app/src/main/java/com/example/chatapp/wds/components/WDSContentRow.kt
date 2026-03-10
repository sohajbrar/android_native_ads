package com.example.chatapp.wds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.R
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS Content Row – a row with optional leading content, label + value text, and trailing content.
 *
 * Used for structured information display (e.g. review screens, detail pages)
 * where each row shows a label above a value with optional leading/trailing elements.
 *
 * Layout specs:
 * - Padding: start 16dp, end 12dp, top 12dp, bottom 12dp
 * - Leading content: 40dp container (icon with background or thread image)
 * - Icon tint: colorContentDeemphasized
 * - Icon background: colorSurfaceEmphasized
 *
 * @param label Small deemphasized text displayed above the value
 * @param value Main text displayed below the label
 * @param modifier Optional modifier
 * @param leadingContent Optional composable for the leading slot (icon with background, image, etc.)
 * @param trailingContent Optional composable for the trailing slot (action icon, etc.)
 * @param onClick Optional click handler for the entire row
 */
@Composable
fun WDSContentRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .then(
                if (onClick != null) Modifier.clickableWithSound { onClick() }
                else Modifier
            )
            .padding(
                start = dimensions.wdsSpacingDouble,
                end = dimensions.wdsSpacingSinglePlus,
                top = dimensions.wdsSpacingSinglePlus,
                bottom = dimensions.wdsSpacingSinglePlus
            ),
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingDouble),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent?.invoke()

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
        ) {
            Text(
                text = label,
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = typography.body1,
                color = colors.colorContentDefault,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }

        trailingContent?.invoke()
    }
}

/**
 * WDS Content Row – convenience overload with a drawable icon inside a 40dp background container.
 *
 * The icon is rendered at 24dp centered inside a 40dp rounded container
 * with colorSurfaceEmphasized background and colorContentDefault tint.
 *
 * @param iconRes Drawable resource ID for the leading icon
 * @param label Small deemphasized text displayed above the value
 * @param value Main text displayed below the label
 * @param modifier Optional modifier
 * @param trailingContent Optional composable for the trailing slot
 * @param onClick Optional click handler for the entire row
 */
@Composable
fun WDSContentRow(
    @DrawableRes iconRes: Int,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val shapes = WdsTheme.shapes

    WDSContentRow(
        label = label,
        value = value,
        modifier = modifier,
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(dimensions.wdsTouchTargetCompact)
                    .clip(shapes.single)
                    .background(colors.colorSurfaceEmphasized),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                    tint = colors.colorContentDefault
                )
            }
        },
        trailingContent = trailingContent,
        onClick = onClick
    )
}

@Preview(showBackground = true, name = "WDS Content Row - Light")
@Composable
private fun WDSContentRowPreviewLight() {
    WdsTheme(darkTheme = false) {
        Column {
            WDSContentRow(
                iconRes = R.drawable.ic_business_broadcast,
                label = "Message",
                value = "Welcome, VIP clients!"
            )
            WDSContentRow(
                iconRes = R.drawable.ic_business_broadcast,
                label = "Available credit",
                value = "250 messages"
            )
        }
    }
}

@Preview(showBackground = true, name = "WDS Content Row - Dark")
@Composable
private fun WDSContentRowPreviewDark() {
    WdsTheme(darkTheme = true) {
        Column {
            WDSContentRow(
                iconRes = R.drawable.ic_business_broadcast,
                label = "Message",
                value = "Welcome, VIP clients!"
            )
        }
    }
}
