package com.example.chatapp.features.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.features.chat.components.*
import com.example.chatapp.wds.theme.WdsTheme
import kotlinx.coroutines.delay

@Composable
fun ChatScreen(
    conversationId: String,
    onBackClick: () -> Unit,
    onChatInfoClick: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val messages by viewModel.messages.collectAsState()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Track keyboard state
    val imeInsets = WindowInsets.ime
    val density = LocalDensity.current
    val imeHeight = with(density) { imeInsets.getBottom(this).toDp() }
    val isKeyboardOpen = imeHeight > 0.dp

    // Load conversation details
    LaunchedEffect(conversationId) {
        viewModel.loadConversation(conversationId)
    }

    // Mark messages as read when leaving the screen (works for back button, gestures, etc.)
    DisposableEffect(conversationId) {
        onDispose {
            viewModel.markMessagesAsRead()
        }
    }

    // Track if this is the initial load
    var isInitialLoad by remember { mutableStateOf(true) }

    // Scroll to first unread or bottom when messages load
    LaunchedEffect(messages, uiState.unreadCount) {
        if (messages.isNotEmpty() && isInitialLoad) {
            isInitialLoad = false

            val firstUnreadIndex = if (uiState.firstUnreadMessageId != null) {
                messages.indexOfFirst { it.messageId == uiState.firstUnreadMessageId }
            } else {
                -1
            }

            if (firstUnreadIndex >= 0 && uiState.unreadCount > 0) {
                // Has unread messages - scroll to first unread
                delay(50)
                val targetIndex = maxOf(0, firstUnreadIndex - 2)
                listState.scrollToItem(
                    index = targetIndex,
                    scrollOffset = 0
                )
            } else {
                // No unread messages (read chat) - start at bottom instantly
                listState.scrollToItem(messages.size - 1)
            }
        }
    }

    // Scroll to bottom when user sends a message
    LaunchedEffect(messages.size) {
        if (!isInitialLoad && messages.isNotEmpty()) {
            val lastMessage = messages.last()
            if (lastMessage.senderId == viewModel.currentUserId) {
                scope.launch {
                    delay(100)
                    listState.animateScrollToItem(messages.size - 1)
                }
            }
        }
    }

    // Main content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WdsTheme.colors.colorChatBackgroundWallpaper)
    ) {
        ChatContentView(
            uiState = uiState,
            messages = messages,
            listState = listState,
            isKeyboardOpen = isKeyboardOpen,
            onBackClick = onBackClick,
            viewModel = viewModel,
            onHeaderClick = onChatInfoClick,
            showInsightsButton = false,
            onInsightsClick = {},
            hideBackButton = false,
            removeStatusBarPadding = false,
            modifier = Modifier.fillMaxSize()
        )
    }
}
