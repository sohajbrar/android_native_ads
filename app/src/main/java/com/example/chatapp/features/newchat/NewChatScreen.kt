@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.newchat

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.chatapp.R
import com.example.chatapp.data.local.entity.UserEntity
import com.example.chatapp.wds.components.WDSSearchBar
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.components.WdsDialog
import com.example.chatapp.wds.components.WdsDialogButton
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun NewChatScreen(
    onNavigateBack: () -> Unit = {},
    onContactClick: (String) -> Unit = {},
    onNewGroupClick: () -> Unit = {},
    onNewContactClick: () -> Unit = {},
    onNewBroadcastClick: () -> Unit = {},
    viewModel: NewChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NewChatContent(
        contacts = uiState.displayContacts,
        searchQuery = uiState.searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onNavigateBack = onNavigateBack,
        onContactClick = onContactClick,
        onNewGroupClick = onNewGroupClick,
        onNewContactClick = onNewContactClick,
        onNewBroadcastClick = onNewBroadcastClick
    )
}

@Composable
private fun NewChatContent(
    contacts: List<UserEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onContactClick: (String) -> Unit,
    onNewGroupClick: () -> Unit,
    onNewContactClick: () -> Unit,
    onNewBroadcastClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    var showComingSoon by remember { mutableStateOf(false) }

    if (showComingSoon) {
        WdsDialog(
            title = "This feature is coming soon.",
            positiveButton = WdsDialogButton(
                text = "OK",
                onClick = { showComingSoon = false }
            ),
            onDismissRequest = { showComingSoon = false }
        )
    }

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "New chat",
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
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            item {
                WDSSearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    placeholder = "Name, number, username",
                    modifier = Modifier.padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSingle
                    )
                )
            }

            // Action items
            item {
                NewChatActionRow(
                    icon = Icons.Filled.GroupAdd,
                    text = "New group",
                    onClick = { showComingSoon = true }
                )
            }

            item {
                NewChatActionRow(
                    icon = Icons.Filled.PersonAdd,
                    text = "New contact",
                    onClick = { showComingSoon = true },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Outlined.QrCode2,
                            contentDescription = "Scan QR code",
                            modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                            tint = colors.colorContentDefault
                        )
                    }
                )
            }

            item {
                NewChatActionRow(
                    iconPainter = painterResource(R.drawable.ic_business_broadcast),
                    text = "New business broadcast",
                    onClick = onNewBroadcastClick
                )
            }

            // Section header
            item {
                Text(
                    text = "Contacts on WhatsApp",
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    modifier = Modifier.padding(
                        start = dimensions.wdsSpacingDouble,
                        end = dimensions.wdsSpacingDouble,
                        top = dimensions.wdsSpacingDouble,
                        bottom = dimensions.wdsSpacingSingle
                    )
                )
            }

            // Contact list
            items(
                items = contacts,
                key = { it.userId }
            ) { user ->
                ContactRow(
                    user = user,
                    onClick = { showComingSoon = true }
                )
            }
        }
    }
}

@Composable
private fun NewChatActionRow(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    trailingContent: @Composable (() -> Unit)? = null
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
                when {
                    icon != null -> Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                        tint = colors.colorAlwaysWhite
                    )
                    iconPainter != null -> Icon(
                        painter = iconPainter,
                        contentDescription = null,
                        modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                        tint = colors.colorAlwaysWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Text(
            text = text,
            style = typography.body1,
            color = colors.colorContentDefault,
            modifier = Modifier.weight(1f)
        )

        if (trailingContent != null) {
            trailingContent()
        }
    }
}

/**
 * Contact row displaying avatar, name, and status.
 */
