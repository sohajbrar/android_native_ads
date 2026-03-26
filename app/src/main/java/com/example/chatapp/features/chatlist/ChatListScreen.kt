@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.chatlist

import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.foundation.layout.*
import com.example.chatapp.wds.theme.WdsTheme
import com.example.chatapp.wds.theme.BaseColors
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider
import com.example.chatapp.wds.components.WDSSearchBar
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.animation.core.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import com.example.chatapp.R
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.CachePolicy
import kotlin.math.absoluteValue
import androidx.compose.runtime.compositionLocalOf
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.components.WDSContextMenu
import com.example.chatapp.wds.components.WDSContextMenuItem
import com.example.chatapp.wds.components.WDSChatListItem
import com.example.chatapp.wds.components.MessageType
import androidx.compose.material.icons.outlined.Palette
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.ZoneId

// Composition local for passing padding values
val LocalPaddingValues = compositionLocalOf<PaddingValues?> { null }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onChatClick: (String) -> Unit = {},
    onBroadcastChatClick: (conversationId: String, title: String, recipientCount: Int, linkedListCount: Int) -> Unit = { _, _, _, _ -> },
    viewModel: ChatListViewModel = hiltViewModel(),
    onSearchClick: () -> Unit = {},
    onCameraClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onNewChatClick: () -> Unit = {},
    onMetaAIClick: () -> Unit = {},
    onDesignLibraryClick: () -> Unit = {},
    onToolsClick: () -> Unit = {},
    onUpdatesClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val view = LocalView.current



    // Search state
    var searchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedSearchFilter by remember { mutableStateOf<String?>(null) }
    
    // Menu state
    var showMenu by remember { mutableStateOf(false) }
    
    // Log state changes
    LaunchedEffect(uiState.conversations) {
        println("ChatListScreen: UI State updated. Total conversations: ${uiState.conversations.size}")
        uiState.conversations.take(10).forEach { conv ->
            if (conv.id in listOf("conv_3", "conv_5", "conv_8")) {
                println("  ${conv.id}: unreadCount=${conv.unreadCount}, hasUnread=${conv.hasUnread}")
            }
        }
    }
    
    // Filter conversations based on search query
    val filteredConversations = remember(searchQuery, uiState.conversations) {
        if (searchQuery.isEmpty()) {
            uiState.conversations
        } else {
            uiState.conversations.filter { conversation ->
                conversation.title.contains(searchQuery, ignoreCase = true) ||
                conversation.lastMessage.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    // Simplified scaffold without insights/variants
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WdsTheme.colors.colorSurfaceDefault,
        contentColor = WdsTheme.colors.colorContentDefault,
        topBar = {
            // Only show top bar when search is not active
            if (!searchActive) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    ChatListTopBar(
                        onCameraClick = onCameraClick,
                        onMoreClick = { showMenu = true },
                        showInsightsButton = false,
                        onInsightsClick = {},
                        onLogoClick = {}
                    )
                    
                    // Context menu - aligned to top-end to position below the 3-dot button
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 56.dp, end = 8.dp)
                    ) {
                        WDSContextMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            items = listOf(
                                WDSContextMenuItem(
                                    icon = Icons.Outlined.Palette,
                                    text = "Design Library",
                                    onClick = onDesignLibraryClick
                                )
                            )
                        )
                    }
                }
            }
        },
        bottomBar = {
            ChatListBottomBar(
                selectedTab = 0,  // Chats tab is selected
                unreadChats = uiState.conversations.count { it.unreadCount > 0 },
                hasUpdates = false,
                callsBadgeCount = 0,
                onChatsClick = { /* Already on chats */ },
                onCallsClick = { /* Calls not implemented yet */ },
                onUpdatesClick = onUpdatesClick,
                onToolsClick = onToolsClick
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingDouble)
            ) {
                // Meta AI FAB
                Surface(
                    onClick = onMetaAIClick,
                    color = WdsTheme.colors.colorSurfaceEmphasized,
                    shape = WdsTheme.shapes.singlePlus,
                    tonalElevation = 0.dp,
                    shadowElevation = 6.dp,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_meta_ai),
                            contentDescription = "Meta AI",
                            modifier = Modifier.size(WdsTheme.dimensions.wdsIconSizeMedium),
                            tint = Color.Unspecified
                        )
                    }
                }

                // New Chat FAB
                FloatingActionButton(
                    onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onNewChatClick() },
                    containerColor = WdsTheme.colors.colorAccent,
                    contentColor = WdsTheme.colors.colorContentOnAccent
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_create_rounded),
                        contentDescription = "New Chat"
                    )
                }
            }
        }
    ) { paddingValues ->
        CompositionLocalProvider(LocalPaddingValues provides paddingValues) {
            ChatListMainContent(
                listState = listState,
                searchActive = searchActive,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchActiveChange = { searchActive = it },
                selectedSearchFilter = selectedSearchFilter,
                onSearchFilterSelected = { selectedSearchFilter = if (selectedSearchFilter == it) null else it },
                filteredConversations = filteredConversations,
                uiState = uiState,
                viewModel = viewModel,
                onChatClick = onChatClick,
                onBroadcastChatClick = onBroadcastChatClick,
                scope = scope
            )
        }
    }

}

