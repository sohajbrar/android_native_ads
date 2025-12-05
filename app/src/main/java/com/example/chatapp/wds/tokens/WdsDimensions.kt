package com.example.chatapp.wds.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

/**
 * WDS Dimensions - Size, spacing, elevation, and component tokens for consistent design
 *
 * Example usage:
 * - WdsTheme.dimensions.wdsIconSizeMedium
 * - WdsTheme.dimensions.wdsSpacingDouble
 * - WdsTheme.dimensions.wdsElevationSubtle
 *
 * Icon sizes:
 * - ExtraSmall (12dp) - Tiny icons
 * - Small (16dp) - Small icons
 * - MediumSmall (18dp) - Medium-small icons
 * - Medium (24dp) - Standard icon size
 * - Large (28dp) - Large icons
 *
 * Touch target / component sizes:
 * - Compact (40dp) - Compact touch targets
 * - Standard (44dp) - Standard touch targets
 * - Comfortable (48dp) - Comfortable touch targets (Material Design minimum)
 * - Large (60dp) - Large touch targets
 *
 * Avatar sizes:
 * - Small (28dp) - Small avatars
 * - Medium (40dp) - Medium avatars (chat header)
 * - MediumLarge (48dp) - Medium-large avatars (chat list)
 * - Large (64dp) - Large avatars
 * - ExtraLarge (88dp) - Extra large avatars
 * - XXL (120dp) - Maximum avatar size
 *
 * Elevation:
 * - None (0dp) - No elevation
 * - Subtle (1dp) - Subtle shadow/elevation
 * - Medium (6dp) - Prominent elevation
 *
 * Spacing scale:
 * - Quarter (2dp) - Minimal spacing
 * - Half (4dp) - Very tight spacing
 * - HalfPlus (6dp) - Tight spacing
 * - Single (8dp) - Base spacing unit
 * - SinglePlus (12dp) - Comfortable spacing
 * - Double (16dp) - Standard spacing
 * - DoublePlus (20dp) - Generous spacing
 * - Triple (24dp) - Large spacing
 * - TriplePlus (28dp) - Extra large spacing
 * - Quad (32dp) - Very large spacing
 * - Quint (40dp) - Maximum spacing
 */
@Immutable
class WdsDimensions {
    val wdsSizeZero: Dp = BaseDimensions.wdsSizeZero

    // Icon sizes
    val wdsIconSizeExtraSmall: Dp = BaseDimensions.wdsIconSizeExtraSmall
    val wdsIconSizeSmall: Dp = BaseDimensions.wdsIconSizeSmall
    val wdsIconSizeMediumSmall: Dp = BaseDimensions.wdsIconSizeMediumSmall
    val wdsIconSizeMedium: Dp = BaseDimensions.wdsIconSizeMedium
    val wdsIconSizeLarge: Dp = BaseDimensions.wdsIconSizeLarge

    // Touch target / component sizes
    val wdsTouchTargetCompact: Dp = BaseDimensions.wdsTouchTargetCompact
    val wdsTouchTargetStandard: Dp = BaseDimensions.wdsTouchTargetStandard
    val wdsTouchTargetComfortable: Dp = BaseDimensions.wdsTouchTargetComfortable
    val wdsTouchTargetLarge: Dp = BaseDimensions.wdsTouchTargetLarge

    // Avatar sizes
    val wdsAvatarSmall: Dp = BaseDimensions.wdsAvatarSmall
    val wdsAvatarMedium: Dp = BaseDimensions.wdsAvatarMedium
    val wdsAvatarMediumLarge: Dp = BaseDimensions.wdsAvatarMediumLarge
    val wdsAvatarLarge: Dp = BaseDimensions.wdsAvatarLarge
    val wdsAvatarExtraLarge: Dp = BaseDimensions.wdsAvatarExtraLarge
    val wdsAvatarXXL: Dp = BaseDimensions.wdsAvatarXXL

    // Elevation
    val wdsElevationNone: Dp = BaseDimensions.wdsElevationNone
    val wdsElevationSubtle: Dp = BaseDimensions.wdsElevationSubtle
    val wdsElevationMedium: Dp = BaseDimensions.wdsElevationMedium

    // Spacing
    val wdsSpacingQuarter: Dp = BaseDimensions.wdsSpacingQuarter
    val wdsSpacingHalf: Dp = BaseDimensions.wdsSpacingHalf
    val wdsSpacingHalfPlus: Dp = BaseDimensions.wdsSpacingHalfPlus
    val wdsSpacingSingle: Dp = BaseDimensions.wdsSpacingSingle
    val wdsSpacingSinglePlus: Dp = BaseDimensions.wdsSpacingSinglePlus
    val wdsSpacingDouble: Dp = BaseDimensions.wdsSpacingDouble
    val wdsSpacingDoublePlus: Dp = BaseDimensions.wdsSpacingDoublePlus
    val wdsSpacingTriple: Dp = BaseDimensions.wdsSpacingTriple
    val wdsSpacingTriplePlus: Dp = BaseDimensions.wdsSpacingTriplePlus
    val wdsSpacingQuad: Dp = BaseDimensions.wdsSpacingQuad
    val wdsSpacingQuint: Dp = BaseDimensions.wdsSpacingQuint

    // Border widths
    val wdsBorderWidthNone: Dp = BaseDimensions.wdsBorderWidthNone
    val wdsBorderWidthThin: Dp = BaseDimensions.wdsBorderWidthThin
    val wdsBorderWidthMedium: Dp = BaseDimensions.wdsBorderWidthMedium
    val wdsBorderWidthThick: Dp = BaseDimensions.wdsBorderWidthThick
}

/**
 * CompositionLocal used to pass [WdsDimensions] down the composition hierarchy.
 *
 * This value is set as part of [WdsTheme]. Ensure [WdsTheme] is included in your composition
 * hierarchy to use this (automatically included if using [WdsTheme]).
 *
 * To get the current value of this CompositionLocal, use [WdsTheme.dimensions].
 */
val LocalDimensions: ProvidableCompositionLocal<WdsDimensions> = staticCompositionLocalOf {
    error(
        "CompositionLocal not present for LocalDimensions. This is likely because WdsTheme has not " +
                "been included in your Compose hierarchy."
    )
}

