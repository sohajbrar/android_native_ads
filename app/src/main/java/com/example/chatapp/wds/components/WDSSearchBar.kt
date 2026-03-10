package com.example.chatapp.wds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS Search Bar Component
 *
 * A pill-shaped search bar matching the WhatsApp design pattern.
 * Features a leading search icon, placeholder text, and optional trailing content.
 *
 * @param query Current search text
 * @param onQueryChange Callback when text changes
 * @param modifier Optional modifier
 * @param placeholder Placeholder text shown when query is empty
 * @param trailingContent Optional trailing composable (e.g., a numpad icon)
 * @param onSearch Callback when search action is triggered via keyboard
 */
@Composable
fun WDSSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    trailingContent: @Composable (() -> Unit)? = null,
    onSearch: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val shapes = WdsTheme.shapes

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(shapes.circle)
            .background(colors.colorSurfaceHighlight)
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSingle
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            modifier = Modifier.size(dimensions.wdsIconSizeMedium),
            tint = colors.colorContentDeemphasized
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimensions.wdsSpacingSingle),
            contentAlignment = Alignment.CenterStart
        ) {
            if (query.isEmpty()) {
                Text(
                    text = placeholder,
                    style = typography.body1,
                    color = colors.colorContentDeemphasized
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                textStyle = typography.body1.copy(color = colors.colorContentDefault),
                singleLine = true,
                cursorBrush = SolidColor(colors.colorAccent),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (trailingContent != null) {
            trailingContent()
        }
    }
}
