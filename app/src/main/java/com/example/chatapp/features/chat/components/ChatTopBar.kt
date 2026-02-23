package com.example.chatapp.features.chat.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ChatTopBar(
    conversationTitle: String,
    conversationAvatar: String?,
    isOnline: Boolean,
    lastSeen: Instant? = null,
    lastSeenText: String? = null,
    isGroupChat: Boolean = false,
    participantNames: List<String> = emptyList(),
    onlineParticipantCount: Int = 0,
    onBackClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    onAudioCallClick: () -> Unit,
    onMoreClick: () -> Unit,
    onHeaderClick: () -> Unit = {},
    showInsightsButton: Boolean = false,
    onInsightsClick: () -> Unit = {},
    hideBackButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors

    val titleOffset by animateIntAsState(
        targetValue = if (!hideBackButton) 0 else -40,
        animationSpec = spring(
            dampingRatio = 0.95f,
            stiffness = Spring.StiffnessLow
        ),
        label = "title_offset"
    )

    WDSTopBar(
        titleContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = titleOffset.dp)
                    .clickable { onHeaderClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChatTopBarAvatar(
                    conversationAvatar = conversationAvatar,
                    conversationTitle = conversationTitle,
                    isGroupChat = isGroupChat
                )

                Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingSingle))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = conversationTitle,
                        style = WdsTheme.typography.chatHeaderTitle,
                        color = colors.colorContentDefault,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val subtitleText = if (isGroupChat) {
                        when {
                            onlineParticipantCount > 0 -> "$onlineParticipantCount online"
                            participantNames.isNotEmpty() -> participantNames.joinToString(", ")
                            else -> "tap here for group info"
                        }
                    } else {
                        when {
                            isOnline -> "online"
                            lastSeenText != null -> lastSeenText
                            lastSeen != null -> {
                                val formatter = DateTimeFormatter.ofPattern("h:mm a")
                                    .withZone(ZoneId.systemDefault())
                                "last seen at ${formatter.format(lastSeen)}"
                            }
                            else -> "tap here for contact info"
                        }
                    }

                    Text(
                        text = subtitleText,
                        style = WdsTheme.typography.body3,
                        color = WdsTheme.colors.colorContentDeemphasized,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            val backButtonAlpha by animateFloatAsState(
                targetValue = if (!hideBackButton) 1f else 0f,
                animationSpec = spring(
                    dampingRatio = 0.95f,
                    stiffness = Spring.StiffnessLow
                ),
                label = "back_button_alpha"
            )

            val backButtonScale by animateFloatAsState(
                targetValue = if (!hideBackButton) 1f else 0.8f,
                animationSpec = spring(
                    dampingRatio = 0.95f,
                    stiffness = Spring.StiffnessLow
                ),
                label = "back_button_scale"
            )

            val backButtonOffset by animateIntAsState(
                targetValue = if (!hideBackButton) 0 else -40,
                animationSpec = spring(
                    dampingRatio = 0.95f,
                    stiffness = Spring.StiffnessLow
                ),
                label = "back_button_offset"
            )

            Box(
                modifier = Modifier
                    .alpha(backButtonAlpha)
                    .offset(x = backButtonOffset.dp)
                    .graphicsLayer {
                        scaleX = backButtonScale
                        scaleY = backButtonScale
                    }
            ) {
                IconButton(
                    onClick = onBackClick,
                    enabled = !hideBackButton
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.colorContentDefault
                    )
                }
            }
        },
        actions = {
            ChatTopBarActions(
                colors = colors,
                showInsightsButton = showInsightsButton,
                onInsightsClick = onInsightsClick,
                onVideoCallClick = onVideoCallClick,
                onAudioCallClick = onAudioCallClick,
                onMoreClick = onMoreClick
            )
        },
        modifier = modifier
    )
}

