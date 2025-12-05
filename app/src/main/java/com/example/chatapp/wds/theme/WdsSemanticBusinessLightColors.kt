package com.example.chatapp.wds.theme

import androidx.compose.runtime.Immutable

/**
 * WDS Semantic Business Light Colors
 * Business app color scheme with dark/warm gray accents instead of green
 * 
 * Key differences from Consumer:
 * - Primary accent: #171616 (Dark/Black) instead of Green
 * - Accent deemphasized: #F1EEEB (Warm Cream) instead of Green tint
 * - Accent emphasized: #262524 (Dark Gray) instead of Dark Green
 * 
 * Persistent elements (same across Consumer and Business):
 * - Badges: Green (#1DAA61)
 * - Status rings: Green
 * - Unread timestamps: Green
 */
@Immutable
class WdsSemanticBusinessLightColors : WdsColorScheme() {
    // ===== BUSINESS-SPECIFIC ACCENT COLORS =====
    // Primary accent is dark/black instead of green
    override val colorAccent = BaseColors.wdsWarmGray900  // #171616
    override val colorAccentDeemphasized = BaseColors.wdsWarmGray75  // #F7F5F3 (close to #F1EEEB)
    override val colorAccentEmphasized = BaseColors.wdsWarmGray800  // #262524
    
    // ===== PERSISTENT "ALWAYS BRANDED" GREEN =====
    // These stay green across both Consumer and Business apps
    override val colorAlwaysBranded = BaseColors.wdsGreen500  // #1DAA61
    override val colorActivityIndicator = BaseColors.wdsGreen400  // #25D366
    
    // ===== STANDARD SEMANTIC COLORS (Same as Consumer) =====
    override val color3pChatBackgroundWallpaper = BaseColors.wdsCoolGray75
    override val colorActiveListRow = BaseColors.wdsBlackAlpha10
    override val colorAlwaysBlack = BaseColors.wdsCoolGray1000
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
    
    // Action colors use dark accent instead of green
    override val colorContentActionEmphasized = BaseColors.wdsWarmGray900  // #171616
    
    override val colorContentDeemphasized = BaseColors.wdsCoolGray600
    override val colorContentDefault = BaseColors.wdsCoolGray1000
    override val colorContentDisabled = BaseColors.wdsCoolGray300
    override val colorContentExternalLink = BaseColors.wdsCobalt500
    override val colorContentInverse = BaseColors.wdsWhiteOpaque
    override val colorContentOnAccent = BaseColors.wdsWhiteOpaque
    override val colorContentRead = BaseColors.wdsCobalt400
    override val colorDivider = BaseColors.wdsCoolGrayAlpha10
    
    // Filter surface uses warm cream instead of green
    override val colorFilterSurfaceSelected = BaseColors.wdsWarmGray75  // #F7F5F3 (close to #F1EEEB)
    
    override val colorNegative = BaseColors.wdsRed400
    override val colorNegativeDeemphasized = BaseColors.wdsRed75
    override val colorNegativeEmphasized = BaseColors.wdsRed500
    override val colorOutlineDeemphasized = BaseColors.wdsCoolGrayAlpha20
    override val colorOutlineDefault = BaseColors.wdsCoolGray400
    override val colorOutlineProfilePhoto = BaseColors.wdsBlackAlpha10
    override val colorPlatformGestureBar = BaseColors.wdsBlackAlpha50
    override val colorPlatformStatusBar = BaseColors.wdsBlackAlpha80
    
    // Positive colors stay green (for status indicators, checkmarks, etc.)
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

    // ===== COLOR ALIASES =====
    override val colorBackgroundDimmer = BaseColors.wdsBlackAlpha40
    override val colorBubbleContentBusiness = colorContentDeemphasized
    override val colorBubbleContentE2e = colorContentDeemphasized
    override val colorBubbleSurfaceIncoming = colorSurfaceElevatedDefault
    override val colorBubbleSurfaceOverlay = colorSurfaceHighlight
    override val colorChatSurfaceComposer = colorSurfaceElevatedDefault
    override val colorChatSurfaceTray = colorSurfaceEmphasized
    
    // Action default uses dark accent instead of default content color
    override val colorContentActionDefault = colorAccent
    
    override val colorSurfaceNavBar = colorSurfaceDefault
}



