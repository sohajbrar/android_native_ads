package com.example.chatapp.features.chatinfo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.chatapp.data.local.entity.UserEntity
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun ProfileHeader(
    user: UserEntity?,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    // Profile section
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WdsTheme.colors.colorSurfaceDefault)
            .padding(bottom = WdsTheme.dimensions.wdsSpacingSingle),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar - 120dp as per Figma
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        ) {
            val avatarUrl = user?.avatarUrl
            
            when {
                avatarUrl != null && avatarUrl.startsWith("drawable://") -> {
                    // Handle local drawable resources
                    val drawableResId = when (avatarUrl.removePrefix("drawable://")) {
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
                            contentDescription = "Profile picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Fallback to initials
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(WdsTheme.colors.colorSurfaceHighlight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user?.displayName?.firstOrNull()?.toString() ?: "?",
                                style = MaterialTheme.typography.headlineLarge,
                                color = WdsTheme.colors.colorContentDeemphasized
                            )
                        }
                    }
                }
                avatarUrl != null && avatarUrl.isNotEmpty() -> {
                    // Handle network URLs
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    // Fallback to initials
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(WdsTheme.colors.colorSurfaceHighlight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user?.displayName?.firstOrNull()?.toString() ?: "?",
                            style = MaterialTheme.typography.headlineLarge,
                            color = WdsTheme.colors.colorContentDeemphasized
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingDouble))

        // Name
        Text(
            text = user?.displayName ?: "Unknown",
            style = WdsTheme.typography.headline1,
            color = WdsTheme.colors.colorContentDefault
        )

        // Phone number
        user?.username?.let { phone ->
            Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingHalf))
            Text(
                text = phone,
                style = WdsTheme.typography.body1,
                color = WdsTheme.colors.colorContentDeemphasized
            )
        }
    }
}