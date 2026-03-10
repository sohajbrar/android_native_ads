package com.example.chatapp.features.broadcast

import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.features.chat.components.ChatComposer
import com.example.chatapp.features.chat.components.DateHeader
import com.example.chatapp.features.chat.components.MessageItem
import com.example.chatapp.wds.components.WDSSystemMessage
import com.example.chatapp.wds.components.WDSSystemMessageType
import com.example.chatapp.wds.components.WdsComingSoonDialog
import com.example.chatapp.wds.theme.BaseColors
import com.example.chatapp.wds.theme.WdsTheme
import com.example.chatapp.wds.tokens.BaseDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastChatScreen(
    conversationId: String = "",
    title: String,
    recipientCount: Int,
    linkedListCount: Int = 0,
    sentMessage: String? = null,
    onBackClick: () -> Unit = {},
    onHeaderClick: () -> Unit = {},
    onNextClick: (messageText: String) -> Unit = {},
    onMessageProcessed: () -> Unit = {},
    viewModel: BroadcastViewModel = hiltViewModel()
) {
    LaunchedEffect(sentMessage) {
        if (!sentMessage.isNullOrBlank()) {
            viewModel.addSentMessage(sentMessage, conversationId)
            onMessageProcessed()
        }
    }

    val sentMessages by viewModel.sentMessages.collectAsState()

    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    var composerText by remember { mutableStateOf("") }
    var showAttachmentPanel by remember { mutableStateOf(false) }
    var showGallerySheet by remember { mutableStateOf(false) }
    var showComingSoonDialog by remember { mutableStateOf(false) }

    if (showComingSoonDialog) {
        WdsComingSoonDialog(onDismissRequest = { showComingSoonDialog = false })
    }

    if (showGallerySheet) {
        GalleryBottomSheet(onDismiss = { showGallerySheet = false })
    }

    Scaffold(
        containerColor = colors.colorChatBackgroundWallpaper,
        topBar = {
            BroadcastChatTopBar(
                title = title,
                recipientCount = recipientCount,
                onBackClick = onBackClick,
                onHeaderClick = onHeaderClick,
                onMoreClick = onHeaderClick
            )
        },
        bottomBar = {
            Column {
                ChatComposer(
                    value = composerText,
                    onValueChange = { composerText = it },
                    onSendClick = { onNextClick(composerText) },
                    onAttachClick = { showComingSoonDialog = true },
                    onCameraClick = { showComingSoonDialog = true },
                    isBroadcast = true,
                    onStickerClick = { showComingSoonDialog = true }
                )
                AnimatedVisibility(
                    visible = showAttachmentPanel,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    BroadcastAttachmentPanel(
                        onGalleryClick = { showGallerySheet = true }
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
        ) {
            item(key = "date_header") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensions.wdsSpacingSingle),
                    contentAlignment = Alignment.Center
                ) {
                    DateHeader(timestamp = System.currentTimeMillis())
                }
            }

            item(key = "security_message") {
                WDSSystemMessage(
                    type = WDSSystemMessageType.SECURITY_E2EE,
                    modifier = Modifier.padding(bottom = dimensions.wdsSpacingSingle)
                )
            }

            item(key = "audience_created") {
                BroadcastSystemBubble(text = "You created an audience.")
            }

            if (linkedListCount > 0) {
                item(key = "linked_list") {
                    val listText = if (linkedListCount == 1) {
                        "You linked 1 list to your audience."
                    } else {
                        "You linked $linkedListCount lists to your audience."
                    }
                    BroadcastSystemBubble(text = listText)
                }
            }

            items(
                items = sentMessages,
                key = { it.messageId }
            ) { message ->
                MessageItem(
                    message = message,
                    isFromCurrentUser = true,
                    isGroupChat = false,
                    senderName = "",
                    senderAvatar = null,
                    showSenderInfo = false
                )
            }
        }
    }
}

