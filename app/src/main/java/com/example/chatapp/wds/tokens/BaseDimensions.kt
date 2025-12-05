package com.example.chatapp.wds.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Immutable
object BaseDimensions {
    val undefined = 0.dp
    val wdsSizeZero = 0.dp

    // Icon sizes
    val wdsIconSizeExtraSmall = 12.dp
    val wdsIconSizeSmall = 16.dp
    val wdsIconSizeMediumSmall = 18.dp
    val wdsIconSizeMedium = 24.dp
    val wdsIconSizeLarge = 28.dp

    // Touch target / component sizes
    val wdsTouchTargetCompact = 40.dp
    val wdsTouchTargetStandard = 44.dp
    val wdsTouchTargetComfortable = 48.dp
    val wdsTouchTargetLarge = 60.dp

    // Avatar sizes
    val wdsAvatarSmall = 28.dp
    val wdsAvatarMedium = 40.dp
    val wdsAvatarMediumLarge = 48.dp
    val wdsAvatarLarge = 64.dp
    val wdsAvatarExtraLarge = 88.dp
    val wdsAvatarXXL = 120.dp

    // Elevation
    val wdsElevationNone = 0.dp
    val wdsElevationSubtle = 1.dp
    val wdsElevationMedium = 6.dp

    // Common icon sizes (deprecated - use wdsIconSize* instead)
    val iconMedium = 24.dp

    // Common text sizes
    val wdsText12 = 12.sp
    val wdsText14 = 14.sp
    val wdsText16 = 16.sp
    val wdsText18 = 18.sp

    // Spacing
    val wdsSpacingQuarter = 2.dp
    val wdsSpacingHalf = 4.dp
    val wdsSpacingHalfPlus = 6.dp
    val wdsSpacingSingle = 8.dp
    val wdsSpacingSinglePlus = 12.dp
    val wdsSpacingDouble = 16.dp
    val wdsSpacingDoublePlus = 20.dp
    val wdsSpacingTriple = 24.dp
    val wdsSpacingTriplePlus = 28.dp
    val wdsSpacingQuad = 32.dp
    val wdsSpacingQuint = 40.dp

    // Corner radius values
    val wdsCornerRadiusNone = 0.dp
    val wdsCornerRadiusHalfPlus = 6.dp
    val wdsCornerRadiusSingle = 8.dp
    val wdsCornerRadiusSinglePlus = 12.dp
    val wdsCornerRadiusDouble = 16.dp
    val wdsCornerRadiusTriple = 24.dp
    val wdsCornerRadiusTriplePlus = 28.dp
    val wdsCornerRadiusCircle = 100.dp

    // Border widths
    val wdsBorderWidthNone = 0.dp
    val wdsBorderWidthThin = 1.dp
    val wdsBorderWidthMedium = 2.dp
    val wdsBorderWidthThick = 4.dp

    // Typography - Font sizes
    val wdsFontLargeTitle1Size = 32.sp
    val wdsFontLargeTitle2Size = 28.sp
    val wdsFontHeadline1Size = 24.sp
    val wdsFontHeadline2Size = 22.sp
    val wdsFontBody1Size = 16.sp
    val wdsFontBody2Size = 14.sp
    val wdsFontBody3Size = 12.sp
    val wdsFontChatListTitleSize = 17.sp
    val wdsFontChatHeaderTitleSize = 18.sp

    // Typography - Line heights
    val wdsFontLargeTitle1LineHeight = 40.sp
    val wdsFontLargeTitle2LineHeight = 36.sp
    val wdsFontHeadline1LineHeight = 32.sp
    val wdsFontHeadline2LineHeight = 28.sp
    val wdsFontBody1LineHeight = 24.sp
    val wdsFontBody2LineHeight = 20.sp  // Fixed: was 22.sp, should be 20.sp
    val wdsFontBody3LineHeight = 16.sp
    val wdsFontChatListTitleLineHeight = 22.sp
    val wdsFontChatHeaderTitleLineHeight = 28.sp

    // Typography - Letter spacing
    val wdsFontLargeTitle1LetterSpacing = 0.em
    val wdsFontLargeTitle2LetterSpacing = 0.em
    val wdsFontHeadline1LetterSpacing = 0.em
    val wdsFontHeadline2LetterSpacing = 0.em
    val wdsFontBody1LetterSpacing = 0.0125.em
    val wdsFontBody1EmphasizedLetterSpacing = 0.0125.em
    val wdsFontBody2LetterSpacing = 0.01785714.em
    val wdsFontBody2EmphasizedLetterSpacing = 0.01071428.em
    val wdsFontBody3LetterSpacing = 0.01666666.em
    val wdsFontBody3EmphasizedLetterSpacing = 0.02083333.em
    val wdsFontChatListTitleLetterSpacing = 0.em
    val wdsFontChatHeaderTitleLetterSpacing = 0.00833333.em
}

