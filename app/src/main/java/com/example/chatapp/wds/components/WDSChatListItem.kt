package com.example.chatapp.wds.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.chatapp.R
import com.example.chatapp.wds.theme.WdsTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

/**
 * WDS Chat List Item Component
 *
 * A reusable component for displaying conversation previews in chat lists.
 * Supports both individual and group chats with various message types.
 */
@Composable
fun WDSChatListItem(
    title: String,
    subtitle: String? = null,
    avatarUrl: String?,
    lastMessage: String,
    lastMessageTime: Instant,
    lastMessageSender: String? = null,
    lastMessageType: MessageType = MessageType.TEXT,
    unreadCount: Int = 0,
    isGroup: Boolean = false,
    isSentByUser: Boolean = false,
    isRead: Boolean = false,
    isMissedCall: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Cache theme lookups
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
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingDouble),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        ChatListAvatar(
            avatarUrl = avatarUrl,
            title = title
        )

        // Content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
                ) {
                    Text(
                        text = title,
                        style = typography.chatListTitle,
                        color = colors.colorContentDefault,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Show subtitle if present (for disambiguation)
                    subtitle?.let { sub ->
                        Text(
                            text = sub,
                            style = typography.body3,
                            color = colors.colorContentDeemphasized,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Text(
                    text = formatChatTime(lastMessageTime),
                    style = if (unreadCount > 0) typography.body3Emphasized else typography.body3,
                    color = if (unreadCount > 0) colors.colorAlwaysBranded else colors.colorContentDeemphasized
                )
            }

            // Message Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status icon (if sent by user)
                if (isSentByUser) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = if (isRead) colors.colorContentRead else colors.colorContentDeemphasized,
                        modifier = Modifier.size(dimensions.wdsIconSizeSmall)
                    )
                }

                // Sender name (for groups)
                if (isGroup && lastMessageSender != null) {
                    Text(
                        text = "$lastMessageSender:",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                }

                // Message content
                when (lastMessageType) {
                    MessageType.PHOTO -> {
                        Icon(
                            painter = painterResource(R.drawable.ic_photo_message),
                            contentDescription = null,
                            tint = colors.colorContentDeemphasized,
                            modifier = Modifier.size(dimensions.wdsIconSizeSmall)
                        )
                        Text(
                            text = "Photo",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    MessageType.VIDEO -> {
                        Icon(
                            imageVector = Icons.Outlined.Videocam,
                            contentDescription = null,
                            tint = colors.colorContentDeemphasized,
                            modifier = Modifier.size(dimensions.wdsIconSizeSmall)
                        )
                        Text(
                            text = "Video",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    MessageType.STICKER -> {
                        Icon(
                            painter = painterResource(R.drawable.ic_sticker_message),
                            contentDescription = null,
                            tint = colors.colorContentDeemphasized,
                            modifier = Modifier.size(dimensions.wdsIconSizeSmall)
                        )
                        Text(
                            text = "Sticker",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    MessageType.FILE, MessageType.DOCUMENT -> {
                        Icon(
                            painter = painterResource(R.drawable.ic_file_message),
                            contentDescription = null,
                            tint = colors.colorContentDeemphasized,
                            modifier = Modifier.size(dimensions.wdsIconSizeSmall)
                        )
                        Text(
                            text = lastMessage
                                .removePrefix("📎 ")
                                .removePrefix("🔗 ")
                                .ifEmpty { "Document" },
                            style = typography.body2,
                            color = colors.colorContentDeemphasized,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    MessageType.VOICE_CALL, MessageType.VIDEO_CALL -> {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = if (isMissedCall) colors.colorNegative else colors.colorContentDeemphasized,
                            modifier = Modifier.size(dimensions.wdsIconSizeSmall)
                        )
                        Text(
                            text = if (isMissedCall) "Missed Call" else "Call",
                            style = typography.body2,
                            color = if (isMissedCall) colors.colorNegative else colors.colorContentDeemphasized,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    else -> {
                        Text(
                            text = lastMessage,
                            style = typography.body2,
                            color = colors.colorContentDeemphasized,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Unread badge
                if (unreadCount > 0) {
                    Surface(
                        shape = CircleShape,
                        color = colors.colorAlwaysBranded
                    ) {
                        Text(
                            text = unreadCount.toString(),
                            style = typography.body3Emphasized,
                            color = colors.colorContentOnAccent,
                            modifier = Modifier.padding(
                                horizontal = dimensions.wdsSpacingHalfPlus,
                                vertical = dimensions.wdsSpacingQuarter
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatListAvatar(
    avatarUrl: String?,
    title: String,
    modifier: Modifier = Modifier
) {
    val dimensions = WdsTheme.dimensions
    val colors = WdsTheme.colors
    var imageLoadFailed by remember { mutableStateOf(false) }
    
    // Check if title is a phone number (starts with + or contains only digits, spaces, dashes, parentheses)
    val isPhoneNumber = title.matches(Regex("^[+]?[0-9\\s()\\-]+$"))

    if (avatarUrl != null && avatarUrl.isNotEmpty() && !imageLoadFailed) {
        when {
            avatarUrl == "broadcast://" -> {
                Surface(
                    shape = CircleShape,
                    color = colors.colorBroadcastAvatar,
                    modifier = modifier.size(dimensions.wdsAvatarMediumLarge)
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
            }
            avatarUrl.startsWith("drawable://") -> {
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
                        contentDescription = "Profile picture of $title",
                        modifier = modifier
                            .size(dimensions.wdsAvatarMediumLarge)
                            .clip(CircleShape)
                            .background(colors.colorSurfaceEmphasized, CircleShape),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    DefaultChatAvatar(title = title, modifier = modifier, isPhoneNumber = isPhoneNumber)
                }
            }
            else -> {
                // Handle network URLs
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(avatarUrl)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .allowHardware(true)
                        .build(),
                    contentDescription = "Profile picture of $title",
                    modifier = modifier
                        .size(dimensions.wdsAvatarMediumLarge)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    onError = {
                        imageLoadFailed = true
                    }
                )
            }
        }
    } else {
        // Fallback to placeholder for phone numbers or solid color avatar with initials
        DefaultChatAvatar(title = title, modifier = modifier, isPhoneNumber = isPhoneNumber)
    }
}

@Composable
private fun DefaultChatAvatar(
    title: String,
    modifier: Modifier = Modifier,
    isPhoneNumber: Boolean = false
) {
    val dimensions = WdsTheme.dimensions
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography

    // Use placeholder avatar for phone numbers
    if (isPhoneNumber) {
        Image(
            painter = painterResource(id = R.drawable.avatar_contact_placeholder),
            contentDescription = "Contact placeholder for $title",
            modifier = modifier
                .size(dimensions.wdsAvatarMediumLarge)
                .clip(CircleShape)
                .background(colors.colorSurfaceEmphasized, CircleShape),
            contentScale = ContentScale.Fit
        )
    } else {
        // Use initials for named contacts
        Surface(
            modifier = modifier
                .size(dimensions.wdsAvatarMediumLarge)
                .clip(CircleShape),
            color = colors.colorSurfaceEmphasized
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title.take(1).uppercase(),
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault
                )
            }
        }
    }
}

@Composable
fun EnhancedChatAvatar(
    title: String,
    seed: String,
    modifier: Modifier = Modifier
) {
    val dimensions = WdsTheme.dimensions
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography

    // Generate gradient colors based on seed for variety
    val gradientColors = generateGradientColors(seed)
    val initials = generateInitials(title)

    Box(
        modifier = modifier
            .size(dimensions.wdsAvatarMediumLarge)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, 0f),
                    end = Offset(100f, 100f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = typography.headline2,
            color = colors.colorAlwaysWhite
        )
    }
}

private fun generateInitials(name: String): String {
    val parts = name.trim().split(" ")
    return when {
        parts.size >= 2 -> "${parts[0].take(1)}${parts[1].take(1)}".uppercase()
        parts[0].length >= 2 -> parts[0].take(2).uppercase()
        else -> parts[0].take(1).uppercase()
    }
}

@Composable
private fun generateGradientColors(seed: String): List<Color> {
    val hash = seed.hashCode().absoluteValue
    val colors = WdsTheme.colors

    val colorSchemes = listOf(
        // Using WDS semantic colors for avatar gradients
        listOf(colors.colorAccentEmphasized, colors.colorAccentDeemphasized),
        listOf(colors.colorPositive, colors.colorAccentEmphasized),
        listOf(colors.colorAccentEmphasized, colors.colorPositive),
        listOf(colors.colorWarning, colors.colorAccentEmphasized),
        listOf(colors.colorAccentDeemphasized, colors.colorPositive),
        listOf(colors.colorPositive, colors.colorAccentDeemphasized),
        listOf(colors.colorAccentEmphasized, colors.colorWarning),
        listOf(colors.colorWarning, colors.colorPositive)
    )

    return colorSchemes[hash % colorSchemes.size]
}

private fun formatChatTime(instant: Instant): String {
    val localDateTime = LocalDateTime.ofInstant(instant.toJavaInstant(), ZoneId.systemDefault())
    val now = LocalDateTime.now()

    return when {
        localDateTime.toLocalDate() == now.toLocalDate() -> {
            // Today - show time with AM/PM
            localDateTime.format(DateTimeFormatter.ofPattern("h:mm a"))
        }
        localDateTime.toLocalDate() == now.toLocalDate().minusDays(1) -> {
            // Yesterday
            "Yesterday"
        }
        else -> {
            // Show date
            localDateTime.format(DateTimeFormatter.ofPattern("M/d/yy"))
        }
    }
}

// Message type enum for the component
enum class MessageType {
    TEXT, PHOTO, VIDEO, AUDIO, DOCUMENT, LOCATION, VOICE_CALL, VIDEO_CALL, FILE, STICKER, GIF, VOICE_NOTE
}