@Composable
private fun ChatListMainContent(
    listState: androidx.compose.foundation.lazy.LazyListState,
    searchActive: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchActiveChange: (Boolean) -> Unit,
    selectedSearchFilter: String?,
    onSearchFilterSelected: (String) -> Unit,
    filteredConversations: List<ConversationUiModel>,
    uiState: ChatListUiState,
    viewModel: ChatListViewModel,
    onChatClick: (String) -> Unit,
    onBroadcastChatClick: (conversationId: String, title: String, recipientCount: Int, linkedListCount: Int) -> Unit,
    scope: CoroutineScope
) {
    val view = LocalView.current

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(WdsTheme.colors.colorSurfaceDefault)
            .padding(LocalPaddingValues.current ?: PaddingValues(0.dp))
        ) {
            // WDS Search Bar
            item {
                val colors = WdsTheme.colors
                val dimensions = WdsTheme.dimensions
                val typography = WdsTheme.typography
                val shapes = WdsTheme.shapes
                val keyboardController = LocalSoftwareKeyboardController.current

                if (searchActive) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = dimensions.wdsSpacingHalf,
                                end = dimensions.wdsSpacingDouble,
                                top = dimensions.wdsSpacingHalf,
                                bottom = dimensions.wdsSpacingHalf
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            onSearchActiveChange(false)
                            onSearchQueryChange("")
                            keyboardController?.hide()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = colors.colorContentDefault
                            )
                        }

                        WDSSearchBar(
                            query = searchQuery,
                            onQueryChange = onSearchQueryChange,
                            placeholder = "Ask Meta AI or Search",
                            modifier = Modifier.weight(1f),
                            trailingContent = if (searchQuery.isNotEmpty()) {
                                {
                                    Icon(
                                        imageVector = Icons.Outlined.Close,
                                        contentDescription = "Clear",
                                        modifier = Modifier
                                            .size(dimensions.wdsIconSizeMedium)
                                            .clickableWithSound { onSearchQueryChange("") },
                                        tint = colors.colorContentDeemphasized
                                    )
                                }
                            } else null,
                            onSearch = { keyboardController?.hide() }
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensions.wdsSpacingDouble,
                                vertical = dimensions.wdsSpacingHalf
                            )
                            .height(48.dp)
                            .clip(shapes.circle)
                            .background(colors.colorSurfaceHighlight)
                            .clickableWithSound { onSearchActiveChange(true) }
                            .padding(
                                horizontal = dimensions.wdsSpacingDouble,
                                vertical = dimensions.wdsSpacingSingle
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                            tint = colors.colorContentDeemphasized
                        )

                        Text(
                            text = "Ask Meta AI or Search",
                            style = typography.body1,
                            color = colors.colorContentDeemphasized,
                            modifier = Modifier.padding(horizontal = dimensions.wdsSpacingSingle)
                        )
                    }
                }
            }
            
            // Filter Chips
            item {
                if (searchActive) {
                    Column {
                        SearchFilterPills(
                            selectedFilter = selectedSearchFilter,
                            onFilterSelected = onSearchFilterSelected
                        )
                        HorizontalDivider(
                            color = WdsTheme.colors.colorDivider,
                            thickness = WdsTheme.dimensions.wdsBorderWidthThin
                        )
                    }
                } else {
                    FilterChips(
                        selectedFilter = uiState.selectedFilter,
                        onFilterSelected = { viewModel.selectFilter(it) },
                        modifier = Modifier.padding(
                            horizontal = WdsTheme.dimensions.wdsSpacingDouble,
                            vertical = WdsTheme.dimensions.wdsSpacingHalf
                        )
                    )
                }
            }

            // Search results when search is active
            if (searchActive && searchQuery.isNotEmpty()) {
                if (filteredConversations.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(WdsTheme.dimensions.wdsSpacingQuad),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No results found",
                                style = WdsTheme.typography.body2,
                                color = WdsTheme.colors.colorContentDeemphasized
                            )
                        }
                    }
                } else {
                    items(filteredConversations.size) { index ->
                        val conversation = filteredConversations[index]
                        WDSChatListItem(
                            title = conversation.title,
                            subtitle = conversation.subtitle,
                            avatarUrl = conversation.avatarUrl,
                            lastMessage = conversation.lastMessage,
                            lastMessageTime = conversation.lastMessageTime,
                            lastMessageSender = conversation.lastMessageSender,
                            lastMessageType = conversation.lastMessageType,
                            unreadCount = conversation.unreadCount,
                            isGroup = conversation.isGroup,
                            isSentByUser = conversation.isSentByUser,
                            isRead = conversation.isRead,
                            isMissedCall = conversation.isMissedCall,
                            onClick = {
                                if (conversation.isBroadcast) {
                                    onBroadcastChatClick(
                                        conversation.id,
                                        conversation.title,
                                        conversation.broadcastRecipientCount,
                                        conversation.broadcastLinkedListCount
                                    )
                                } else {
                                    onChatClick(conversation.id)
                                }
                                onSearchActiveChange(false)
                                onSearchQueryChange("")
                                onSearchFilterSelected("")
                            }
                        )
                    }
                }
            }

            // Chat Items
            if (!searchActive) {
                items(
                    items = uiState.conversations,
                    key = { it.id },
                    contentType = { "chat_item" }
                ) { conversation ->
                // Log to verify recomposition with more detail
                LaunchedEffect(conversation) {
                    if (conversation.id in listOf("conv_3", "conv_5", "conv_8")) {
                        println("ChatListScreen: Rendering ${conversation.id} - unreadCount=${conversation.unreadCount}, hasUnread=${conversation.hasUnread}")
                    }
                }
                
                // Also log in the composable itself to ensure we see recomposition
                if (conversation.id in listOf("conv_3", "conv_5", "conv_8")) {
                    SideEffect {
                        println("ChatListScreen: Composing item ${conversation.id} - unreadCount=${conversation.unreadCount}, hasUnread=${conversation.hasUnread}")
                    }
                }
                
                WDSChatListItem(
                    title = conversation.title,
                    subtitle = conversation.subtitle,
                    avatarUrl = conversation.avatarUrl,
                    lastMessage = conversation.lastMessage,
                    lastMessageTime = conversation.lastMessageTime,
                    lastMessageSender = conversation.lastMessageSender,
                    lastMessageType = conversation.lastMessageType,
                    unreadCount = conversation.unreadCount,
                    isGroup = conversation.isGroup,
                    isSentByUser = conversation.isSentByUser,
                    isRead = conversation.isRead,
                    isMissedCall = conversation.isMissedCall,
                    onClick = {
                        if (conversation.isBroadcast) {
                            onBroadcastChatClick(
                                conversation.id,
                                conversation.title,
                                conversation.broadcastRecipientCount,
                                conversation.broadcastLinkedListCount
                            )
                        } else {
                            onChatClick(conversation.id)
                        }
                    }
                )
                }
            }
            
            // End-to-end encryption notice - only show when not searching
            if (!searchActive) {
                item {
                    EncryptionNotice()
                }
            }
        }
    }