@Composable
private fun ChatTopBarAvatar(
    conversationAvatar: String?,
    conversationTitle: String,
    isGroupChat: Boolean
) {
    when {
        conversationAvatar != null && conversationAvatar.startsWith("drawable://") -> {
            val drawableResId = when (conversationAvatar.removePrefix("drawable://")) {
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
                    contentDescription = "Group Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                GroupPlaceholderAvatar()
            }
        }
        conversationAvatar != null && conversationAvatar.isNotEmpty() -> {
            AsyncImage(
                model = conversationAvatar,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        else -> {
            if (isGroupChat) {
                GroupPlaceholderAvatar()
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(WdsTheme.colors.colorSurfaceEmphasized),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversationTitle.take(1).uppercase(),
                        style = WdsTheme.typography.body1Emphasized,
                        color = WdsTheme.colors.colorContentDefault
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupPlaceholderAvatar() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(WdsTheme.colors.colorSurfaceEmphasized),
        contentAlignment = Alignment.Center
    ) {
        val groupIcon = remember {
            ImageVector.Builder(
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(
                    fill = SolidColor(Color(0xFF757575)),
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(12f, 12f)
                    curveTo(14.21f, 12f, 16f, 10.21f, 16f, 8f)
                    reflectiveCurveTo(14.21f, 4f, 12f, 4f)
                    reflectiveCurveTo(8f, 5.79f, 8f, 8f)
                    reflectiveCurveTo(9.79f, 12f, 12f, 12f)
                    close()
                    moveTo(12f, 14f)
                    curveTo(9.33f, 14f, 4f, 15.34f, 4f, 18f)
                    verticalLineTo(20f)
                    horizontalLineTo(20f)
                    verticalLineTo(18f)
                    curveTo(20f, 15.34f, 14.67f, 14f, 12f, 14f)
                    close()
                }
            }.build()
        }

        Icon(
            imageVector = groupIcon,
            contentDescription = "Group",
            modifier = Modifier.size(24.dp),
            tint = WdsTheme.colors.colorContentDeemphasized
        )
    }
}

@Composable
private fun ChatTopBarActions(
    colors: com.example.chatapp.wds.theme.WdsColorScheme,
    showInsightsButton: Boolean,
    onInsightsClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    onAudioCallClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    val insightsButtonAlpha by animateFloatAsState(
        targetValue = if (showInsightsButton) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.95f,
            stiffness = Spring.StiffnessLow
        ),
        label = "insights_button_alpha"
    )

    val insightsButtonScale by animateFloatAsState(
        targetValue = if (showInsightsButton) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = 0.95f,
            stiffness = Spring.StiffnessLow
        ),
        label = "insights_button_scale"
    )

    if (showInsightsButton || insightsButtonAlpha > 0.01f) {
        Box(
            modifier = Modifier
                .alpha(insightsButtonAlpha)
                .graphicsLayer {
                    scaleX = insightsButtonScale
                    scaleY = insightsButtonScale
                }
        ) {
            IconButton(
                onClick = onInsightsClick,
                enabled = showInsightsButton
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_insights),
                    contentDescription = "Insights",
                    tint = colors.colorContentDefault
                )
            }
        }
    }

    val videoCallIcon = remember {
        ImageVector.Builder(
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4f, 20f)
                curveTo(3.45f, 20f, 2.97917f, 19.8042f, 2.5875f, 19.4125f)
                curveTo(2.19583f, 19.0208f, 2f, 18.55f, 2f, 18f)
                verticalLineTo(6f)
                curveTo(2f, 5.45f, 2.19583f, 4.97917f, 2.5875f, 4.5875f)
                curveTo(2.97917f, 4.19583f, 3.45f, 4f, 4f, 4f)
                horizontalLineTo(16f)
                curveTo(16.55f, 4f, 17.0208f, 4.19583f, 17.4125f, 4.5875f)
                curveTo(17.8042f, 4.97917f, 18f, 5.45f, 18f, 6f)
                verticalLineTo(10.5f)
                lineTo(21.15f, 7.35f)
                curveTo(21.3167f, 7.18333f, 21.5f, 7.14167f, 21.7f, 7.225f)
                curveTo(21.9f, 7.30833f, 22f, 7.46667f, 22f, 7.7f)
                verticalLineTo(16.3f)
                curveTo(22f, 16.5333f, 21.9f, 16.6917f, 21.7f, 16.775f)
                curveTo(21.5f, 16.8583f, 21.3167f, 16.8167f, 21.15f, 16.65f)
                lineTo(18f, 13.5f)
                verticalLineTo(18f)
                curveTo(18f, 18.55f, 17.8042f, 19.0208f, 17.4125f, 19.4125f)
                curveTo(17.0208f, 19.8042f, 16.55f, 20f, 16f, 20f)
                horizontalLineTo(4f)
                close()
                moveTo(4f, 18f)
                horizontalLineTo(16f)
                verticalLineTo(6f)
                horizontalLineTo(4f)
                verticalLineTo(18f)
                close()
            }
        }.build()
    }

    val phoneCallIcon = remember {
        ImageVector.Builder(
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19.95f, 21f)
                curveTo(17.8667f, 21f, 15.8083f, 20.5458f, 13.775f, 19.6375f)
                curveTo(11.7417f, 18.7292f, 9.89167f, 17.4417f, 8.225f, 15.775f)
                curveTo(6.55833f, 14.1083f, 5.27083f, 12.2583f, 4.3625f, 10.225f)
                curveTo(3.45417f, 8.19167f, 3f, 6.13333f, 3f, 4.05f)
                curveTo(3f, 3.75f, 3.1f, 3.5f, 3.3f, 3.3f)
                curveTo(3.5f, 3.1f, 3.75f, 3f, 4.05f, 3f)
                horizontalLineTo(8.1f)
                curveTo(8.33333f, 3f, 8.54167f, 3.07917f, 8.725f, 3.2375f)
                curveTo(8.90833f, 3.39583f, 9.01667f, 3.58333f, 9.05f, 3.8f)
                lineTo(9.7f, 7.3f)
                curveTo(9.73333f, 7.56667f, 9.725f, 7.79167f, 9.675f, 7.975f)
                curveTo(9.625f, 8.15833f, 9.53333f, 8.31667f, 9.4f, 8.45f)
                lineTo(6.975f, 10.9f)
                curveTo(7.30833f, 11.5167f, 7.70417f, 12.1125f, 8.1625f, 12.6875f)
                curveTo(8.62083f, 13.2625f, 9.125f, 13.8167f, 9.675f, 14.35f)
                curveTo(10.1917f, 14.8667f, 10.7333f, 15.3458f, 11.3f, 15.7875f)
                curveTo(11.8667f, 16.2292f, 12.4667f, 16.6333f, 13.1f, 17f)
                lineTo(15.45f, 14.65f)
                curveTo(15.6f, 14.5f, 15.7958f, 14.3875f, 16.0375f, 14.3125f)
                curveTo(16.2792f, 14.2375f, 16.5167f, 14.2167f, 16.75f, 14.25f)
                lineTo(20.2f, 14.95f)
                curveTo(20.4333f, 15.0167f, 20.625f, 15.1375f, 20.775f, 15.3125f)
                curveTo(20.925f, 15.4875f, 21f, 15.6833f, 21f, 15.9f)
                verticalLineTo(19.95f)
                curveTo(21f, 20.25f, 20.9f, 20.5f, 20.7f, 20.7f)
                curveTo(20.5f, 20.9f, 20.25f, 21f, 19.95f, 21f)
                close()
                moveTo(6.025f, 9f)
                lineTo(7.675f, 7.35f)
                lineTo(7.25f, 5f)
                horizontalLineTo(5.025f)
                curveTo(5.10833f, 5.68333f, 5.225f, 6.35833f, 5.375f, 7.025f)
                curveTo(5.525f, 7.69167f, 5.74167f, 8.35f, 6.025f, 9f)
                close()
                moveTo(14.975f, 17.95f)
                curveTo(15.625f, 18.2333f, 16.2875f, 18.4583f, 16.9625f, 18.625f)
                curveTo(17.6375f, 18.7917f, 18.3167f, 18.9f, 19f, 18.95f)
                verticalLineTo(16.75f)
                lineTo(16.65f, 16.275f)
                lineTo(14.975f, 17.95f)
                close()
            }
        }.build()
    }

    if (showInsightsButton) {
        var showCallOptions by remember { mutableStateOf(false) }
        Box {
            IconButton(onClick = { showCallOptions = !showCallOptions }) {
                Icon(
                    imageVector = videoCallIcon,
                    contentDescription = "Call",
                    tint = colors.colorContentDefault
                )
            }

            DropdownMenu(
                expanded = showCallOptions,
                onDismissRequest = { showCallOptions = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Video call") },
                    onClick = {
                        showCallOptions = false
                        onVideoCallClick()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = videoCallIcon,
                            contentDescription = "Video Call",
                            tint = colors.colorContentDefault
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text("Audio call") },
                    onClick = {
                        showCallOptions = false
                        onAudioCallClick()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = phoneCallIcon,
                            contentDescription = "Audio Call",
                            tint = colors.colorContentDefault
                        )
                    }
                )
            }
        }
    } else {
        IconButton(onClick = onVideoCallClick) {
            Icon(
                imageVector = videoCallIcon,
                contentDescription = "Video Call",
                tint = colors.colorContentDefault
            )
        }
        IconButton(onClick = onAudioCallClick) {
            Icon(
                imageVector = phoneCallIcon,
                contentDescription = "Audio Call",
                tint = colors.colorContentDefault
            )
        }
    }

    IconButton(onClick = onMoreClick) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More Options",
            tint = colors.colorContentDefault
        )
    }
}