@Composable
private fun BroadcastSystemBubble(
    text: String,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensions.wdsSpacingQuarter),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = colors.colorBubbleSurfaceSystem,
            shape = WdsTheme.shapes.single
        ) {
            Text(
                text = text,
                style = typography.body3,
                color = colors.colorContentDeemphasized,
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingSinglePlus,
                    vertical = dimensions.wdsSpacingSingle
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BroadcastChatTopBar(
    title: String,
    recipientCount: Int,
    onBackClick: () -> Unit,
    onHeaderClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableWithSound { onHeaderClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = colors.colorBroadcastAvatar,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.ic_business_broadcast),
                            contentDescription = null,
                            modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                            tint = colors.colorContentOnAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.width(dimensions.wdsSpacingSingle))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = typography.chatHeaderTitle,
                        color = colors.colorContentDefault,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "$recipientCount recipients",
                        style = typography.body3,
                        color = colors.colorContentDeemphasized,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = colors.colorContentDefault
                )
            }
        },
        actions = {
            IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onMoreClick() }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    tint = colors.colorContentDefault
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.colorSurfaceDefault,
            titleContentColor = colors.colorContentDefault,
            navigationIconContentColor = colors.colorContentDefault,
            actionIconContentColor = colors.colorContentDefault
        ),
        modifier = modifier
    )
}

@Composable
private fun BroadcastAttachmentPanel(
    onGalleryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colors.colorSurfaceDefault
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensions.wdsSpacingTriple,
                    vertical = dimensions.wdsSpacingDouble
                ),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AttachmentOption(
                icon = Icons.Outlined.Description,
                label = "Document",
                tint = BaseColors.wdsPurple500,
                onClick = {}
            )
            AttachmentOption(
                icon = Icons.Outlined.PhotoCamera,
                label = "Camera",
                tint = BaseColors.wdsPink500,
                onClick = {}
            )
            AttachmentOption(
                icon = Icons.Outlined.Image,
                label = "Gallery",
                tint = BaseColors.wdsCobalt500,
                onClick = onGalleryClick
            )
            AttachmentOption(
                icon = Icons.Outlined.Storefront,
                label = "Catalog",
                tint = BaseColors.wdsBrown500,
                onClick = {}
            )
        }
    }
}

@Composable
private fun AttachmentOption(
    icon: ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column(
        modifier = modifier.clickableWithSound(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
    ) {
        Surface(
            shape = WdsTheme.shapes.double,
            color = colors.colorSurfaceElevatedDefault,
            border = BorderStroke(1.dp, colors.colorDivider)
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .padding(dimensions.wdsSpacingDouble),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                )
            }
        }
        Text(
            text = label,
            style = typography.body3,
            color = colors.colorContentDeemphasized
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GalleryBottomSheet(
    onDismiss: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.colorSurfaceElevatedDefault,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(
            topStart = BaseDimensions.wdsCornerRadiusTriplePlus,
            topEnd = BaseDimensions.wdsCornerRadiusTriplePlus
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensions.wdsSpacingDouble)
        ) {
            Text(
                text = "Gallery",
                style = typography.headline2,
                color = colors.colorContentDefault,
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingSingle
                )
            )

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .padding(horizontal = dimensions.wdsSpacingHalf),
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter),
                verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
            ) {
                items(12) { index ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(
                                colors.colorSurfaceHighlight,
                                RoundedCornerShape(dimensions.wdsSpacingHalf)
                            )
                            .clickableWithSound { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Image,
                            contentDescription = null,
                            tint = colors.colorContentDeemphasized,
                            modifier = Modifier.size(dimensions.wdsIconSizeLarge)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Broadcast Chat - List Selected")
@Composable
private fun BroadcastChatScreenPreviewList() {
    WdsTheme(darkTheme = false) {
        BroadcastChatScreen(
            title = "New order",
            recipientCount = 303,
            linkedListCount = 1
        )
    }
}

@Preview(showBackground = true, name = "Broadcast Chat - Contacts Selected")
@Composable
private fun BroadcastChatScreenPreviewContacts() {
    WdsTheme(darkTheme = false) {
        BroadcastChatScreen(
            title = "Alice, Anna Soe, Anika Chavan, Ayesha",
            recipientCount = 4,
            linkedListCount = 0
        )
    }
}

@Preview(showBackground = true, name = "Broadcast Chat - Dark")
@Composable
private fun BroadcastChatScreenPreviewDark() {
    WdsTheme(darkTheme = true) {
        BroadcastChatScreen(
            title = "New order",
            recipientCount = 303,
            linkedListCount = 1
        )
    }
}
