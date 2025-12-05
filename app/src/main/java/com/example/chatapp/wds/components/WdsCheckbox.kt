package com.example.chatapp.wds.components

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun WdsCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = WdsTheme.colors.colorAccent,
            uncheckedColor = WdsTheme.colors.colorOutlineDefault,
            checkmarkColor = WdsTheme.colors.colorContentOnAccent,
            disabledCheckedColor = WdsTheme.colors.colorContentDisabled,
            disabledUncheckedColor = WdsTheme.colors.colorOutlineDeemphasized,
            disabledIndeterminateColor = WdsTheme.colors.colorContentDisabled
        )
    )
}
