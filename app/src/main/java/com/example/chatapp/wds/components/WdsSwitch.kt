package com.example.chatapp.wds.components

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun WdsSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = WdsTheme.colors.colorContentOnAccent,
            checkedTrackColor = WdsTheme.colors.colorAccent,
            checkedBorderColor = WdsTheme.colors.colorAccent,
            uncheckedThumbColor = WdsTheme.colors.colorContentDefault,
            uncheckedTrackColor = WdsTheme.colors.colorSurfaceEmphasized,
            uncheckedBorderColor = WdsTheme.colors.colorOutlineDefault,
            disabledCheckedThumbColor = WdsTheme.colors.colorContentDisabled,
            disabledCheckedTrackColor = WdsTheme.colors.colorSurfaceEmphasized,
            disabledCheckedBorderColor = WdsTheme.colors.colorOutlineDeemphasized,
            disabledUncheckedThumbColor = WdsTheme.colors.colorContentDisabled,
            disabledUncheckedTrackColor = WdsTheme.colors.colorSurfaceEmphasized,
            disabledUncheckedBorderColor = WdsTheme.colors.colorOutlineDeemphasized
        )
    )
}

