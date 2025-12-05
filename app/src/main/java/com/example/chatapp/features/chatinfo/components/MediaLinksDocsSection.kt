package com.example.chatapp.features.chatinfo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.data.local.entity.MessageType
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun MediaLinksDocsSection(
    photoCount: Int,
    videoCount: Int,
    docCount: Int,
    mediaMessages: List<MessageEntity>,
    onViewAll: () -> Unit
) {
    val colors = WdsTheme.colors

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewAll() }
                .padding(horizontal = WdsTheme.dimensions.wdsSpacingTriple, vertical = WdsTheme.dimensions.wdsSpacingDouble),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Media, links and docs",
                    style = WdsTheme.typography.body2,
                    color = colors.colorContentDeemphasized
                )
                Text(
                    text = when {
                        photoCount + videoCount + docCount == 0 -> "No media"
                        else -> buildString {
                            val items = mutableListOf<String>()
                            if (photoCount > 0) items.add("$photoCount photo${if (photoCount > 1) "s" else ""}")
                            if (videoCount > 0) items.add("$videoCount video${if (videoCount > 1) "s" else ""}")
                            if (docCount > 0) items.add("$docCount document${if (docCount > 1) "s" else ""}")
                            append(items.joinToString(", "))
                        }
                    },
                    style = WdsTheme.typography.body1,
                    color = colors.colorContentDefault
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = "View all",
                tint = colors.colorContentDeemphasized
            )
        }

        // Media preview - show up to 4 items
        if (mediaMessages.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = WdsTheme.dimensions.wdsSpacingTriple)
                    .padding(top = WdsTheme.dimensions.wdsSpacingSingle, bottom = WdsTheme.dimensions.wdsSpacingDouble),
                horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
            ) {
                items(
                    items = mediaMessages.take(4),
                    key = { it.messageId }
                ) { message ->
                    MediaThumbnail(message = message)
                }

                // Show remaining count if more than 4
                if (mediaMessages.size > 4) {
                    item {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(WdsTheme.shapes.halfPlus)
                                .background(colors.colorSurfaceEmphasized)
                                .clickable { onViewAll() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+${mediaMessages.size - 4}",
                                style = WdsTheme.typography.headline2,
                                color = colors.colorContentDeemphasized
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaThumbnail(message: MessageEntity) {
    val colors = WdsTheme.colors

    Box(
        modifier = Modifier
            .size(88.dp)
            .clip(WdsTheme.shapes.halfPlus)
            .background(colors.colorSurfaceEmphasized)
    ) {
        when (message.messageType) {
            MessageType.IMAGE -> {
                // In a real app, you'd load the actual image
                AsyncImage(
                    model = message.content, // Assuming content contains image URL
                    contentDescription = "Photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            MessageType.VIDEO -> {
                // Video thumbnail placeholder
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.colorSurfaceEmphasized),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶",
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.colorContentDefault
                    )
                }
            }
            MessageType.FILE -> {
                // Document thumbnail
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.colorSurfaceElevatedDefault),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📄",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            else -> {
                // Other types
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.colorSurfaceEmphasized)
                )
            }
        }
    }
}