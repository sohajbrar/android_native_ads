package com.example.chatapp.wds.tokens

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

/**
 * WDS Shapes - Corner radius tokens for consistent rounded corners
 *
 * Example usage: WdsTheme.shapes.single
 *
 * Shape scale:
 * - none (0dp) - No rounding (Rectangle)
 * - halfPlus (6dp) - Subtle rounding
 * - single (8dp) - Base corner radius
 * - singlePlus (12dp) - Comfortable rounding
 * - double (16dp) - Standard rounding
 * - triple (24dp) - Large rounding
 * - triplePlus (28dp) - Extra large rounding
 * - circle (100dp) - Fully rounded (Pill shape)
 */
@Immutable
class WdsShapes(
    val none: CornerBasedShape = RoundedCornerShape(BaseDimensions.wdsCornerRadiusNone),
    val halfPlus: CornerBasedShape = RoundedCornerShape(BaseDimensions.wdsCornerRadiusHalfPlus),
    val single: CornerBasedShape = RoundedCornerShape(BaseDimensions.wdsCornerRadiusSingle),
    val singlePlus: CornerBasedShape = RoundedCornerShape(BaseDimensions.wdsCornerRadiusSinglePlus),
    val double: CornerBasedShape = RoundedCornerShape(BaseDimensions.wdsCornerRadiusDouble),
    val triple: CornerBasedShape = RoundedCornerShape(BaseDimensions.wdsCornerRadiusTriple),
    val triplePlus: CornerBasedShape = RoundedCornerShape(BaseDimensions.wdsCornerRadiusTriplePlus),
    val circle: CornerBasedShape = RoundedCornerShape(BaseDimensions.wdsCornerRadiusCircle),
)

/**
 * CompositionLocal used to pass [WdsShapes] down the composition hierarchy.
 *
 * This value is set as part of [WdsTheme]. Ensure [WdsTheme] is included in your composition
 * hierarchy to use this (automatically included if using [WdsTheme]).
 *
 * To get the current value of this CompositionLocal, use [WdsTheme.shapes].
 */
val LocalShapes: ProvidableCompositionLocal<WdsShapes> = staticCompositionLocalOf {
    error(
        "CompositionLocal not present for LocalShapes. This is likely because WdsTheme has not " +
                "been included in your Compose hierarchy."
    )
}

