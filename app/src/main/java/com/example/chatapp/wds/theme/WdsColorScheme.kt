package com.example.chatapp.wds.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * WDS Semantic Color Scheme
 * Abstract class defining all semantic color tokens used throughout the app
 */
@Immutable
abstract class WdsColorScheme {
    abstract val color3pChatBackgroundWallpaper: Color
    abstract val colorAccent: Color
    abstract val colorAccentDeemphasized: Color
    abstract val colorAccentEmphasized: Color
    abstract val colorActiveListRow: Color
    abstract val colorActivityIndicator: Color
    abstract val colorAlwaysBlack: Color
    abstract val colorAlwaysBranded: Color
    abstract val colorAlwaysWhite: Color
    abstract val colorBackgroundDimmer: Color
    abstract val colorBackgroundElevatedWashInset: Color
    abstract val colorBackgroundElevatedWashPlain: Color
    abstract val colorBackgroundWashInset: Color
    abstract val colorBackgroundWashPlain: Color
    abstract val colorBubbleContentBusiness: Color
    abstract val colorBubbleContentDeemphasized: Color
    abstract val colorBubbleContentE2e: Color
    abstract val colorBubbleSurfaceBusiness: Color
    abstract val colorBubbleSurfaceE2e: Color
    abstract val colorBubbleSurfaceIncoming: Color
    abstract val colorBubbleSurfaceOutgoing: Color
    abstract val colorBubbleSurfaceOverlay: Color
    abstract val colorBubbleSurfaceSystem: Color
    abstract val colorChatBackgroundWallpaper: Color
    abstract val colorChatForegroundWallpaper: Color
    abstract val colorChatSurfaceComposer: Color
    abstract val colorChatSurfaceTray: Color
    abstract val colorContentActionDefault: Color
    abstract val colorContentActionEmphasized: Color
    abstract val colorContentDeemphasized: Color
    abstract val colorContentDefault: Color
    abstract val colorContentDisabled: Color
    abstract val colorContentExternalLink: Color
    abstract val colorContentInverse: Color
    abstract val colorContentOnAccent: Color
    abstract val colorContentRead: Color
    abstract val colorDivider: Color
    abstract val colorFilterSurfaceSelected: Color
    abstract val colorNegative: Color
    abstract val colorNegativeDeemphasized: Color
    abstract val colorNegativeEmphasized: Color
    abstract val colorOutlineDeemphasized: Color
    abstract val colorOutlineDefault: Color
    abstract val colorOutlineProfilePhoto: Color
    abstract val colorPlatformGestureBar: Color
    abstract val colorPlatformStatusBar: Color
    abstract val colorPositive: Color
    abstract val colorPositiveDeemphasized: Color
    abstract val colorStatusSeen: Color
    abstract val colorSurfaceDefault: Color
    abstract val colorSurfaceElevatedDefault: Color
    abstract val colorSurfaceElevatedEmphasized: Color
    abstract val colorSurfaceEmphasized: Color
    abstract val colorSurfaceHighlight: Color
    abstract val colorSurfaceInverse: Color
    abstract val colorSurfaceNavBar: Color
    abstract val colorSurfacePressed: Color
    abstract val colorWarning: Color
    abstract val colorWarningDeemphasized: Color
}

/**
 * CompositionLocal used to pass [WdsColorScheme] down the composition hierarchy.
 *
 * This value is set as part of [WdsTheme]. Ensure [WdsTheme] is included in your composition
 * hierarchy to use this.
 *
 * To get the current value of this CompositionLocal, use [WdsTheme.colors].
 */
val LocalWdsColors = staticCompositionLocalOf<WdsColorScheme> {
    error(
        "CompositionLocal not present for LocalWdsColors. This is likely because WdsTheme has not " +
                "been included in your Compose hierarchy."
    )
}

