package com.example.chatapp.wds.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * WDS Semantic Light Colors
 * Light mode color scheme with semantic color mappings
 */
@Immutable
open class WdsSemanticLightColors : WdsColorScheme() {
    override val color3pChatBackgroundWallpaper = BaseColors.wdsCoolGray75
    override val colorAccent = BaseColors.wdsGreen500
    override val colorAccentDeemphasized = BaseColors.wdsGreen100
    override val colorAccentEmphasized = BaseColors.wdsGreen700
    override val colorActiveListRow = BaseColors.wdsBlackAlpha10
    override val colorActivityIndicator = BaseColors.wdsGreen400
    override val colorAlwaysBlack = BaseColors.wdsCoolGray1000
    override val colorAlwaysBranded = BaseColors.wdsGreen500
    override val colorAlwaysWhite = BaseColors.wdsWhiteOpaque
    override val colorBackgroundElevatedWashInset = BaseColors.wdsWarmGray75
    override val colorBackgroundElevatedWashPlain = BaseColors.wdsWhiteOpaque
    override val colorBackgroundWashInset = BaseColors.wdsWarmGray75
    override val colorBackgroundWashPlain = BaseColors.wdsWhiteOpaque
    override val colorBubbleContentDeemphasized = BaseColors.wdsCoolGrayAlpha50
    override val colorBubbleSurfaceBusiness = BaseColors.wdsEmerald100
    override val colorBubbleSurfaceE2e = BaseColors.wdsYellow100
    override val colorBubbleSurfaceOutgoing = BaseColors.wdsGreen100
    override val colorBubbleSurfaceSystem = BaseColors.wdsWhiteAlpha90
    override val colorChatBackgroundWallpaper = BaseColors.wdsCream75
    override val colorChatForegroundWallpaper = BaseColors.wdsCream150
    override val colorContentActionEmphasized = BaseColors.wdsGreen600
    override val colorContentDeemphasized = BaseColors.wdsCoolGray600
    override val colorContentDefault = BaseColors.wdsCoolGray1000
    override val colorContentDisabled = BaseColors.wdsCoolGray300
    override val colorContentExternalLink = BaseColors.wdsCobalt500
    override val colorContentInverse = BaseColors.wdsWhiteOpaque
    override val colorContentOnAccent = BaseColors.wdsWhiteOpaque
    override val colorContentRead = BaseColors.wdsCobalt400
    override val colorDivider = BaseColors.wdsCoolGrayAlpha10
    override val colorFilterSurfaceSelected = BaseColors.wdsGreen100
    override val colorNegative = BaseColors.wdsRed400
    override val colorNegativeDeemphasized = BaseColors.wdsRed75
    override val colorNegativeEmphasized = BaseColors.wdsRed500
    override val colorOutlineDeemphasized = BaseColors.wdsCoolGrayAlpha20
    override val colorOutlineDefault = BaseColors.wdsCoolGray400
    override val colorOutlineProfilePhoto = BaseColors.wdsBlackAlpha10
    override val colorPlatformGestureBar = BaseColors.wdsBlackAlpha50
    override val colorPlatformStatusBar = BaseColors.wdsBlackAlpha80
    override val colorPositive = BaseColors.wdsGreen500
    override val colorPositiveDeemphasized = BaseColors.wdsGreen75
    override val colorStatusSeen = BaseColors.wdsWarmGray300
    override val colorSurfaceDefault = BaseColors.wdsWhiteOpaque
    override val colorSurfaceElevatedDefault = BaseColors.wdsWhiteOpaque
    override val colorSurfaceElevatedEmphasized = BaseColors.wdsWarmGray75
    override val colorSurfaceEmphasized = BaseColors.wdsWarmGray75
    override val colorSurfaceHighlight = BaseColors.wdsWarmGray300Alpha15
    override val colorSurfaceInverse = BaseColors.wdsCoolGray800
    override val colorSurfacePressed = BaseColors.wdsCoolGrayAlpha20
    override val colorWarning = BaseColors.wdsYellow400
    override val colorWarningDeemphasized = BaseColors.wdsYellow75

    // These colors are aliases
    override val colorBackgroundDimmer = Color(0x52000000)
    override val colorBubbleContentBusiness = colorContentDeemphasized
    override val colorBubbleContentE2e = colorContentDeemphasized
    override val colorBubbleSurfaceIncoming = colorSurfaceElevatedDefault
    override val colorBubbleSurfaceOverlay = colorSurfaceHighlight
    override val colorChatSurfaceComposer = colorSurfaceElevatedDefault
    override val colorChatSurfaceTray = colorSurfaceEmphasized
    override val colorContentActionDefault = colorContentDefault
    override val colorSurfaceNavBar = colorSurfaceDefault
}