@Composable
private fun ChatListTopBar(
    onCameraClick: () -> Unit,
    onMoreClick: () -> Unit,
    showInsightsButton: Boolean = false,
    onInsightsClick: () -> Unit = {},
    onLogoClick: () -> Unit = {}
) {
    val view = LocalView.current

    TopAppBar(
        title = {
            Icon(
                painter = painterResource(R.drawable.ic_whatsapp_text_logo),
                contentDescription = "WhatsApp",
                modifier = Modifier
                    .height(21.dp)
                    .clickableWithSound { onLogoClick() },
                tint = WdsTheme.colors.colorContentDefault
            )
        },
        actions = {
            // Insights button (only shown for backdrop variant)
            if (showInsightsButton) {
                IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onInsightsClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_insights),
                        contentDescription = "Insights",
                        tint = WdsTheme.colors.colorContentDefault
                    )
                }
            }

            IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onCameraClick() }) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Camera",
                        tint = WdsTheme.colors.colorContentDefault
                    )
                }
            IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onMoreClick() }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = WdsTheme.colors.colorContentDefault
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = WdsTheme.colors.colorSurfaceDefault
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChips(
    selectedFilter: ChatFilter,
    onFilterSelected: (ChatFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
    ) {
        // All filter
        FilterChip(
            selected = selectedFilter == ChatFilter.ALL,
            onClick = { onFilterSelected(ChatFilter.ALL) },
            label = { Text("All") },
            shape = RoundedCornerShape(com.example.chatapp.wds.tokens.BaseDimensions.wdsCornerRadiusCircle),
            border = BorderStroke(WdsTheme.dimensions.wdsBorderWidthThin, WdsTheme.colors.colorOutlineDeemphasized),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = WdsTheme.colors.colorFilterSurfaceSelected,
                selectedLabelColor = WdsTheme.colors.colorContentActionEmphasized,
                labelColor = WdsTheme.colors.colorContentDeemphasized
            )
        )

        // Unread filter
        FilterChip(
            selected = selectedFilter == ChatFilter.UNREAD,
            onClick = { onFilterSelected(ChatFilter.UNREAD) },
            label = { Text("Unread") },
            shape = RoundedCornerShape(com.example.chatapp.wds.tokens.BaseDimensions.wdsCornerRadiusCircle),
            border = BorderStroke(WdsTheme.dimensions.wdsBorderWidthThin, WdsTheme.colors.colorOutlineDeemphasized),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = WdsTheme.colors.colorFilterSurfaceSelected,
                selectedLabelColor = WdsTheme.colors.colorContentActionEmphasized,
                labelColor = WdsTheme.colors.colorContentDeemphasized
            )
        )

        // Favorites filter
        FilterChip(
            selected = selectedFilter == ChatFilter.FAVORITES,
            onClick = { onFilterSelected(ChatFilter.FAVORITES) },
            label = { Text("Favorites") },
            shape = RoundedCornerShape(com.example.chatapp.wds.tokens.BaseDimensions.wdsCornerRadiusCircle),
            border = BorderStroke(WdsTheme.dimensions.wdsBorderWidthThin, WdsTheme.colors.colorOutlineDeemphasized),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = WdsTheme.colors.colorFilterSurfaceSelected,
                selectedLabelColor = WdsTheme.colors.colorContentActionEmphasized,
                labelColor = WdsTheme.colors.colorContentDeemphasized
            )
        )

        // Groups filter
        FilterChip(
            selected = selectedFilter == ChatFilter.GROUPS,
            onClick = { onFilterSelected(ChatFilter.GROUPS) },
            label = { Text("Groups") },
            shape = RoundedCornerShape(com.example.chatapp.wds.tokens.BaseDimensions.wdsCornerRadiusCircle),
            border = BorderStroke(WdsTheme.dimensions.wdsBorderWidthThin, WdsTheme.colors.colorOutlineDeemphasized),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = WdsTheme.colors.colorFilterSurfaceSelected,
                selectedLabelColor = WdsTheme.colors.colorContentActionEmphasized,
                labelColor = WdsTheme.colors.colorContentDeemphasized
            )
        )
    }
}

