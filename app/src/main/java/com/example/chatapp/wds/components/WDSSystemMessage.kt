package com.example.chatapp.wds.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.wds.theme.WdsTheme
import com.example.chatapp.wds.theme.WhatsAppIconsFont

/**
 * WDS System Message Component
 *
 * Displays system messages in chat with proper WhatsApp styling.
 * Matches Figma design specifications exactly.
 *
 * Icon Unicode Characters:
 * - Lock (E2EE): '\uE1D0' - Test range: U+E000-U+F8FF
 * - Shield (Business): '\uE1D1' - Test range: U+E000-U+F8FF
 *
 * Note: If icons don't render correctly, try these test characters:
 * Lock candidates: \uE1D0, \uE897, \uE8A8, or space ' '
 * Shield candidates: \uE1D1, \uE8A9, \uE0A2, or space ' '
 */

enum class WDSSystemMessageType {
    SECURITY_E2EE,       // Yellow background, lock icon
    SECURITY_BUSINESS,   // Green background, shield icon
    GROUP_YOU_JOINED,    // White background, no icon
    GROUP_PERSON_ADDED   // White background, no icon
}

@Composable
fun WDSSystemMessage(
    type: WDSSystemMessageType,
    personName: String? = null,
    groupName: String? = null,
    onLearnMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Cache theme lookups
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val shapes = WdsTheme.shapes
    val typography = WdsTheme.typography

    // Determine styling based on type
    val backgroundColor = when (type) {
        WDSSystemMessageType.SECURITY_E2EE -> colors.colorBubbleSurfaceE2e
        WDSSystemMessageType.SECURITY_BUSINESS -> colors.colorBubbleSurfaceBusiness
        WDSSystemMessageType.GROUP_YOU_JOINED,
        WDSSystemMessageType.GROUP_PERSON_ADDED -> colors.colorBubbleSurfaceSystem
    }

    val hasIcon = type == WDSSystemMessageType.SECURITY_E2EE ||
                  type == WDSSystemMessageType.SECURITY_BUSINESS

    val verticalPadding = if (hasIcon) {
        dimensions.wdsSpacingHalf // 4dp for security messages with icon
    } else {
        dimensions.wdsSpacingSingle // 8dp for group messages
    }

    val content = getMessageContent(type, personName, groupName)
    val hasLearnMore = type == WDSSystemMessageType.SECURITY_E2EE ||
                      type == WDSSystemMessageType.SECURITY_BUSINESS

    if (hasIcon) {
        // Security messages with icon and background - full width with outer padding
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.wdsSpacingTriple) // 24dp left and right padding
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = backgroundColor,
                shape = shapes.single, // 8dp corner radius
                shadowElevation = dimensions.wdsElevationSubtle
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensions.wdsSpacingSinglePlus, // 12dp
                            vertical = verticalPadding
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Icon character to prepend to text
                    // TODO: Find correct unicode values in WhatsAppIconsNew font
                    // Temporarily using visible placeholder: ● (bullet)
                    val iconChar = when (type) {
                        WDSSystemMessageType.SECURITY_E2EE -> '' // TODO: Replace with lock icon unicode
                        WDSSystemMessageType.SECURITY_BUSINESS -> '' // TODO: Replace with shield icon unicode
                        else -> ' '
                    }

                    // Message text with icon inline
                    if (hasLearnMore) {
                        ClickableMessageTextWithIcon(
                            iconChar = iconChar,
                            text = content,
                            textColor = colors.colorContentDeemphasized,
                            linkColor = colors.colorContentDeemphasized,
                            onLearnMoreClick = onLearnMoreClick
                        )
                    } else {
                        TextWithIcon(
                            iconChar = iconChar,
                            text = content,
                            textColor = colors.colorContentDeemphasized,
                            typography = typography
                        )
                    }
                }
            }
        }
    } else {
        // Group messages with background - centered
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = colors.colorBubbleSurfaceSystem,
                shape = shapes.single // 8dp corner radius
            ) {
                Text(
                    text = content,
                    style = typography.body3.copy(
                        shadow = null // Explicitly remove any text shadow
                    ),
                    color = colors.colorContentDeemphasized,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        horizontal = dimensions.wdsSpacingSinglePlus, // 12dp
                        vertical = verticalPadding
                    )
                )
            }
        }
    }
}

