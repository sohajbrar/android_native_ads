package com.example.chatapp.wds.theme

import androidx.compose.runtime.Immutable

/**
 * WDS Semantic Business Dark Colors
 * Business app dark mode color scheme with white/light accents instead of green.
 *
 * Key differences from Consumer Dark:
 * - Primary accent: White (#FFFFFF) — FAB, primary buttons, selected tabs
 * - Content on accent: Near-black (#0A1014) — icons/text on white buttons
 * - Accent deemphasized: Dark warm gray — tinted backgrounds
 * - Filter surface: Dark warm gray instead of green tint
 *
 * Persistent "always-branded" elements remain green (badges, status rings).
 */
@Immutable
class WdsSemanticBusinessDarkColors : WdsSemanticDarkColors() {

    // White accent for FAB, primary buttons, selected tab indicators
    override val colorAccent = BaseColors.wdsWhiteOpaque
    override val colorAccentDeemphasized = BaseColors.wdsWarmGray800
    override val colorAccentEmphasized = BaseColors.wdsCoolGray200

    // Dark text/icons on the white accent surfaces
    override val colorContentOnAccent = BaseColors.wdsCoolGray1000

    // Action colors follow the business accent
    override val colorContentActionDefault = colorAccent
    override val colorContentActionEmphasized = BaseColors.wdsCoolGray200

    // Selected filters use warm gray instead of green tint
    override val colorFilterSurfaceSelected = BaseColors.wdsWarmGray800
}
