package com.example.chatapp.wds.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Text styles definition for WhatsApp Design System.
 *
 * Example usage: WdsTheme.typography.headline1
 *
 * @property largeTitle1 Used for hero/primary navigation title, full page headlines (32sp)
 * @property largeTitle2 Used for hero/primary navigation title, full page headlines (28sp)
 * @property headline1 Used for title bar actions (24sp)
 * @property headline2 Used for title bar headlines, dialog headlines (22sp)
 * @property body1 Used for bottom sheet header actions (16sp)
 * @property body1Emphasized Used for bottom sheet headlines (16sp, Medium weight)
 * @property body2 Used for footnotes, metadata (14sp)
 * @property body2Emphasized Used for footnote actions, tappable metadata (14sp, Medium weight)
 * @property body3 Used for body text (12sp)
 * @property body3Emphasized Used for body text actions (12sp, Medium weight)
 * @property chatListTitle Used for chat list item primary line (17sp, Medium weight)
 * @property chatHeaderTitle Used for chat header title and collapsed chat info title (18sp, Regular weight)
 */
@Immutable
class WdsTypography(
    val largeTitle1: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontLargeTitle1Size,
            lineHeight = BaseDimensions.wdsFontLargeTitle1LineHeight,
            letterSpacing = BaseDimensions.wdsFontLargeTitle1LetterSpacing,
        ),
    val largeTitle2: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontLargeTitle2Size,
            lineHeight = BaseDimensions.wdsFontLargeTitle2LineHeight,
            letterSpacing = BaseDimensions.wdsFontLargeTitle2LetterSpacing,
        ),
    val headline1: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontHeadline1Size,
            lineHeight = BaseDimensions.wdsFontHeadline1LineHeight,
            letterSpacing = BaseDimensions.wdsFontHeadline1LetterSpacing,
        ),
    val headline2: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontHeadline2Size,
            lineHeight = BaseDimensions.wdsFontHeadline2LineHeight,
            letterSpacing = BaseDimensions.wdsFontHeadline2LetterSpacing,
        ),
    val body1: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody1Size,
            lineHeight = BaseDimensions.wdsFontBody1LineHeight,
            letterSpacing = BaseDimensions.wdsFontBody1LetterSpacing,
        ),
    val body1Emphasized: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody1Size,
            lineHeight = BaseDimensions.wdsFontBody1LineHeight,
            letterSpacing = BaseDimensions.wdsFontBody1EmphasizedLetterSpacing,
        ),
    val body1InlineLink: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody1Size,
            lineHeight = BaseDimensions.wdsFontBody1LineHeight,
            letterSpacing = BaseDimensions.wdsFontBody1EmphasizedLetterSpacing,
        ),
    val body2: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody2Size,
            lineHeight = BaseDimensions.wdsFontBody2LineHeight,
            letterSpacing = BaseDimensions.wdsFontBody2LetterSpacing,
        ),
    val body2Emphasized: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody2Size,
            lineHeight = BaseDimensions.wdsFontBody2LineHeight,
            letterSpacing = BaseDimensions.wdsFontBody2EmphasizedLetterSpacing,
        ),
    val body2InlineLink: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody2Size,
            lineHeight = BaseDimensions.wdsFontBody2LineHeight,
            letterSpacing = BaseDimensions.wdsFontBody2EmphasizedLetterSpacing,
        ),
    val body3: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody3Size,
            lineHeight = BaseDimensions.wdsFontBody3LineHeight,
            letterSpacing = BaseDimensions.wdsFontBody3LetterSpacing,
        ),
    val body3Emphasized: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody3Size,
            lineHeight = BaseDimensions.wdsFontBody3LineHeight,
            letterSpacing = BaseDimensions.wdsFontBody3EmphasizedLetterSpacing,
        ),
    val body3InlineLink: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody3Size,
            lineHeight = BaseDimensions.wdsFontBody3LineHeight,
            letterSpacing = BaseDimensions.wdsFontBody3EmphasizedLetterSpacing,
        ),
    val chatBody1: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody1Size,
        ),
    val chatBody1Emphasized: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody1Size,
        ),
    val chatBody2: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody2Size,
        ),
    val chatBody2Emphasized: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody2Size,
        ),
    val chatBody3: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody3Size,
        ),
    val chatBody3Emphasized: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontBody3Size,
        ),
    val chatListTitle: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontChatListTitleSize,
            lineHeight = BaseDimensions.wdsFontChatListTitleLineHeight,
            letterSpacing = BaseDimensions.wdsFontChatListTitleLetterSpacing,
        ),
    val chatHeaderTitle: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            fontSize = BaseDimensions.wdsFontChatHeaderTitleSize,
            lineHeight = BaseDimensions.wdsFontChatHeaderTitleLineHeight,
            letterSpacing = BaseDimensions.wdsFontChatHeaderTitleLetterSpacing,
        ),
)

/**
 * CompositionLocal used to pass [WdsTypography] down the composition hierarchy.
 *
 * This value is set as part of [WdsTheme]. Ensure [WdsTheme] is included in your composition
 * hierarchy to use this (automatically included if using [WdsTheme] as root in the surface).
 *
 * To get the current value of this CompositionLocal, use [WdsTheme.typography].
 */
val LocalTypography: ProvidableCompositionLocal<WdsTypography> = staticCompositionLocalOf {
    error(
        "CompositionLocal not present for LocalTypography. This is likely because WdsTheme has not " +
                "been included in your Compose hierarchy."
    )
}

