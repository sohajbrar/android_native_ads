package com.example.chatapp.features.chat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import com.example.chatapp.wds.theme.WdsTheme
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.features.chat.*
import com.example.chatapp.features.chat.components.*
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatContentView(
    uiState: ChatUiState,
    messages: List<MessageEntity>,
    listState: LazyListState,
    isKeyboardOpen: Boolean,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel,
    onHeaderClick: () -> Unit = {}, // New parameter for variant selector
    showInsightsButton: Boolean = false,
    onInsightsClick: () -> Unit = {},
    hideBackButton: Boolean = false,
    removeStatusBarPadding: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Cache theme lookups for performance
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    
    // Check if currently at bottom
    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.totalItemsCount == 0) {
                true
            } else {
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem != null && lastVisibleItem.index >= layoutInfo.totalItemsCount - 1
            }
        }
    }
    
    // Track keyboard state and scroll position
    var previousKeyboardState by remember { mutableStateOf(false) }
    var wasAtBottom by remember { mutableStateOf(false) }
    var savedScrollIndex by remember { mutableStateOf(0) }
    var savedScrollOffset by remember { mutableStateOf(0) }
    
    // Auto-scroll to bottom when new messages arrive (if already at bottom)
    LaunchedEffect(messages.size) {
        if (isAtBottom && messages.isNotEmpty() && !isKeyboardOpen) {
            // Only auto-scroll if keyboard is not open
            delay(100)
            listState.animateScrollToItem(
                index = messages.size - 1,
                scrollOffset = 0
            )
        }
    }
    
    // Handle keyboard state changes
    LaunchedEffect(isKeyboardOpen) {
        if (isKeyboardOpen != previousKeyboardState && messages.isNotEmpty()) {
            val firstVisibleItem = listState.firstVisibleItemIndex
            val firstVisibleItemOffset = listState.firstVisibleItemScrollOffset
            
            if (isKeyboardOpen) {
                // Keyboard is opening - check bottom state BEFORE any scrolling
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = listState.layoutInfo.totalItemsCount
                
                // Consider "at bottom" if viewing last 2 messages (more tolerant)
                val atBottomNow = lastVisibleItemIndex >= totalItemsCount - 2
                wasAtBottom = atBottomNow
                
                if (atBottomNow) {
                    // At bottom - scroll to keep bottom message visible above keyboard
                    // Wait for keyboard animation to complete before scrolling
                    delay(150)  // Balanced delay for keyboard animation
                    listState.animateScrollToItem(
                        index = messages.size - 1,
                        scrollOffset = 0
                    )
                } else {
                    // Not at bottom - save and maintain exact position
                    savedScrollIndex = firstVisibleItem
                    savedScrollOffset = firstVisibleItemOffset
                    
                    // Aggressively maintain position during keyboard animation
                    repeat(10) {
                        delay(30)
                        // Only maintain if we weren't at bottom
                        if (!wasAtBottom) {
                            listState.scrollToItem(
                                index = savedScrollIndex,
                                scrollOffset = savedScrollOffset
                            )
                        }
                    }
                }
            } else {
                // Keyboard is closing
                if (!wasAtBottom) {
                    // Was scrolled up - maintain position
                    repeat(5) {
                        delay(30)
                        listState.scrollToItem(
                            index = savedScrollIndex,
                            scrollOffset = savedScrollOffset
                        )
                    }
                } else {
                    // Was at bottom - return to normal bottom position
                    delay(150)  // Wait for keyboard close animation
                    listState.animateScrollToItem(
                        index = messages.size - 1,
                        scrollOffset = 0  // Normal position when keyboard closed
                    )
                }
            }
        }
        
        previousKeyboardState = isKeyboardOpen
    }
    
    Box(
        modifier = modifier
            .background(WdsTheme.colors.colorChatBackgroundWallpaper)
    ) {
        // Fixed background pattern that doesn't move with keyboard
        Image(
            painter = painterResource(id = R.drawable.chat_background_old),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                WdsTheme.colors.colorChatForegroundWallpaper
            )
        )
        
        // Debug logging for padding state
        android.util.Log.d("ChatContentView", "removeStatusBarPadding: $removeStatusBarPadding")

        Scaffold(
            modifier = if (removeStatusBarPadding) {
                android.util.Log.d("ChatContentView", "Applying custom insets - removing top padding")
                Modifier.windowInsetsPadding(
                    WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                )
            } else {
                android.util.Log.d("ChatContentView", "Using default Scaffold padding")
                Modifier
            },
            topBar = {
                ChatTopBar(
                    conversationTitle = uiState.conversationTitle,
                    conversationAvatar = uiState.conversationAvatar,
                    isOnline = uiState.isOnline,
                    lastSeenText = uiState.lastSeen,
                    isGroupChat = uiState.isGroupChat,
                    onBackClick = onBackClick,
                    onVideoCallClick = { /* TODO */ },
                    onAudioCallClick = { /* TODO */ },
                    onMoreClick = { /* TODO */ },
                    onHeaderClick = onHeaderClick, // Pass through header click
                    showInsightsButton = showInsightsButton,
                    onInsightsClick = onInsightsClick,
                    hideBackButton = hideBackButton
                )
            },
            bottomBar = {
                ChatComposer(
                    value = uiState.composerText,
                    onValueChange = viewModel::updateComposerText,
                    onSendClick = {
                        viewModel.sendMessage()
                    },
                    onAttachClick = { /* TODO */ },
                    onCameraClick = { /* TODO */ },
                    onMicClick = { /* TODO */ }
                )
            },
            containerColor = Color.Transparent,
            contentColor = colors.colorContentDefault
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // The unread messages are determined in the ViewModel based on lastViewedAt
                val firstUnreadIncomingMessage = if (uiState.firstUnreadMessageId != null) {
                    messages.firstOrNull { it.messageId == uiState.firstUnreadMessageId }
                } else {
                    null
                }
                
                val firstUnreadIndex = if (firstUnreadIncomingMessage != null) {
                    messages.indexOfFirst { it.messageId == firstUnreadIncomingMessage.messageId }
                } else {
                    -1
                }
                
                // Use the unread count from the ViewModel which is based on lastViewedAt
                val actualUnreadCountBelowPill = uiState.unreadCount
                
                // Add immediate logging to check messages
                LaunchedEffect(messages) {
                    android.util.Log.d("ChatContentView", "Messages loaded: ${messages.size} messages")
                }
                
                // Track the oldest visible message date for the transient header
                val oldestVisibleMessageDate: Long? by remember(messages) {
                    derivedStateOf {
                        if (messages.isEmpty()) {
                            null
                        } else {
                            val visibleItems = listState.layoutInfo.visibleItemsInfo
                            if (visibleItems.isEmpty()) {
                                null
                            } else {
                                val firstVisibleIndex = visibleItems.first().index
                                
                                // Account for header items in group chats
                                val headerItemsCount = if (uiState.isGroupChat) 2 else 0
                                val messageIndex = firstVisibleIndex - headerItemsCount
                                
                                when {
                                    messageIndex >= 0 && messageIndex < messages.size -> {
                                        messages[messageIndex].timestamp
                                    }
                                    firstVisibleIndex < headerItemsCount && messages.isNotEmpty() -> {
                                        // We're looking at header items, use first message
                                        messages.first().timestamp
                                    }
                                    else -> null
                                }
                            }
                        }
                    }
                }
                
                // Track if user is actively scrolling
                val isScrolling by remember {
                    derivedStateOf {
                        listState.isScrollInProgress
                    }
                }
                
                // State for managing date header visibility with timer
                var showDateHeaderState by remember { mutableStateOf(false) }
                var hideTimerJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }
                
                // Simple scroll detection - show header when scrolling at all and not at bottom
                LaunchedEffect(isScrolling, isAtBottom) {
                    if (isScrolling && !isAtBottom) {
                        // User is scrolling and not at bottom - show header
                        showDateHeaderState = true
                        // Cancel any existing hide timer
                        hideTimerJob?.cancel()
                        hideTimerJob = null
                    } else if (!isScrolling && showDateHeaderState) {
                        // User stopped scrolling, start hide timer
                        if (hideTimerJob == null) {
                            hideTimerJob = launch {
                                delay(3000) // Wait 3 seconds
                                showDateHeaderState = false
                                hideTimerJob = null
                            }
                        }
                    }
                    
                    // Hide immediately when at bottom
                    if (isAtBottom) {
                        hideTimerJob?.cancel()
                        hideTimerJob = null
                        showDateHeaderState = false
                    }
                }
                
                // Show date header when state is true and has valid date
                val showDateHeader = showDateHeaderState && oldestVisibleMessageDate != null
                
                // Add debug logging
                LaunchedEffect(showDateHeaderState, oldestVisibleMessageDate, isScrolling) {
                    android.util.Log.d("ChatContentView", "DateHeader Debug - State: $showDateHeaderState, Date: $oldestVisibleMessageDate, isScrolling: $isScrolling, isAtBottom: $isAtBottom")
                }
                
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 0.dp,
                        end = 0.dp,
                        top = WdsTheme.dimensions.wdsSpacingSingle,
                        bottom = WdsTheme.dimensions.wdsSpacingSingle
                    ),
                    verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingQuarter)
                ) {
                    // Add security message for all chats (E2EE or Business)
                    item {
                        com.example.chatapp.wds.components.WDSSystemMessage(
                            type = if (uiState.isBusinessChat) {
                                com.example.chatapp.wds.components.WDSSystemMessageType.SECURITY_BUSINESS
                            } else {
                                com.example.chatapp.wds.components.WDSSystemMessageType.SECURITY_E2EE
                            },
                            onLearnMoreClick = { /* TODO: Open info dialog */ },
                            modifier = Modifier.padding(bottom = WdsTheme.dimensions.wdsSpacingSingle)
                        )
                    }

                    // Add "You joined" message for group chats
                    if (uiState.isGroupChat) {
                        item {
                            com.example.chatapp.wds.components.WDSSystemMessage(
                                type = com.example.chatapp.wds.components.WDSSystemMessageType.GROUP_YOU_JOINED,
                                groupName = uiState.conversationTitle,
                                modifier = Modifier.padding(bottom = WdsTheme.dimensions.wdsSpacingSingle)
                            )
                        }
                    }

                    // Add messages with proper pill placement
                    itemsIndexed(
                        items = messages,
                        key = { _, message -> message.messageId }
                    ) { index, message ->
                        val senderInfo = uiState.participantInfo[message.senderId]
                        val previousMessage = if (index > 0) messages[index - 1] else null
                        val isFirstInSequence = previousMessage == null || previousMessage.senderId != message.senderId
                        val isFromCurrentUser = message.senderId == viewModel.currentUserId
                        val previousIsFromCurrentUser = previousMessage?.senderId == viewModel.currentUserId
                        
                        // Add extra spacing conditions
                        val needsExtraSpacing = when {
                            previousMessage == null -> false
                            // Between incoming and outgoing messages
                            isFromCurrentUser != previousIsFromCurrentUser -> true
                            // Between incoming messages from different senders in groups
                            !isFromCurrentUser && uiState.isGroupChat && isFirstInSequence -> true
                            else -> false
                        }
                        
                        Column {
                            // Add unread pill before the first unread incoming message
                            if (index == firstUnreadIndex && actualUnreadCountBelowPill > 0) {
                                UnreadMessagesPill(
                                    count = actualUnreadCountBelowPill,
                                    onClick = {
                                        // TODO: Show summary dialog
                                        scope.launch {
                                            listState.animateScrollToItem(
                                                index = maxOf(0, firstUnreadIndex),
                                                scrollOffset = -100
                                            )
                                        }
                                    },
                                    modifier = Modifier.padding(bottom = WdsTheme.dimensions.wdsSpacingSingle)
                                )
                            }

                            if (needsExtraSpacing) {
                                Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingHalfPlus))
                            }

                            // Handle SYSTEM messages separately
                            if (message.messageType == com.example.chatapp.data.local.entity.MessageType.SYSTEM) {
                                // Parse content to determine system message type
                                val systemMessageType = when {
                                    message.content.contains("was added") -> {
                                        // Extract person name (format: "Name was added")
                                        val personName = message.content.substringBefore(" was added")
                                        com.example.chatapp.wds.components.WDSSystemMessage(
                                            type = com.example.chatapp.wds.components.WDSSystemMessageType.GROUP_PERSON_ADDED,
                                            personName = personName,
                                            modifier = Modifier.padding(vertical = WdsTheme.dimensions.wdsSpacingQuarter)
                                        )
                                    }
                                    else -> null
                                }

                                systemMessageType
                            } else {
                                // Regular message
                                MessageItem(
                                    message = message,
                                    isFromCurrentUser = isFromCurrentUser,
                                    isGroupChat = uiState.isGroupChat,
                                    senderName = senderInfo?.displayName ?: "Unknown",
                                    senderAvatar = senderInfo?.avatarUrl,
                                    showSenderInfo = isFirstInSequence
                                )
                            }
                        }
                    }
                }
                
                // Transient date header - positioned 8dp below the chat header (centered horizontally)
                AnimatedVisibility(
                    visible = showDateHeader,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = WdsTheme.dimensions.wdsSpacingSingle) // 8dp below the header
                ) {
                    DateHeader(
                        timestamp = oldestVisibleMessageDate ?: System.currentTimeMillis(),
                        modifier = Modifier.padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble)
                    )
                }
                
                // Scroll to bottom FAB
                AnimatedVisibility(
                    visible = !isAtBottom,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = WdsTheme.dimensions.wdsSpacingSinglePlus,
                            bottom = WdsTheme.dimensions.wdsSpacingSinglePlus // 12dp above the composer container
                        )
                ) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                if (messages.isNotEmpty()) {
                                    listState.animateScrollToItem(messages.size - 1)
                                }
                            }
                        },
                        containerColor = WdsTheme.colors.colorBubbleSurfaceIncoming,
                        contentColor = WdsTheme.colors.colorContentDeemphasized,
                        modifier = Modifier
                            .size(32.dp) // Custom FAB size for compact scroll button
                            .shadow(
                                elevation = WdsTheme.dimensions.wdsElevationSubtle,
                                spotColor = WdsTheme.colors.colorBubbleSurfaceOverlay,
                                ambientColor = WdsTheme.colors.colorBubbleSurfaceOverlay,
                                shape = CircleShape
                            )
                            .shadow(
                                elevation = WdsTheme.dimensions.wdsElevationNone,
                                spotColor = WdsTheme.colors.colorBubbleSurfaceOverlay,
                                ambientColor = WdsTheme.colors.colorBubbleSurfaceOverlay,
                                shape = CircleShape
                            ),
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = WdsTheme.dimensions.wdsElevationNone,
                            pressedElevation = WdsTheme.dimensions.wdsElevationNone
                        )
                    ) {
                        // Custom double chevron icon
                        val iconSize = dimensions.wdsIconSizeMediumSmall
                        val doubleChevronIcon = ImageVector.Builder(
                            defaultWidth = iconSize,
                            defaultHeight = iconSize,
                            viewportWidth = 18f,
                            viewportHeight = 18f
                        ).path(
                            fill = SolidColor(WdsTheme.colors.colorContentDeemphasized),
                            stroke = null,
                            strokeLineWidth = 0f,
                            strokeLineCap = StrokeCap.Butt,
                            strokeLineJoin = StrokeJoin.Miter,
                            strokeLineMiter = 4f,
                            pathFillType = PathFillType.NonZero
                        ) {
                            // First chevron
                            moveTo(9.00002f, 7.18133f)
                            lineTo(11.925f, 4.27508f)
                            curveTo(12.0625f, 4.13758f, 12.2344f, 4.06571f, 12.4406f, 4.05946f)
                            curveTo(12.6469f, 4.05321f, 12.825f, 4.12508f, 12.975f, 4.27508f)
                            curveTo(13.1125f, 4.41258f, 13.1813f, 4.58758f, 13.1813f, 4.80008f)
                            curveTo(13.1813f, 5.01258f, 13.1125f, 5.19071f, 12.975f, 5.34071f)
                            lineTo(9.54377f, 8.75633f)
                            curveTo(9.45627f, 8.84383f, 9.35627f, 8.90946f, 9.24377f, 8.95321f)
                            curveTo(9.13127f, 8.99696f, 9.01252f, 9.01883f, 8.88752f, 9.01883f)
                            curveTo(8.76252f, 9.01883f, 8.64377f, 8.99696f, 8.53127f, 8.95321f)
                            curveTo(8.41877f, 8.90946f, 8.31877f, 8.84383f, 8.23127f, 8.75633f)
                            lineTo(4.80002f, 5.34071f)
                            curveTo(4.66252f, 5.20321f, 4.59065f, 5.03446f, 4.5844f, 4.83446f)
                            curveTo(4.57815f, 4.63446f, 4.65002f, 4.45633f, 4.80002f, 4.30008f)
                            curveTo(4.95002f, 4.15008f, 5.13127f, 4.07508f, 5.34377f, 4.07508f)
                            curveTo(5.55627f, 4.07508f, 5.73752f, 4.15008f, 5.88752f, 4.30008f)
                            lineTo(9.00002f, 7.18133f)
                            close()
                            // Second chevron
                            moveTo(9.00002f, 13.1813f)
                            lineTo(11.925f, 10.2751f)
                            curveTo(12.0625f, 10.1376f, 12.2344f, 10.0657f, 12.4406f, 10.0595f)
                            curveTo(12.6469f, 10.0532f, 12.825f, 10.1251f, 12.975f, 10.2751f)
                            curveTo(13.1125f, 10.4126f, 13.1813f, 10.5876f, 13.1813f, 10.8001f)
                            curveTo(13.1813f, 11.0126f, 13.1125f, 11.1907f, 12.975f, 11.3407f)
                            lineTo(9.54377f, 14.7563f)
                            curveTo(9.45627f, 14.8438f, 9.35627f, 14.9095f, 9.24377f, 14.9532f)
                            curveTo(9.13127f, 14.997f, 9.01252f, 15.0188f, 8.88752f, 15.0188f)
                            curveTo(8.76252f, 15.0188f, 8.64377f, 14.997f, 8.53127f, 14.9532f)
                            curveTo(8.41877f, 14.9095f, 8.31877f, 14.8438f, 8.23127f, 14.7563f)
                            lineTo(4.80002f, 11.3407f)
                            curveTo(4.66252f, 11.2032f, 4.59065f, 11.0345f, 4.5844f, 10.8345f)
                            curveTo(4.57815f, 10.6345f, 4.65002f, 10.4563f, 4.80002f, 10.3001f)
                            curveTo(4.95002f, 10.1501f, 5.13127f, 10.0751f, 5.34377f, 10.0751f)
                            curveTo(5.55627f, 10.0751f, 5.73752f, 10.1501f, 5.88752f, 10.3001f)
                            lineTo(9.00002f, 13.1813f)
                            close()
                        }.build()

                        Icon(
                            imageVector = doubleChevronIcon,
                            contentDescription = "Scroll to bottom",
                            modifier = Modifier.size(iconSize),
                            tint = WdsTheme.colors.colorContentDeemphasized
                        )
                    }
                }
            }
        }
    }
}