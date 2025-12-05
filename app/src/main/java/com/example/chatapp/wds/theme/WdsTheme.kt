package com.example.chatapp.wds.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.example.chatapp.wds.tokens.WdsTypography
import com.example.chatapp.wds.tokens.WdsDimensions
import com.example.chatapp.wds.tokens.WdsShapes
import com.example.chatapp.wds.tokens.LocalTypography
import com.example.chatapp.wds.tokens.LocalDimensions
import com.example.chatapp.wds.tokens.LocalShapes

/**
 * WDS Theme
 * Main theme composable that provides WDS design system to the composition tree
 *
 * @param darkTheme Whether to use dark theme colors. Defaults to system theme.
 * @param businessMode Whether to use Business app colors (dark/warm gray accents) instead of Consumer colors (green accents).
 * @param colors Custom color scheme. If null, uses default light/dark colors based on darkTheme and businessMode.
 * @param typography Custom typography. If null, uses default WDS typography.
 * @param dimensions Custom dimensions. If null, uses default WDS dimensions.
 * @param shapes Custom shapes. If null, uses default WDS shapes.
 * @param content The composable content to be themed
 */
@Composable
fun WdsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    businessMode: Boolean = true,  // Default to Business mode
    colors: WdsColorScheme? = null,
    typography: WdsTypography? = null,
    dimensions: WdsDimensions? = null,
    shapes: WdsShapes? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = colors ?: when {
        darkTheme && businessMode -> WdsSemanticDarkColors()  // TODO: Create WdsSemanticBusinessDarkColors
        darkTheme && !businessMode -> WdsSemanticDarkColors()
        !darkTheme && businessMode -> WdsSemanticBusinessLightColors()
        else -> WdsSemanticLightColors()  // Consumer light mode
    }
    
    val typographyScheme = typography ?: WdsTypography()
    val dimensionsScheme = dimensions ?: WdsDimensions()
    val shapesScheme = shapes ?: WdsShapes()

    CompositionLocalProvider(
        LocalWdsColors provides colorScheme,
        LocalTypography provides typographyScheme,
        LocalDimensions provides dimensionsScheme,
        LocalShapes provides shapesScheme
    ) {
        // Set default text style to body1
        ProvideTextStyle(typographyScheme.body1) {
            content()
        }
    }
}

/**
 * WdsTheme object for accessing theme values
 */
object WdsTheme {
    /**
     * Retrieves the current [WdsColorScheme] at the call site's position in the hierarchy.
     */
    val colors: WdsColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalWdsColors.current
    
    /**
     * Retrieves the current [WdsTypography] at the call site's position in the hierarchy.
     */
    val typography: WdsTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
    
    /**
     * Retrieves the current [WdsDimensions] at the call site's position in the hierarchy.
     */
    val dimensions: WdsDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current
    
    /**
     * Retrieves the current [WdsShapes] at the call site's position in the hierarchy.
     */
    val shapes: WdsShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current
}
