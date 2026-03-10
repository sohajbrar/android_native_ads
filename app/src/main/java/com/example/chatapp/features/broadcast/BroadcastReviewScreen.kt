package com.example.chatapp.features.broadcast

import androidx.compose.foundation.background
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.R
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonSize
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSContentRow
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastReviewScreen(
    messageText: String,
    recipientCount: Int,
    onBackClick: () -> Unit = {},
    onEditAudienceClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onSendNowClick: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Review",
                onNavigateBack = onBackClick
            )
        },
        bottomBar = {
            ReviewBottomBar(
                onScheduleClick = onScheduleClick,
                onSendNowClick = onSendNowClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            MessageContentRow(messageText = messageText)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.wdsSpacingDouble),
                contentAlignment = Alignment.Center
            ) {
                WDSDivider(
                    modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
                )
            }

            AudienceContentRow(
                recipientCount = recipientCount,
                onEditClick = onEditAudienceClick
            )

            AvailableCreditContentRow()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.wdsSpacingDouble),
                contentAlignment = Alignment.Center
            ) {
                WDSDivider(
                    modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
                )
            }

            CreditsUsedRow(creditsUsed = recipientCount.coerceAtMost(250))

            TotalRow(total = "$0")

            DisclaimerSection()

            Spacer(modifier = Modifier.weight(1f))

            TermsSection()
        }
    }
}

@Composable
private fun MessageContentRow(messageText: String) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val shapes = WdsTheme.shapes

    WDSContentRow(
        label = "Message",
        value = messageText,
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(dimensions.wdsTouchTargetCompact)
                    .clip(shapes.single)
                    .background(Color(0xFFD1D7D8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_business_broadcast),
                    contentDescription = null,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                    tint = colors.colorContentOnAccent
                )
            }
        }
    )
}

@Composable
private fun AudienceContentRow(
    recipientCount: Int,
    onEditClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val shapes = WdsTheme.shapes

    WDSContentRow(
        label = "Audience",
        value = "$recipientCount recipients",
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(dimensions.wdsTouchTargetCompact)
                    .clip(shapes.single)
                    .background(colors.colorSurfaceEmphasized),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Group,
                    contentDescription = null,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                    tint = colors.colorContentDefault
                )
            }
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit audience",
                modifier = Modifier
                    .size(dimensions.wdsIconSizeMedium)
                    .clickableWithSound { onEditClick() },
                tint = colors.colorContentDeemphasized
            )
        }
    )
}

@Composable
private fun AvailableCreditContentRow() {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions

    WDSContentRow(
        iconRes = R.drawable.ic_payments,
        label = "Available credit",
        value = "250 messages",
        trailingContent = {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Credit info",
                modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                tint = colors.colorContentDeemphasized
            )
        }
    )
}

@Composable
private fun CreditsUsedRow(creditsUsed: Int) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Credits used",
            style = typography.body2,
            color = colors.colorContentDeemphasized
        )
        Text(
            text = creditsUsed.toString(),
            style = typography.body2,
            color = colors.colorContentDeemphasized
        )
    }
}

@Composable
private fun TotalRow(total: String) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Total",
            style = typography.body1,
            color = colors.colorContentDefault
        )
        Text(
            text = total,
            style = typography.body1,
            color = colors.colorContentDefault
        )
    }
}

@Composable
private fun DisclaimerSection() {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val disclaimerText = buildAnnotatedString {
        append("To help keep WhatsApp users safe, your business broadcasts are ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.colorAlwaysBranded)) {
            append("subject to review by Meta")
        }
        append(" before you send them in WhatsApp chats. Meta can\u2019t access your messages once they\u2019re in a chat.")
    }

    Text(
        text = disclaimerText,
        style = typography.body3,
        color = colors.colorContentDefault,
        modifier = Modifier.padding(
            horizontal = dimensions.wdsSpacingDouble,
            vertical = dimensions.wdsSpacingSinglePlus
        )
    )
}

@Composable
private fun TermsSection() {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val termsText = buildAnnotatedString {
        append("By continuing, you agree to the ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.colorAlwaysBranded)) {
            append("Meta Terms for WhatsApp Marketing Messages Features")
        }
        append(" and ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.colorAlwaysBranded)) {
            append("Meta Privacy Policy")
        }
        append(".")
    }

    Text(
        text = termsText,
        style = typography.body3,
        color = colors.colorContentDefault,
        modifier = Modifier.padding(
            horizontal = dimensions.wdsSpacingDouble,
            vertical = dimensions.wdsSpacingSinglePlus
        )
    )
}

@Composable
private fun ReviewBottomBar(
    onScheduleClick: () -> Unit,
    onSendNowClick: () -> Unit
) {
    val dimensions = WdsTheme.dimensions
    val colors = WdsTheme.colors

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.colorSurfaceDefault
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingSinglePlus
                ),
            horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
        ) {
            WDSButton(
                onClick = onScheduleClick,
                text = "Schedule",
                icon = Icons.Outlined.Schedule,
                variant = WDSButtonVariant.OUTLINE,
                size = WDSButtonSize.LARGE,
                modifier = Modifier.weight(1f)
            )
            WDSButton(
                onClick = onSendNowClick,
                text = "Send now",
                icon = Icons.Outlined.PlayArrow,
                variant = WDSButtonVariant.FILLED,
                size = WDSButtonSize.LARGE,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, name = "Broadcast Review - Light")
@Composable
private fun BroadcastReviewScreenPreviewLight() {
    WdsTheme(darkTheme = false) {
        BroadcastReviewScreen(
            messageText = "Welcome, VIP clients! \uD83C\uDF31 Thanks for joining our community. Stay tuned for exclusive offers and gardening tips!",
            recipientCount = 303
        )
    }
}

@Preview(showBackground = true, name = "Broadcast Review - Dark")
@Composable
private fun BroadcastReviewScreenPreviewDark() {
    WdsTheme(darkTheme = true) {
        BroadcastReviewScreen(
            messageText = "Welcome, VIP clients! \uD83C\uDF31 Thanks for joining our community. Stay tuned for exclusive offers and gardening tips!",
            recipientCount = 303
        )
    }
}
