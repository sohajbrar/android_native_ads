@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.broadcast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.R
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.components.WdsDialog
import com.example.chatapp.wds.components.WdsDialogButton
import com.example.chatapp.wds.theme.BaseColors
import com.example.chatapp.wds.theme.WdsTheme

data class AudienceItem(
    val id: String,
    val name: String,
    val recipientCount: Int,
    val subtitle: String,
    @DrawableRes val iconResId: Int
)

@Composable
fun NewBusinessBroadcastScreen(
    onNavigateBack: () -> Unit = {},
    onNewAudienceClick: () -> Unit = {},
    onAudienceClick: (String) -> Unit = {}
) {
    val suggestedAudiences = remember {
        listOf(
            AudienceItem(
                id = "chats_from_ads",
                name = "Chats from ads",
                recipientCount = 150,
                subtitle = "30 days",
                iconResId = R.drawable.ic_business_broadcast
            ),
            AudienceItem(
                id = "active_chats",
                name = "Active chats",
                recipientCount = 35,
                subtitle = "Last 30 days",
                iconResId = R.drawable.ic_active_chats
            ),
            AudienceItem(
                id = "inactive_chats",
                name = "Inactive chats",
                recipientCount = 56,
                subtitle = "Inactive for 30 days",
                iconResId = R.drawable.ic_inactive_chats
            )
        )
    }

    val existingAudiences = remember {
        listOf(
            AudienceItem(
                id = "audience_1",
                name = "[Audience name]",
                recipientCount = 48,
                subtitle = "48 recipients",
                iconResId = R.drawable.ic_business_broadcast
            ),
            AudienceItem(
                id = "audience_2",
                name = "[Audience name]",
                recipientCount = 124,
                subtitle = "124 recipients",
                iconResId = R.drawable.ic_business_broadcast
            ),
            AudienceItem(
                id = "audience_3",
                name = "[Audience name]",
                recipientCount = 48,
                subtitle = "48 recipients",
                iconResId = R.drawable.ic_business_broadcast
            ),
            AudienceItem(
                id = "audience_4",
                name = "[Audience name]",
                recipientCount = 48,
                subtitle = "48 recipients",
                iconResId = R.drawable.ic_business_broadcast
            )
        )
    }

    NewBusinessBroadcastContent(
        suggestedAudiences = suggestedAudiences,
        existingAudiences = existingAudiences,
        onNavigateBack = onNavigateBack,
        onNewAudienceClick = onNewAudienceClick,
        onAudienceClick = onAudienceClick
    )
}

@Composable
private fun NewBusinessBroadcastContent(
    suggestedAudiences: List<AudienceItem>,
    existingAudiences: List<AudienceItem>,
    onNavigateBack: () -> Unit,
    onNewAudienceClick: () -> Unit,
    onAudienceClick: (String) -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    var showComingSoonDialog by remember { mutableStateOf(false) }

    if (showComingSoonDialog) {
        WdsDialog(
            title = "This feature is coming soon.",
            positiveButton = WdsDialogButton(
                text = "OK",
                onClick = { showComingSoonDialog = false }
            ),
            onDismissRequest = { showComingSoonDialog = false }
        )
    }

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "New business broadcast",
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // New audience action row
            item {
                NewAudienceRow(onClick = onNewAudienceClick)
            }

            // Suggested audiences section
            item {
                SectionHeader(title = "Suggested audiences")
            }

            items(
                items = suggestedAudiences,
                key = { it.id }
            ) { audience ->
                AudienceRow(
                    audience = audience,
                    showRecipientPrefix = true,
                    onClick = { showComingSoonDialog = true }
                )
            }

            // Existing audiences section
            item {
                SectionHeader(title = "Existing audiences")
            }

            items(
                items = existingAudiences,
                key = { it.id }
            ) { audience ->
                AudienceRow(
                    audience = audience,
                    showRecipientPrefix = false,
                    onClick = { showComingSoonDialog = true }
                )
            }
        }
    }
}

@Composable
private fun NewAudienceRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = colors.colorAccent,
            modifier = Modifier.size(dimensions.wdsAvatarMediumLarge)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Groups,
                    contentDescription = null,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                    tint = colors.colorAlwaysWhite
                )
            }
        }

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Text(
            text = "New audience",
            style = typography.body1,
            color = colors.colorContentDefault
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Text(
        text = title,
        style = typography.body2,
        color = colors.colorContentDeemphasized,
        modifier = modifier.padding(
            start = dimensions.wdsSpacingDouble,
            end = dimensions.wdsSpacingDouble,
            top = dimensions.wdsSpacingDouble,
            bottom = dimensions.wdsSpacingSingle
        )
    )
}

@Composable
private fun AudienceRow(
    audience: AudienceItem,
    showRecipientPrefix: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val subtitleText = if (showRecipientPrefix) {
        "${audience.recipientCount} recipients \u2022 ${audience.subtitle}"
    } else {
        "${audience.recipientCount} recipients"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = BaseColors.wdsCoolGray250,
            modifier = Modifier.size(dimensions.wdsAvatarMediumLarge)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(audience.iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                    tint = colors.colorContentOnAccent
                )
            }
        }

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
        ) {
            Text(
                text = audience.name,
                style = typography.body1,
                color = colors.colorContentDefault,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = subtitleText,
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, name = "New Business Broadcast - Light")
@Composable
private fun NewBusinessBroadcastScreenPreviewLight() {
    WdsTheme(darkTheme = false) {
        NewBusinessBroadcastScreen()
    }
}

@Preview(showBackground = true, name = "New Business Broadcast - Dark")
@Composable
private fun NewBusinessBroadcastScreenPreviewDark() {
    WdsTheme(darkTheme = true) {
        NewBusinessBroadcastScreen()
    }
}
