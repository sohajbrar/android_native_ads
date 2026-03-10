package com.example.chatapp.features.advertise

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

/**
 * Animated step progress bar for the ad creation flow.
 *
 * Reads its starting position from [adViewModel]'s current progress so
 * it animates correctly in both directions:
 * - Forward (Next): bar grows from the previous step's progress to [toProgress].
 * - Backward (Back): bar shrinks from the current step's progress to [toProgress].
 *
 * @param toProgress  The progress fraction this step should reach (0f–1f).
 * @param adViewModel Shared ViewModel that tracks the last-displayed progress.
 */
@Composable
fun AdCreationProgressBar(
    toProgress: Float,
    adViewModel: AdCreationViewModel,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors

    var targetProgress by remember { mutableFloatStateOf(adViewModel.currentProgress) }

    LaunchedEffect(Unit) {
        targetProgress = toProgress
        adViewModel.currentProgress = toProgress
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "stepProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(colors.colorDivider)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = animatedProgress)
                .height(2.dp)
                .background(colors.colorAccent)
        )
    }
}
