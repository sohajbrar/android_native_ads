package com.example.chatapp.wds.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role

/**
 * Plays the system click sound via the current Android [View].
 * Call this inside any @Composable scope (e.g. inside an onClick lambda that has
 * access to a captured `view` reference).
 */
@Composable
fun rememberClickSoundAction(): () -> Unit {
    val view = LocalView.current
    return remember(view) {
        { view.playSoundEffect(SoundEffectConstants.CLICK) }
    }
}

/**
 * A drop-in replacement for [Modifier.clickable] that also plays the
 * system tap sound when the element is clicked.
 */
fun Modifier.clickableWithSound(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = composed {
    val view = LocalView.current
    this.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role
    ) {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        onClick()
    }
}

/**
 * Overload that accepts a custom [MutableInteractionSource] and indication.
 */
fun Modifier.clickableWithSound(
    interactionSource: MutableInteractionSource?,
    indication: androidx.compose.foundation.Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = composed {
    val view = LocalView.current
    this.clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role
    ) {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        onClick()
    }
}
