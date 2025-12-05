package com.example.chatapp.features.chatinfo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.data.local.entity.ConversationEntity
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun GroupInfoHeader(
    conversation: ConversationEntity?,
    participantCount: Int,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    // Group info section
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WdsTheme.colors.colorSurfaceDefault)
            .padding(bottom = WdsTheme.dimensions.wdsSpacingSingle),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Group Avatar - 120dp as per Figma
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        ) {
            when {
                conversation?.avatarUrl != null && conversation.avatarUrl.startsWith("drawable://") -> {
                    // Handle local drawable resources for groups
                    val drawableResId = when (conversation.avatarUrl.removePrefix("drawable://")) {
                        "emoji_thinking_monocle" -> R.drawable.emoji_thinking_monocle
                        "emoji_microscope" -> R.drawable.emoji_microscope
                        "avatar_contact_placeholder" -> R.drawable.avatar_contact_placeholder
                        "avatar_faith" -> R.drawable.avatar_faith
                        "avatar_maria" -> R.drawable.avatar_maria
                        "avatar_anika" -> R.drawable.avatar_anika
                        "avatar_gerald" -> R.drawable.avatar_gerald
                        "avatar_yuna" -> R.drawable.avatar_yuna
                        else -> null
                    }

                    if (drawableResId != null) {
                        Image(
                            painter = painterResource(id = drawableResId),
                            contentDescription = "Group Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        GroupAvatarPlaceholder()
                    }
                }
                conversation?.avatarUrl != null -> {
                    AsyncImage(
                        model = conversation.avatarUrl,
                        contentDescription = "Group picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    GroupAvatarPlaceholder()
                }
            }
        }

        Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingDouble))

        // Group name
        Text(
            text = conversation?.title ?: "Unknown Group",
            style = WdsTheme.typography.headline1,
            color = WdsTheme.colors.colorContentDefault
        )

        // Member count
        Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingHalf))
        Text(
            text = when (participantCount) {
                0 -> "Group"
                1 -> "Group · 1 member"
                else -> "Group · $participantCount members"
            },
            style = WdsTheme.typography.body1,
            color = WdsTheme.colors.colorContentDeemphasized,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GroupAvatarPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WdsTheme.colors.colorSurfaceHighlight),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Group,
            contentDescription = "Group",
            modifier = Modifier.size(60.dp),
            tint = WdsTheme.colors.colorContentDeemphasized
        )
    }
}