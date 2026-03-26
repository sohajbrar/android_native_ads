@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.updates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import android.view.SoundEffectConstants
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.chatapp.R
import com.example.chatapp.features.chatlist.ChatListBottomBar
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun UpdatesScreen(
    onNavigateToChats: () -> Unit = {},
    onStatusClick: (Int) -> Unit = {},
    onMyStatusClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onToolsClick: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions

    val statusItems = remember { StatusData.statuses }

    val followedChannels = remember {
        listOf(
            ChannelItem("c1", "The New York Times", avatarData = R.drawable.channel_nyt, avatarColor = Color(0xFF1A1A1A), isVerified = true, lastMessage = "\uD83D\uDCFA The Elections Department has...", time = "12:21", unreadCount = 8),
            ChannelItem("c2", "WhatsApp", avatarData = R.drawable.channel_whatsapp, avatarColor = Color(0xFF25D366), isVerified = true, lastMessage = "\uD83D\uDCFA New: WhatsApp for Mac. Everythin...", time = "Yesterday"),
            ChannelItem("c3", "PGA Tour", avatarData = R.drawable.channel_pga_tour, avatarColor = Color(0xFF00205B), lastMessage = "It's official. The U.S. Team for the...", time = "Yesterday")
        )
    }

    val recommendedChannels = remember {
        listOf(
            RecommendedChannelItem("r1", "NASCAR", avatarData = R.drawable.channel_nascar, avatarColor = Color(0xFF1A237E), isVerified = true, followerCount = "390K followers"),
            RecommendedChannelItem("r2", "NFL", avatarData = R.drawable.channel_nfl, avatarColor = Color(0xFF01579B), isVerified = true, followerCount = "2.1M followers"),
            RecommendedChannelItem("r3", "ESPN", avatarData = R.drawable.channel_espn, avatarColor = Color(0xFFD50000), isVerified = true, followerCount = "1.5M followers"),
            RecommendedChannelItem("r4", "BBC News", avatarData = R.drawable.channel_bbc, avatarColor = Color(0xFF880E4F), isVerified = true, followerCount = "4.2M followers")
        )
    }

    Scaffold(
        topBar = {
            UpdatesTopBar(
                onSearchClick = onSearchClick,
                onMoreClick = onMoreClick
            )
        },
        bottomBar = {
            ChatListBottomBar(
                selectedTab = 2,
                unreadChats = 2,
                hasUpdates = false,
                callsBadgeCount = 12,
                onChatsClick = onNavigateToChats,
                onCallsClick = onCallsClick,
                onUpdatesClick = { },
                onToolsClick = onToolsClick
            )
        },
        floatingActionButton = {
            UpdatesFabs()
        },
        containerColor = colors.colorSurfaceDefault
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Status section
            item {
                SectionDividerHeader(title = "Status")
            }
            item {
                StatusCardsRow(items = statusItems, onStatusClick = onStatusClick, onMyStatusClick = onMyStatusClick)
            }

            // Channels section
            item {
                SectionDividerHeader(
                    title = "Channels",
                    buttonText = "Explore",
                    onButtonClick = {}
                )
            }
            items(followedChannels, key = { it.id }) { channel ->
                FollowedChannelRow(channel = channel)
            }

            // Find channels to follow section
            item {
                SectionDividerHeader(title = "Find channels to follow", isSubsection = true)
            }
            items(recommendedChannels, key = { it.id }) { channel ->
                RecommendedChannelRow(channel = channel)
            }

            // Action buttons
            item {
                ChannelActionButtons()
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuint))
            }
        }
    }
}

// region Top Bar

