package com.example.chatapp.features.chatinfo

import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.features.chatinfo.components.*
import com.example.chatapp.wds.theme.WdsTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInfoScreen(
    conversationId: String,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit = {},
    viewModel: ChatInfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkTheme = isSystemInDarkTheme()

    // Cache theme lookups
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    // Track scroll state for collapsing header
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    // Calculate threshold: top padding (24dp) + avatar (120dp) + spacer (16dp) = 160dp
    // This is where the name text starts in ProfileHeader/GroupInfoHeader
    val namePositionDp = dimensions.wdsSpacingTriple + dimensions.wdsAvatarXXL + dimensions.wdsSpacingDouble
    val thresholdPx = with(density) { namePositionDp.toPx() }

    // Determine if header should be collapsed (when name scrolls under the header)
    val isHeaderCollapsed by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > thresholdPx
        }
    }

    // Set status bar color based on theme
    val systemUiController = rememberSystemUiController()
    val statusBarColor = colors.colorSurfaceDefault
    DisposableEffect(isDarkTheme, statusBarColor) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = !isDarkTheme
        )
        onDispose {
            // Reset to transparent when leaving the screen
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = !isDarkTheme
            )
        }
    }

    // Load chat info when the screen is first displayed
    LaunchedEffect(conversationId) {
        viewModel.loadChatInfo(conversationId)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            TopAppBar(
                title = {
                    // Show avatar and name when collapsed
                    if (isHeaderCollapsed) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar - 40dp matching ChatTopBar
                            val avatarUrl = if (uiState.isGroupChat) {
                                uiState.conversation?.avatarUrl
                            } else {
                                uiState.directChatUser?.avatarUrl
                            }

                            when {
                                avatarUrl != null && avatarUrl.startsWith("drawable://") -> {
                                    // Handle local drawable resources for groups
                                    val drawableResId = when (avatarUrl.removePrefix("drawable://")) {
                                        "emoji_thinking_monocle" -> R.drawable.emoji_thinking_monocle
                                        "emoji_microscope" -> R.drawable.emoji_microscope
                                        "avatar_contact_placeholder" -> R.drawable.avatar_contact_placeholder
                                        "avatar_faith" -> R.drawable.avatar_faith
                                        "avatar_maria" -> R.drawable.avatar_maria
                                        "avatar_anika" -> R.drawable.avatar_anika
                                        "avatar_gerald" -> R.drawable.avatar_gerald
                                        "avatar_yuna" -> R.drawable.avatar_yuna
                                        "avatar_alice" -> R.drawable.avatar_alice
                                        "avatar_anna_soe" -> R.drawable.avatar_anna_soe
                                        "avatar_anika_chavan" -> R.drawable.avatar_anika_chavan
                                        "avatar_ayesha" -> R.drawable.avatar_ayesha
                                        "avatar_jordan" -> R.drawable.avatar_jordan
                                        else -> null
                                    }

                                    if (drawableResId != null) {
                                        Image(
                                            painter = painterResource(id = drawableResId),
                                            contentDescription = "Avatar",
                                            modifier = Modifier
                                                .size(dimensions.wdsAvatarMedium)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        AvatarPlaceholder(uiState.isGroupChat, colors, dimensions)
                                    }
                                }
                                avatarUrl != null && avatarUrl.isNotEmpty() -> {
                                    AsyncImage(
                                        model = avatarUrl,
                                        contentDescription = "Avatar",
                                        modifier = Modifier
                                            .size(dimensions.wdsAvatarMedium)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                else -> {
                                    AvatarPlaceholder(uiState.isGroupChat, colors, dimensions)
                                }
                            }

                            Spacer(modifier = Modifier.width(dimensions.wdsSpacingSingle))

                            // Name - matching ChatTopBar style
                            Text(
                                text = if (uiState.isGroupChat) {
                                    uiState.conversation?.title ?: "Unknown Group"
                                } else {
                                    uiState.directChatUser?.displayName ?: "Unknown"
                                },
                                style = typography.chatHeaderTitle,
                                color = colors.colorContentDefault,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.colorContentDefault
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK) }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More options",
                            tint = colors.colorContentDefault
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.colorSurfaceDefault
                )
            )
        }
    ) { paddingValues ->
        if (uiState.conversation != null) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = dimensions.wdsSpacingDouble)
            ) {
                if (uiState.isGroupChat) {
                    // Group Chat Info Components
                    item {
                        GroupInfoHeader(
                            conversation = uiState.conversation,
                            participantCount = uiState.participants.size,
                            onBackClick = onBackClick,
                            onMoreClick = { /* TODO: Show more options */ }
                        )
                    }

                    item {
                        GroupActionButtons(
                            onAudioCall = { /* TODO: Handle audio call */ },
                            onVideoCall = { /* TODO: Handle video call */ },
                            onAddMember = { /* TODO: Handle add member */ },
                            onSearch = { /* TODO: Handle search */ }
                        )
                    }

                    item { SectionDivider() }

                    item {
                        GroupDescription(
                            description = uiState.groupDescription,
                            createdBy = uiState.createdBy,
                            createdAt = uiState.conversation?.createdAt ?: System.currentTimeMillis(),
                            onAddDescription = { /* TODO: Handle add description */ }
                        )
                    }

                    // Only show media section if there's media
                    if (uiState.photoCount + uiState.videoCount + uiState.docCount > 0) {
                        item { SectionDivider() }

                        item {
                            MediaLinksDocsSection(
                                photoCount = uiState.photoCount,
                                videoCount = uiState.videoCount,
                                docCount = uiState.docCount,
                                mediaMessages = uiState.mediaMessages,
                                onViewAll = { /* TODO: Navigate to media gallery */ }
                            )
                        }
                    }

                    item { SectionDivider() }

                    item {
                        GroupSettingsSection(
                            isMuted = uiState.isMuted,
                            onToggleMute = viewModel::toggleMute,
                            onCustomNotifications = { /* TODO: Handle custom notifications */ },
                            onMediaVisibility = { /* TODO: Handle media visibility */ }
                        )
                    }

                    item { SectionDivider() }

                    item {
                        EncryptionSection()
                    }

                    item {
                        GroupManagementSection(
                            isLocked = uiState.chatLocked,
                            onToggleLock = viewModel::toggleChatLock,
                            hasDisappearingMessages = uiState.disappearingMessages,
                            onToggleDisappearingMessages = viewModel::toggleDisappearingMessages,
                            onGroupPermissions = { /* TODO: Handle group permissions */ }
                        )
                    }

                    item { SectionDivider() }

                    item {
                        GroupParticipantsSection(
                            participants = uiState.participants,
                            participantRoles = uiState.participantRoles,
                            currentUserId = "user_1", // Current user ID - matches the data generator
                            onAddParticipant = { /* TODO: Handle add participant */ },
                            onInviteViaLink = { /* TODO: Handle invite via link */ },
                            onAddToCommunity = { /* TODO: Handle add to community */ },
                            onParticipantClick = { /* TODO: Handle participant click */ },
                            onMakeAdmin = viewModel::makeAdmin,
                            onRemoveAdmin = viewModel::removeAdmin,
                            onRemoveParticipant = viewModel::removeParticipant
                        )
                    }

                    item { SectionDivider() }

                    item {
                        GroupDangerZone(
                            onExitGroup = { viewModel.exitGroup() },
                            onReportGroup = { viewModel.reportGroup() }
                        )
                    }

                } else {
                    // 1:1 Chat Info Components
                    item {
                        ProfileHeader(
                            user = uiState.directChatUser,
                            onBackClick = onBackClick,
                            onMoreClick = { /* TODO: Show more options */ }
                        )
                    }

                    item {
                        ContactActionButtons(
                            onAudioCall = { /* TODO: Handle audio call */ },
                            onVideoCall = { /* TODO: Handle video call */ },
                            onSearch = { /* TODO: Handle search */ }
                        )
                    }

                    // Customer notes row - no divider above, directly below action tiles
                    item {
                        com.example.chatapp.wds.components.WDSListRow(
                            iconRes = R.drawable.ic_notes,
                            text = "Add customer notes",
                            onClick = { /* TODO: Handle add customer notes */ },
                            iconTint = colors.colorContentDeemphasized
                        )
                    }
                    
                    item { SectionDivider() }

                    item {
                        ContactStatus(
                            statusMessage = uiState.directChatUser?.statusMessage,
                            phoneNumber = uiState.directChatUser?.username
                        )
                    }

                    // Only show media section if there's media
                    if (uiState.photoCount + uiState.videoCount + uiState.docCount > 0) {
                        item { SectionDivider() }

                        item {
                            MediaLinksDocsSection(
                                photoCount = uiState.photoCount,
                                videoCount = uiState.videoCount,
                                docCount = uiState.docCount,
                                mediaMessages = uiState.mediaMessages,
                                onViewAll = { /* TODO: Navigate to media gallery */ }
                            )
                        }
                    }

                    item { SectionDivider() }

                    item {
                        ContactSettingsSection(
                            isMuted = uiState.isMuted,
                            onToggleMute = viewModel::toggleMute,
                            onCustomNotifications = { /* TODO: Handle custom notifications */ },
                            onMediaVisibility = { /* TODO: Handle media visibility */ }
                        )
                    }

                    item { SectionDivider() }

                    item {
                        EncryptionSection()
                    }

                    item {
                        ContactManagementSection(
                            hasDisappearingMessages = uiState.disappearingMessages,
                            onToggleDisappearingMessages = viewModel::toggleDisappearingMessages
                        )
                    }

                    item { SectionDivider() }

                    item {
                        ContactDangerZone(
                            isBlocked = uiState.isBlocked,
                            userName = uiState.directChatUser?.displayName ?: "",
                            onBlockToggle = {
                                if (uiState.isBlocked) {
                                    viewModel.unblockUser()
                                } else {
                                    viewModel.blockUser()
                                }
                            },
                            onReport = { viewModel.reportUser() }
                        )
                    }
                }
            }
        } else {
            // Loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun AvatarPlaceholder(
    isGroupChat: Boolean,
    colors: com.example.chatapp.wds.theme.WdsColorScheme,
    dimensions: com.example.chatapp.wds.tokens.WdsDimensions
) {
    Box(
        modifier = Modifier
            .size(dimensions.wdsAvatarMedium)
            .clip(CircleShape)
            .background(
                if (isGroupChat) colors.colorSurfaceHighlight
                else colors.colorSurfaceElevatedDefault
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isGroupChat) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = "Group",
                modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                tint = colors.colorContentDeemphasized
            )
        }
    }
}