@Composable
private fun TextWithIcon(
    iconChar: Char,
    text: String,
    textColor: androidx.compose.ui.graphics.Color,
    typography: com.example.chatapp.wds.tokens.WdsTypography,
    modifier: Modifier = Modifier
) {
    val annotatedText = buildAnnotatedString {
        // Add icon with icon font
        withStyle(
            SpanStyle(
                fontFamily = WhatsAppIconsFont,
                fontSize = typography.body2.fontSize, // 14sp from body2
                color = textColor
            )
        ) {
            append(iconChar)
            append(" ") // Space after icon
        }

        // Add text
        withStyle(
            SpanStyle(
                fontSize = typography.body3.fontSize, // 12sp from body3
                fontWeight = FontWeight.Normal,
                letterSpacing = typography.body3.letterSpacing, // 0.2sp from body3
                color = textColor
            )
        ) {
            append(text)
        }
    }

    Text(
        text = annotatedText,
        style = typography.body3.copy(
            lineHeight = typography.body3.lineHeight // 16sp from body3
        ),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
private fun ClickableMessageTextWithIcon(
    iconChar: Char,
    text: String,
    textColor: androidx.compose.ui.graphics.Color,
    linkColor: androidx.compose.ui.graphics.Color,
    onLearnMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val typography = WdsTheme.typography

    val annotatedText = buildAnnotatedString {
        // Add icon with icon font
        withStyle(
            SpanStyle(
                fontFamily = WhatsAppIconsFont,
                fontSize = typography.body2.fontSize, // 14sp from body2
                color = textColor
            )
        ) {
            append(iconChar)
            append(" ") // Space after icon
        }

        // Split text by "Learn more"
        val parts = text.split("Learn more")

        if (parts.size == 2) {
            // Regular text before "Learn more"
            withStyle(
                SpanStyle(
                    fontSize = typography.body3.fontSize, // 12sp from body3
                    fontWeight = FontWeight.Normal,
                    letterSpacing = typography.body3.letterSpacing, // 0.2sp from body3
                    color = textColor
                )
            ) {
                append(parts[0])
            }

            // "Learn more" link
            pushStringAnnotation(tag = "LEARN_MORE", annotation = "learn_more")
            withStyle(
                SpanStyle(
                    fontSize = typography.body3.fontSize, // 12sp from body3
                    fontWeight = FontWeight.Medium,
                    letterSpacing = typography.body3Emphasized.letterSpacing, // 0.25sp from body3Emphasized
                    color = linkColor
                )
            ) {
                append("Learn more")
            }
            pop()

            // Text after "Learn more" (if any)
            if (parts[1].isNotEmpty()) {
                withStyle(
                    SpanStyle(
                        fontSize = typography.body3.fontSize, // 12sp from body3
                        fontWeight = FontWeight.Normal,
                        letterSpacing = typography.body3.letterSpacing, // 0.2sp from body3
                        color = textColor
                    )
                ) {
                    append(parts[1])
                }
            }
        } else {
            // No "Learn more" found, render as plain text
            withStyle(
                SpanStyle(
                    fontSize = typography.body3.fontSize, // 12sp from body3
                    fontWeight = FontWeight.Normal,
                    letterSpacing = typography.body3.letterSpacing, // 0.2sp from body3
                    color = textColor
                )
            ) {
                append(text)
            }
        }
    }

    Text(
        text = annotatedText,
        style = typography.body3.copy(
            lineHeight = typography.body3.lineHeight // 16sp from body3
        ),
        textAlign = TextAlign.Center,
        modifier = modifier.clickable {
            // Check if "Learn more" was clicked
            // For now, just trigger the callback on any click
            onLearnMoreClick()
        }
    )
}

private fun getMessageContent(
    type: WDSSystemMessageType,
    personName: String?,
    groupName: String?
): String {
    return when (type) {
        WDSSystemMessageType.SECURITY_E2EE ->
            "Messages and calls are end-to-end encrypted. No one outside of this chat, not even WhatsApp, can read or listen to them. Learn more"

        WDSSystemMessageType.SECURITY_BUSINESS ->
            "This business uses a secure service from Meta to manage this chat. Learn more"

        WDSSystemMessageType.GROUP_YOU_JOINED ->
            "You joined via invite link"

        WDSSystemMessageType.GROUP_PERSON_ADDED ->
            "${personName ?: "Someone"} was added"
    }
}

// Preview functions
@Preview(showBackground = true, name = "E2EE Light")
@Composable
fun WDSSystemMessageE2EEPreviewLight() {
    WdsTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WDSSystemMessage(
                type = WDSSystemMessageType.SECURITY_E2EE
            )
        }
    }
}

@Preview(showBackground = true, name = "E2EE Dark")
@Composable
fun WDSSystemMessageE2EEPreviewDark() {
    WdsTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WDSSystemMessage(
                type = WDSSystemMessageType.SECURITY_E2EE
            )
        }
    }
}

@Preview(showBackground = true, name = "Business Light")
@Composable
fun WDSSystemMessageBusinessPreviewLight() {
    WdsTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WDSSystemMessage(
                type = WDSSystemMessageType.SECURITY_BUSINESS
            )
        }
    }
}

@Preview(showBackground = true, name = "Business Dark")
@Composable
fun WDSSystemMessageBusinessPreviewDark() {
    WdsTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WDSSystemMessage(
                type = WDSSystemMessageType.SECURITY_BUSINESS
            )
        }
    }
}

@Preview(showBackground = true, name = "Group You Joined Light")
@Composable
fun WDSSystemMessageGroupYouJoinedPreviewLight() {
    WdsTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WDSSystemMessage(
                type = WDSSystemMessageType.GROUP_YOU_JOINED,
                groupName = "Family Chat"
            )
        }
    }
}

@Preview(showBackground = true, name = "Group You Joined Dark")
@Composable
fun WDSSystemMessageGroupYouJoinedPreviewDark() {
    WdsTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WDSSystemMessage(
                type = WDSSystemMessageType.GROUP_YOU_JOINED,
                groupName = "Family Chat"
            )
        }
    }
}

@Preview(showBackground = true, name = "Group Person Added Light")
@Composable
fun WDSSystemMessageGroupPersonAddedPreviewLight() {
    WdsTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WDSSystemMessage(
                type = WDSSystemMessageType.GROUP_PERSON_ADDED,
                personName = "Sue"
            )
        }
    }
}

@Preview(showBackground = true, name = "Group Person Added Dark")
@Composable
fun WDSSystemMessageGroupPersonAddedPreviewDark() {
    WdsTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WDSSystemMessage(
                type = WDSSystemMessageType.GROUP_PERSON_ADDED,
                personName = "Sue"
            )
        }
    }
}