@Composable
private fun UpdatesTopBar(
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography

    TopAppBar(
        title = {
            Text(
                text = "Updates",
                style = typography.headline2,
                color = colors.colorContentDefault
            )
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = colors.colorContentDefault
                )
            }
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
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

// endregion

// region Section Divider

@Composable
private fun SectionDividerHeader(
    title: String,
    isSubsection: Boolean = false,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val headerStyle = if (isSubsection) {
        TextStyle(fontSize = 14.sp, lineHeight = 30.sp, fontWeight = FontWeight.Medium)
    } else {
        TextStyle(fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.Medium)
    }
    val headerColor = if (isSubsection) colors.colorContentDeemphasized else colors.colorContentDefault

    Column {
        Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensions.wdsSpacingDouble,
                    end = dimensions.wdsSpacingDouble,
                    bottom = dimensions.wdsSpacingSingle
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = headerStyle,
                color = headerColor
            )

            if (buttonText != null) {
                Surface(
                    onClick = { onButtonClick?.invoke() },
                    shape = RoundedCornerShape(100.dp),
                    color = colors.colorSurfaceHighlight
                ) {
                    Text(
                        text = buttonText,
                        style = typography.body2Emphasized,
                        color = colors.colorContentDefault,
                        modifier = Modifier.padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = 6.dp
                        )
                    )
                }
            }
        }
    }
}

// endregion

// region Status Cards

@Composable
private fun StatusCardsRow(items: List<StatusItem>, onStatusClick: (Int) -> Unit, onMyStatusClick: () -> Unit) {
    val dimensions = WdsTheme.dimensions
    val viewableStatuses = remember { StatusData.viewableStatuses }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = dimensions.wdsSpacingDouble),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(items, key = { it.id }) { item ->
            when {
                item.isMyStatus -> MyStatusCard(item = item, onClick = onMyStatusClick)
                item.isHidden -> HiddenStatusCard()
                else -> {
                    val viewerIndex = viewableStatuses.indexOfFirst { it.id == item.id }
                    ContactStatusCard(
                        item = item,
                        onClick = { if (viewerIndex >= 0) onStatusClick(viewerIndex) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MyStatusCard(item: StatusItem, onClick: () -> Unit = {}) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val context = LocalContext.current
    val view = LocalView.current

    Box(
        modifier = Modifier
            .size(width = 90.dp, height = 160.dp)
            .clip(RoundedCornerShape(dimensions.wdsSpacingDouble))
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onClick()
            }
    ) {
        if (item.previewImageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.previewImageUrl)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = "My status",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x660A1014),
                            Color(0x4D0A1014),
                            Color(0x1A0A1014),
                            Color.Transparent
                        )
                    )
                )
                .padding(dimensions.wdsSpacingSingle)
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(item.avatarUrl)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4DB6AC))
                )

                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .offset(x = 1.dp, y = 1.dp)
                        .clip(CircleShape)
                        .background(colors.colorAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add status",
                        tint = colors.colorContentOnAccent,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x1A0A1014),
                            Color(0x4D0A1014),
                            Color(0x730A1014)
                        )
                    )
                )
                .padding(
                    start = dimensions.wdsSpacingSingle,
                    end = dimensions.wdsSpacingSingle,
                    bottom = dimensions.wdsSpacingSingle,
                    top = 40.dp
                ),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = "My status",
                style = typography.body3Emphasized,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ContactStatusCard(item: StatusItem, onClick: () -> Unit = {}) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val context = LocalContext.current
    val view = LocalView.current
    val ringColor = if (item.isViewed) colors.colorContentDeemphasized else colors.colorAccent

    Box(
        modifier = Modifier
            .size(width = 90.dp, height = 160.dp)
            .clip(RoundedCornerShape(dimensions.wdsSpacingDouble))
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onClick()
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(item.previewImageUrl)
                .crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = "${item.name}'s status",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF78909C))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x660A1014),
                            Color(0x4D0A1014),
                            Color(0x1A0A1014),
                            Color.Transparent
                        )
                    )
                )
                .padding(dimensions.wdsSpacingSingle)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, ringColor, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(item.avatarUrl)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFBDBDBD))
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x1A0A1014),
                            Color(0x4D0A1014),
                            Color(0x730A1014)
                        )
                    )
                )
                .padding(
                    start = dimensions.wdsSpacingSingle,
                    end = dimensions.wdsSpacingSingle,
                    bottom = dimensions.wdsSpacingSingle,
                    top = 40.dp
                ),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = item.name.split(" ").first(),
                style = typography.body3Emphasized,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HiddenStatusCard() {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Surface(
        modifier = Modifier.size(width = 90.dp, height = 160.dp),
        shape = RoundedCornerShape(dimensions.wdsSpacingDouble),
        color = colors.colorSurfaceEmphasized,
        border = BorderStroke(1.dp, colors.colorOutlineDeemphasized)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensions.wdsSpacingSingle)
        ) {
            Icon(
                imageVector = Icons.Outlined.VisibilityOff,
                contentDescription = "Hidden statuses",
                tint = colors.colorContentDeemphasized,
                modifier = Modifier.size(dimensions.wdsIconSizeMedium)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Hidden",
                style = typography.body3Emphasized,
                color = colors.colorContentDeemphasized,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// endregion

// region Followed Channels

@Composable
private fun FollowedChannelRow(channel: ChannelItem) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = dimensions.wdsSpacingDouble, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(channel.avatarData)
                .crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = channel.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(channel.avatarColor)
        )

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = channel.name,
                        style = typography.chatListTitle,
                        color = colors.colorContentDefault,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (channel.isVerified) {
                        Icon(
                            imageVector = Icons.Filled.Verified,
                            contentDescription = "Verified",
                            tint = colors.colorAccent,
                            modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                        )
                    }
                }
                Text(
                    text = channel.time,
                    style = typography.body3,
                    color = if (channel.unreadCount > 0) colors.colorAccent else colors.colorContentDeemphasized
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = channel.lastMessage,
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (channel.unreadCount > 0) {
                    Spacer(modifier = Modifier.width(dimensions.wdsSpacingSingle))
                    Badge(
                        containerColor = colors.colorAccent,
                        contentColor = colors.colorContentOnAccent
                    ) {
                        Text(
                            text = channel.unreadCount.toString(),
                            style = typography.body3Emphasized,
                            color = colors.colorContentOnAccent
                        )
                    }
                }
            }
        }
    }
}

