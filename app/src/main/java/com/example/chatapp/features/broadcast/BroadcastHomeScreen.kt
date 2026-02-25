@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.broadcast

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.R
import com.example.chatapp.data.local.entity.ConversationEntity
import com.example.chatapp.wds.components.WDSFab
import com.example.chatapp.wds.components.WDSTabRow
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BroadcastHomeScreen(
    onNavigateBack: () -> Unit,
    onNewBroadcastClick: () -> Unit = {},
    onBroadcastClick: (String) -> Unit = {},
    viewModel: BroadcastHomeViewModel = hiltViewModel()
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = remember { listOf("Broadcasts", "Audiences") }

    val allConversations = uiState.broadcastConversations
    val sentBroadcastMessages = uiState.sentBroadcastMessages
    val creditsSent = sentBroadcastMessages.size
    val creditsTotal = 250
    val creditsProgress = if (creditsTotal > 0) creditsSent.toFloat() / creditsTotal.toFloat() else 0f

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Business broadcasts",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "More options",
                            tint = colors.colorContentDefault
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            WDSFab(
                onClick = onNewBroadcastClick,
                icon = Icons.Outlined.Add,
                contentDescription = "New broadcast"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MessageCreditsSection(
                creditsSent = creditsSent,
                creditsTotal = creditsTotal,
                progress = creditsProgress,
                dateRange = "Feb 12 - Mar 12"
            )

            WDSTabRow(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )

            when (selectedTabIndex) {
                0 -> BroadcastsTab(
                    sentMessages = sentBroadcastMessages,
                    onBroadcastClick = onBroadcastClick
                )
                1 -> AudiencesTab(
                    conversations = allConversations
                )
            }
        }
    }
}

@Composable
private fun MessageCreditsSection(
    creditsSent: Int,
    creditsTotal: Int,
    progress: Float,
    dateRange: String
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
            ) {
                Text(
                    text = "Message credits",
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault
                )
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Message credits info",
                    modifier = Modifier.size(16.dp),
                    tint = colors.colorContentDeemphasized
                )
            }
            Text(
                text = dateRange,
                style = typography.body2Emphasized,
                color = colors.colorContentDefault
            )
        }

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
        ) {
            Text(
                text = "$creditsSent",
                style = typography.headline2,
                color = colors.colorContentDefault
            )
            Text(
                text = "of $creditsTotal sent",
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                modifier = Modifier.padding(bottom = dimensions.wdsSpacingQuarter)
            )
        }

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(colors.colorPositiveDeemphasized)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(colors.colorPositive)
            )
        }
    }
}

@Composable
private fun BroadcastsTab(
    sentMessages: List<BroadcastSentMessage>,
    onBroadcastClick: (String) -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    if (sentMessages.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No broadcasts yet",
                style = typography.body1,
                color = colors.colorContentDeemphasized
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Sent",
                    style = typography.body2Emphasized,
                    color = colors.colorContentDeemphasized,
                    modifier = Modifier.padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSinglePlus
                    )
                )
            }

            itemsIndexed(
                items = sentMessages,
                key = { _, item -> item.message.messageId }
            ) { _, sentMessage ->
                BroadcastMessageRow(
                    sentMessage = sentMessage,
                    onClick = { onBroadcastClick(sentMessage.message.conversationId) }
                )
            }
        }
    }
}

@Composable
private fun BroadcastMessageRow(
    sentMessage: BroadcastSentMessage,
    onClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val sentDateText = remember(sentMessage.message.timestamp) {
        val formatter = SimpleDateFormat("MMM d", Locale.getDefault())
        "Sent ${formatter.format(Date(sentMessage.message.timestamp))}"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(WdsTheme.shapes.single)
                .background(colors.colorBroadcastAvatar),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_business_broadcast),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = colors.colorContentOnAccent
            )
        }

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = sentMessage.message.content,
                style = typography.body1,
                color = colors.colorContentDefault,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = sentMessage.conversationTitle ?: "Untitled broadcast",
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = dimensions.wdsSpacingQuarter)
            )
            Text(
                text = sentDateText,
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                modifier = Modifier.padding(top = dimensions.wdsSpacingQuarter)
            )
        }

        IconButton(
            onClick = { },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More options",
                tint = colors.colorContentDeemphasized
            )
        }
    }
}

@Composable
private fun AudiencesTab(conversations: List<ConversationEntity>) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    if (conversations.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No audiences yet",
                style = typography.body1,
                color = colors.colorContentDeemphasized
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                items = conversations,
                key = { _, conv -> conv.conversationId }
            ) { _, conversation ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSinglePlus
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(WdsTheme.shapes.single)
                            .background(colors.colorBroadcastAvatar),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_business_broadcast),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = colors.colorContentOnAccent
                        )
                    }

                    Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = conversation.title ?: "Untitled audience",
                            style = typography.body1,
                            color = colors.colorContentDefault,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${conversation.broadcastRecipientCount} recipients",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized,
                            modifier = Modifier.padding(top = dimensions.wdsSpacingQuarter)
                        )
                    }

                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "More options",
                            tint = colors.colorContentDeemphasized
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
private fun BroadcastHomeScreenPreviewLight() {
    WdsTheme(darkTheme = false) {
        BroadcastHomeScreen(onNavigateBack = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
private fun BroadcastHomeScreenPreviewDark() {
    WdsTheme(darkTheme = true) {
        BroadcastHomeScreen(onNavigateBack = {})
    }
}
