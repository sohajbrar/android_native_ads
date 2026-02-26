package com.example.chatapp.wds.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WdsComingSoonDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Coming soon",
    positiveButtonText: String = "OK"
) {
    WdsDialog(
        title = title,
        positiveButton = WdsDialogButton(
            text = positiveButtonText,
            onClick = onDismissRequest
        ),
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}