// endregion

// region Recommended Channels

@Composable
private fun RecommendedChannelRow(channel: RecommendedChannelItem) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = dimensions.wdsSpacingDouble, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingDouble)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(channel.avatarData)
                .crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = channel.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(channel.avatarColor)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = channel.name,
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (channel.isVerified) {
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = "Verified",
                        tint = colors.colorAccent,
                        modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                    )
                }
            }
            Text(
                text = channel.followerCount,
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                maxLines = 1
            )
        }

        WDSButton(
            onClick = { },
            text = "Follow",
            variant = WDSButtonVariant.TONAL
        )
    }
}

// endregion

// region Action Buttons

@Composable
private fun ChannelActionButtons() {
    val dimensions = WdsTheme.dimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSingle
            ),
        verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WDSButton(
            onClick = { },
            text = "Explore more",
            variant = WDSButtonVariant.OUTLINE,
            modifier = Modifier.fillMaxWidth()
        )
        WDSButton(
            onClick = { },
            text = "Create channel",
            variant = WDSButtonVariant.OUTLINE,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// endregion

// region FABs

@Composable
private fun UpdatesFabs() {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingDouble)
    ) {
        Surface(
            onClick = { },
            color = colors.colorSurfaceEmphasized,
            shape = WdsTheme.shapes.singlePlus,
            shadowElevation = 6.dp,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit status",
                    tint = colors.colorContentDefault,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                )
            }
        }

        FloatingActionButton(
            onClick = { },
            containerColor = colors.colorAccent,
            contentColor = colors.colorContentOnAccent,
            shape = WdsTheme.shapes.double
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_a_photo),
                contentDescription = "Add photo status",
                modifier = Modifier.size(dimensions.wdsIconSizeMedium)
            )
        }
    }
}

// endregion
