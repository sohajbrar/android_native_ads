package com.example.chatapp.wds.components

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun WdsRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = RadioButtonDefaults.colors(
            selectedColor = WdsTheme.colors.colorAccent,
            unselectedColor = WdsTheme.colors.colorOutlineDefault,
            disabledSelectedColor = WdsTheme.colors.colorContentDisabled,
            disabledUnselectedColor = WdsTheme.colors.colorOutlineDeemphasized
        )
    )
}

