package com.example.chatapp.features.broadcast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.data.local.entity.MessageType
import com.example.chatapp.features.chat.components.MessageItem
import com.example.chatapp.wds.components.WDSBottomBar
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.components.WdsComingSoonDialog
import com.example.chatapp.wds.theme.WdsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastDraftScreen(
    title: String,
    messageText: String,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    var showComingSoonDialog by remember { mutableStateOf(false) }

    if (showComingSoonDialog) {
        WdsComingSoonDialog(onDismissRequest = { showComingSoonDialog = false })
    }

    val mockMessage = remember(messageText) {
        MessageEntity(
            messageId = "draft",
            conversationId = "draft",
            senderId = "other",
            content = messageText,
            timestamp = System.currentTimeMillis(),
            messageType = MessageType.TEXT
        )
    }

    Scaffold(
        containerColor = colors.colorChatBackgroundWallpaper,
        topBar = {
            WDSTopBar(
                title = "Draft",
                subtitle = "Add an optional action button",
                onNavigateBack = onBackClick
            )
        },
        bottomBar = {
            WDSBottomBar(
                label = title,
                onClick = onNextClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
        ) {
            item(key = "draft_card") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensions.wdsSpacingDouble)
                ) {
                    MessageItem(
                        message = mockMessage,
                        isFromCurrentUser = false,
                        isGroupChat = false,
                        senderName = "",
                        senderAvatar = null,
                        showSenderInfo = false,
                        showTimestamp = false,
                        actionContent = {
                            MessageActionButton(
                                text = "Add button",
                                onClick = { showComingSoonDialog = true }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MessageActionButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val shapes = WdsTheme.shapes

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colors.colorBubbleSurfaceIncoming,
        shape = shapes.singlePlusBottom
    ) {
        Box(
            modifier = Modifier.padding(
                start = dimensions.wdsSpacingHalf,
                end = dimensions.wdsSpacingHalf,
                bottom = dimensions.wdsSpacingHalf
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                color = colors.colorSurfaceHighlight,
                shape = shapes.single,
                onClick = onClick
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = text,
                        style = typography.body2Emphasized,
                        color = colors.colorAccentEmphasized,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Broadcast Draft - Light")
@Composable
private fun BroadcastDraftScreenPreviewLight() {
    WdsTheme(darkTheme = false) {
        BroadcastDraftScreen(
            title = "New order",
            messageText = "Welcome, VIP clients! \uD83C\uDF31 Thanks for joining our community. Stay tuned for exclusive offers and gardening tips!"
        )
    }
}

@Preview(showBackground = true, name = "Broadcast Draft - Dark")
@Composable
private fun BroadcastDraftScreenPreviewDark() {
    WdsTheme(darkTheme = true) {
        BroadcastDraftScreen(
            title = "New order",
            messageText = "Welcome, VIP clients! \uD83C\uDF31 Thanks for joining our community. Stay tuned for exclusive offers and gardening tips!"
        )
    }
}