@Composable
private fun ContactRow(
    user: UserEntity,
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
        ContactAvatar(
            avatarUrl = user.avatarUrl,
            displayName = user.displayName
        )

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
        ) {
            Text(
                text = user.displayName,
                style = typography.body1,
                color = colors.colorContentDefault,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            user.statusMessage?.let { status ->
                Text(
                    text = status,
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ContactAvatar(
    avatarUrl: String?,
    displayName: String,
    modifier: Modifier = Modifier
) {
    val dimensions = WdsTheme.dimensions
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography
    var imageLoadFailed by remember { mutableStateOf(false) }

    val avatarSize = dimensions.wdsAvatarMediumLarge

    if (!avatarUrl.isNullOrEmpty() && !imageLoadFailed) {
        when {
            avatarUrl.startsWith("drawable://") -> {
                val drawableResId = resolveDrawableAvatar(avatarUrl.removePrefix("drawable://"))
                if (drawableResId != null) {
                    Image(
                        painter = painterResource(id = drawableResId),
                        contentDescription = "Profile picture of $displayName",
                        modifier = modifier
                            .size(avatarSize)
                            .clip(CircleShape)
                            .background(colors.colorSurfaceEmphasized, CircleShape),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    InitialsAvatar(displayName = displayName, modifier = modifier)
                }
            }
            else -> {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(avatarUrl)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "Profile picture of $displayName",
                    modifier = modifier
                        .size(avatarSize)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    onError = { imageLoadFailed = true }
                )
            }
        }
    } else {
        InitialsAvatar(displayName = displayName, modifier = modifier)
    }
}

@Composable
private fun InitialsAvatar(
    displayName: String,
    modifier: Modifier = Modifier
) {
    val dimensions = WdsTheme.dimensions
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography

    Surface(
        modifier = modifier
            .size(dimensions.wdsAvatarMediumLarge)
            .clip(CircleShape),
        color = colors.colorSurfaceEmphasized
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = displayName.take(1).uppercase(),
                style = typography.body1Emphasized,
                color = colors.colorContentDefault
            )
        }
    }
}

private fun resolveDrawableAvatar(name: String): Int? {
    return when (name) {
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
}

@Preview(showBackground = true, name = "New Chat - Light")
@Composable
private fun NewChatScreenPreviewLight() {
    WdsTheme(darkTheme = false) {
        val sampleContacts = listOf(
            UserEntity(userId = "u1", username = "alice", displayName = "Alice", avatarUrl = "drawable://avatar_alice", statusMessage = "Available"),
            UserEntity(userId = "u2", username = "anna_soe", displayName = "Anna Soe", avatarUrl = "drawable://avatar_anna_soe", statusMessage = "Busy"),
            UserEntity(userId = "u3", username = "anika", displayName = "Anika Chavan", avatarUrl = "drawable://avatar_anika_chavan", statusMessage = "Available"),
            UserEntity(userId = "u4", username = "ayesha", displayName = "Ayesha", avatarUrl = "drawable://avatar_ayesha", statusMessage = "Busy"),
            UserEntity(userId = "u5", username = "jordan", displayName = "Jordan", avatarUrl = "drawable://avatar_jordan", statusMessage = "Available"),
            UserEntity(userId = "u6", username = "maria", displayName = "Maria Torres", avatarUrl = "drawable://avatar_maria", statusMessage = "Available")
        )

        NewChatContent(
            contacts = sampleContacts,
            searchQuery = "",
            onSearchQueryChange = {},
            onNavigateBack = {},
            onContactClick = {},
            onNewGroupClick = {},
            onNewContactClick = {},
            onNewBroadcastClick = {}
        )
    }
}

@Preview(showBackground = true, name = "New Chat - Dark")
@Composable
private fun NewChatScreenPreviewDark() {
    WdsTheme(darkTheme = true) {
        val sampleContacts = listOf(
            UserEntity(userId = "u1", username = "alice", displayName = "Alice", avatarUrl = "drawable://avatar_alice", statusMessage = "Available"),
            UserEntity(userId = "u2", username = "anna_soe", displayName = "Anna Soe", avatarUrl = "drawable://avatar_anna_soe", statusMessage = "Busy"),
            UserEntity(userId = "u3", username = "anika", displayName = "Anika Chavan", avatarUrl = "drawable://avatar_anika_chavan", statusMessage = "Available")
        )

        NewChatContent(
            contacts = sampleContacts,
            searchQuery = "",
            onSearchQueryChange = {},
            onNavigateBack = {},
            onContactClick = {},
            onNewGroupClick = {},
            onNewContactClick = {},
            onNewBroadcastClick = {}
        )
    }
}