@Composable
private fun ArchivedRow(
    count: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithSound { onClick() }
            .padding(
                horizontal = WdsTheme.dimensions.wdsSpacingDouble,
                vertical = WdsTheme.dimensions.wdsSpacingHalf
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        Box(
            modifier = Modifier.size(WdsTheme.dimensions.wdsTouchTargetComfortable),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Archive,
                contentDescription = "Archived",
                tint = WdsTheme.colors.colorContentDeemphasized,
                modifier = Modifier.size(WdsTheme.dimensions.wdsIconSizeMedium)
            )
        }
        
        Text(
            text = "Archived",
            style = WdsTheme.typography.body1Emphasized,
            color = WdsTheme.colors.colorContentDeemphasized,
            modifier = Modifier.weight(1f)
        )
        
        if (count > 0) {
            Text(
                text = count.toString(),
                style = WdsTheme.typography.body3Emphasized,
                color = WdsTheme.colors.colorContentDeemphasized
            )
        }
    }
}

@Composable
fun ChatListBottomBar(
    selectedTab: Int,
    unreadChats: Int,
    hasUpdates: Boolean,
    callsBadgeCount: Int,
    onChatsClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onUpdatesClick: () -> Unit = {},
    onToolsClick: () -> Unit = {}
) {
    val view = LocalView.current

    Column {
        WDSDivider()
        NavigationBar(
            containerColor = WdsTheme.colors.colorSurfaceDefault,
            tonalElevation = 0.dp  // Disable surface tint overlay
        ) {
        // Tab 1: Chats (leftmost)
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onChatsClick() },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WdsTheme.colors.colorAccentEmphasized,
                selectedTextColor = WdsTheme.colors.colorContentDefault,
                unselectedIconColor = WdsTheme.colors.colorContentDefault,
                unselectedTextColor = WdsTheme.colors.colorContentDefault,
                indicatorColor = WdsTheme.colors.colorFilterSurfaceSelected
            ),
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadChats > 0) {
                            Badge(
                                containerColor = WdsTheme.colors.colorAlwaysBranded,
                                contentColor = WdsTheme.colors.colorContentOnAccent
                            ) {
                                Text(
                                    text = unreadChats.toString(),
                                    style = WdsTheme.typography.body3Emphasized,
                                    color = WdsTheme.colors.colorContentOnAccent
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            if (selectedTab == 0) R.drawable.ic_chats_rounded else R.drawable.ic_chats_rounded_outlined
                        ),
                        contentDescription = "Chats"
                    )
                }
            },
            label = {
                Text(
                    text = "Chats",
                    style = if (selectedTab == 0) WdsTheme.typography.body3InlineLink else WdsTheme.typography.body3Emphasized
                )
            }
        )
        
        // Tab 2: Calls (second from left)
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onCallsClick() },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WdsTheme.colors.colorAccentEmphasized,
                selectedTextColor = WdsTheme.colors.colorContentDefault,
                unselectedIconColor = WdsTheme.colors.colorContentDefault,
                unselectedTextColor = WdsTheme.colors.colorContentDefault,
                indicatorColor = WdsTheme.colors.colorFilterSurfaceSelected
            ),
            icon = {
                BadgedBox(
                    badge = {
                        if (callsBadgeCount > 0) {
                            Badge(
                                containerColor = WdsTheme.colors.colorAlwaysBranded,
                                contentColor = WdsTheme.colors.colorContentOnAccent
                            ) {
                                Text(
                                    text = callsBadgeCount.toString(),
                                    style = WdsTheme.typography.body3Emphasized,
                                    color = WdsTheme.colors.colorContentOnAccent
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Call,
                        contentDescription = "Calls"
                    )
                }
            },
            label = {
                Text(
                    text = "Calls",
                    style = if (selectedTab == 1) WdsTheme.typography.body3InlineLink else WdsTheme.typography.body3Emphasized
                )
            }
        )
        
        // Tab 3: Updates (third from left)
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onUpdatesClick() },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WdsTheme.colors.colorAccentEmphasized,
                selectedTextColor = WdsTheme.colors.colorContentDefault,
                unselectedIconColor = WdsTheme.colors.colorContentDefault,
                unselectedTextColor = WdsTheme.colors.colorContentDefault,
                indicatorColor = WdsTheme.colors.colorFilterSurfaceSelected
            ),
            icon = {
                BadgedBox(
                    badge = {
                        if (hasUpdates) {
                            Badge(
                                modifier = Modifier.size(WdsTheme.dimensions.wdsSpacingSingle),
                                containerColor = WdsTheme.colors.colorAlwaysBranded
                            )
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_updates_rounded),
                        contentDescription = "Updates"
                    )
                }
            },
            label = {
                Text(
                    text = "Updates",
                    style = if (selectedTab == 2) WdsTheme.typography.body3InlineLink else WdsTheme.typography.body3Emphasized
                )
            }
        )
        
        // Tab 4: Tools (rightmost) - Business tools
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onToolsClick() },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WdsTheme.colors.colorAccentEmphasized,
                selectedTextColor = WdsTheme.colors.colorContentDefault,
                unselectedIconColor = WdsTheme.colors.colorContentDefault,
                unselectedTextColor = WdsTheme.colors.colorContentDefault,
                indicatorColor = WdsTheme.colors.colorFilterSurfaceSelected
            ),
            icon = {
                Icon(
                    imageVector = if (selectedTab == 3) Icons.Filled.Store else Icons.Outlined.Store,
                    contentDescription = "Tools"
                )
            },
            label = {
                Text(
                    text = "Tools",
                    style = if (selectedTab == 3) WdsTheme.typography.body3InlineLink else WdsTheme.typography.body3Emphasized
                )
            }
        )
        }
    }
}

