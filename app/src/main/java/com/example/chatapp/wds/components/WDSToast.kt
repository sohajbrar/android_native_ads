package com.example.chatapp.wds.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme
import kotlinx.coroutines.delay

@Composable
fun WDSToast(
    message: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    durationMs: Long = 3000L,
    animationDurationMs: Int = 200
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val shapes = WdsTheme.shapes
    val density = LocalDensity.current

    var toastHeightPx by remember { mutableStateOf(0) }
    val translateY = remember { Animatable(0f) }
    var shouldRender by remember { mutableStateOf(isVisible) }
    var displayedMessage by remember { mutableStateOf(message) }

    // Keep the last meaningful message during exit animation.
    LaunchedEffect(isVisible, message) {
        if (isVisible && message.isNotBlank()) {
            displayedMessage = message
        }
    }

    if (isVisible) {
        LaunchedEffect(message) {
            delay(durationMs)
            onDismiss()
        }
    }

    LaunchedEffect(isVisible, toastHeightPx, message) {
        val extraOffsetPx = with(density) { dimensions.wdsSpacingDouble.toPx() }
        val offscreenPx = if (toastHeightPx > 0) {
            toastHeightPx.toFloat() + extraOffsetPx
        } else {
            // Fallback so we stay offscreen even before measuring.
            with(density) { 200.dp.toPx() }
        }

        if (isVisible) {
            shouldRender = true
            // Start below and slide up into place.
            translateY.snapTo(offscreenPx)
            translateY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = animationDurationMs)
            )
        } else {
            // Slide down out of view, then remove from composition.
            translateY.animateTo(
                targetValue = offscreenPx,
                animationSpec = tween(durationMillis = animationDurationMs)
            )
            shouldRender = false
        }
    }

    if (!shouldRender) return

    Surface(
        color = colors.colorSurfaceInverse,
        shape = shapes.single,
        modifier = modifier
            .onSizeChanged { toastHeightPx = it.height }
            .graphicsLayer { translationY = translateY.value }
    ) {
        Text(
            text = displayedMessage,
            style = typography.body2,
            color = colors.colorAlwaysWhite,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = 14.dp
            )
        )
    }
}

@Composable
fun WDSToastHost(
    message: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    durationMs: Long = 3000L
) {
    val dimensions = WdsTheme.dimensions

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensions.wdsSpacingTriple,
                end = dimensions.wdsSpacingTriple,
                bottom = dimensions.wdsSpacingTriple
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        WDSToast(
            message = message ?: "",
            isVisible = message != null,
            onDismiss = onDismiss,
            durationMs = durationMs
        )
    }
}

@Preview(showBackground = true, name = "WDS Toast - Light")
@Composable
private fun WDSToastPreviewLight() {
    WdsTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            WDSToast(
                message = "To create an audience with individual contacts, remove the lists you\u2019ve selected.",
                isVisible = true,
                onDismiss = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "WDS Toast - Short Message Light")
@Composable
private fun WDSToastShortPreviewLight() {
    WdsTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            WDSToast(
                message = "Message sent",
                isVisible = true,
                onDismiss = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "WDS Toast - Dark")
@Composable
private fun WDSToastPreviewDark() {
    WdsTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            WDSToast(
                message = "To create an audience with individual contacts, remove the lists you\u2019ve selected.",
                isVisible = true,
                onDismiss = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "WDS Toast Host - Light")
@Composable
private fun WDSToastHostPreviewLight() {
    WdsTheme(darkTheme = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Screen content goes here", style = WdsTheme.typography.body1)
            }

            WDSToastHost(
                message = "To create an audience with lists, remove the individual contacts you\u2019ve selected.",
                onDismiss = {},
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