@Composable
private fun SearchFilterPills(
    selectedFilter: String?,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("Photos", "Videos", "Links", "GIFs", "Audio", "Documents")
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = WdsTheme.dimensions.wdsSpacingHalf),
        horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle),
        contentPadding = PaddingValues(horizontal = WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                shape = RoundedCornerShape(com.example.chatapp.wds.tokens.BaseDimensions.wdsCornerRadiusCircle),
                border = BorderStroke(WdsTheme.dimensions.wdsBorderWidthThin, WdsTheme.colors.colorOutlineDeemphasized),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = WdsTheme.colors.colorFilterSurfaceSelected,
                    selectedLabelColor = WdsTheme.colors.colorContentActionEmphasized,
                    labelColor = WdsTheme.colors.colorContentDeemphasized
                )
            )
        }
    }
}

@Composable
private fun EncryptionNotice() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(WdsTheme.dimensions.wdsSpacingDouble),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = WdsTheme.colors.colorContentDeemphasized,
            modifier = Modifier.size(WdsTheme.dimensions.wdsIconSizeMediumSmall)
        )
        Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingHalf))
        Text(
            text = "Your personal messages are ",
            style = WdsTheme.typography.body3,
            color = WdsTheme.colors.colorContentDeemphasized
        )
        Text(
            text = "end-to-end encrypted",
            style = WdsTheme.typography.body3,
            color = WdsTheme.colors.colorPositive
        )
    }

    HorizontalDivider(
        color = WdsTheme.colors.colorDivider,
        thickness = WdsTheme.dimensions.wdsBorderWidthThin
    )
}

// UI Models and Enums
data class ConversationUiModel(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val avatarUrl: String?,
    val lastMessage: String,
    val lastMessageTime: Instant,
    val lastMessageSender: String? = null,
    val lastMessageType: MessageType = MessageType.TEXT,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false,
    val isSentByUser: Boolean = false,
    val isRead: Boolean = false,
    val hasUnread: Boolean = false,
    val isMissedCall: Boolean = false,
    val isPinned: Boolean = false,
    val isBroadcast: Boolean = false,
    val broadcastRecipientCount: Int = 0,
    val broadcastLinkedListCount: Int = 0
)

enum class ChatFilter {
    ALL, UNREAD, FAVORITES, GROUPS
}
