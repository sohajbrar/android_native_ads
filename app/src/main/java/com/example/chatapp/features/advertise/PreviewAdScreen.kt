@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonSize
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun PreviewAdScreen(
    onNavigateBack: () -> Unit,
    selectedMediaUri: String? = null,
    showCloseButton: Boolean = false
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    var selectedPlatform by rememberSaveable { mutableIntStateOf(1) }

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Preview ad",
                onNavigateBack = onNavigateBack,
                showCloseButton = showCloseButton
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Platform toggle buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingDouble
                    ),
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
            ) {
                WDSButton(
                    onClick = { selectedPlatform = 0 },
                    text = "Instagram",
                    variant = if (selectedPlatform == 0) WDSButtonVariant.FILLED else WDSButtonVariant.OUTLINE,
                    size = WDSButtonSize.SMALL
                )
                WDSButton(
                    onClick = { selectedPlatform = 1 },
                    text = "Facebook",
                    variant = if (selectedPlatform == 1) WDSButtonVariant.FILLED else WDSButtonVariant.OUTLINE,
                    size = WDSButtonSize.SMALL
                )
            }

            if (selectedPlatform == 1) {
                FacebookPreview(selectedMediaUri = selectedMediaUri)
            } else {
                InstagramPreview(selectedMediaUri = selectedMediaUri)
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

            // Helper text
            Text(
                text = "When potential customers tap on \"WhatsApp\", they can message you directly on WhatsApp.",
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingSingle
                )
            )
        }
    }
}

@Composable
private fun FacebookPreview(selectedMediaUri: String? = null) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.wdsSpacingDouble),
        shape = WdsTheme.shapes.double,
        colors = CardDefaults.cardColors(
            containerColor = colors.colorSurfaceDefault
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensions.wdsSpacingSinglePlus),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "android.resource://com.example.chatapp/drawable/avatar_my_status",
                    contentDescription = "Business profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(dimensions.wdsSpacingSingle))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Lucky Shrub",
                        style = typography.body2Emphasized,
                        color = colors.colorContentDefault
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
                    ) {
                        Text(
                            text = "Sponsored",
                            style = typography.body3,
                            color = colors.colorContentDeemphasized
                        )
                        Text(
                            text = "·",
                            style = typography.body3,
                            color = colors.colorContentDeemphasized
                        )
                        Icon(
                            imageVector = Icons.Outlined.Public,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = colors.colorContentDeemphasized
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = "More",
                    tint = colors.colorContentDeemphasized
                )
            }

            // Description
            Text(
                text = "Delhi's organic nursery offers the best selection of plants and tools for your weekend plant projects.",
                style = typography.body2,
                color = colors.colorContentDefault,
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingSinglePlus,
                    vertical = dimensions.wdsSpacingHalf
                )
            )

            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .background(Color(0xFFE8E5E1)),
                contentAlignment = Alignment.Center
            ) {
                if (selectedMediaUri != null) {
                    AsyncImage(
                        model = android.net.Uri.parse(selectedMediaUri),
                        contentDescription = "Ad image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "🌿🪴🌱",
                        style = typography.largeTitle1
                    )
                }
            }

            // CTA row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.colorSurfaceEmphasized)
                    .padding(
                        horizontal = dimensions.wdsSpacingSinglePlus,
                        vertical = dimensions.wdsSpacingSingle
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Succulents",
                    style = typography.body2Emphasized,
                    color = colors.colorContentDefault,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(WdsTheme.shapes.circle)
                        .background(colors.colorPositive)
                        .padding(
                            horizontal = dimensions.wdsSpacingSinglePlus,
                            vertical = dimensions.wdsSpacingHalf
                        )
                ) {
                    Text(
                        text = "WhatsApp",
                        style = typography.body3,
                        color = Color.White
                    )
                }
            }

            // Action row (Like, Comment, Share)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSinglePlus
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(icon = Icons.Outlined.ThumbUp, label = "Like")
                ActionButton(icon = Icons.Outlined.ChatBubbleOutline, label = "Comment")
                ActionButton(icon = Icons.Outlined.Share, label = "Share")
            }
        }
    }
}

@Composable
private fun InstagramPreview(selectedMediaUri: String? = null) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.wdsSpacingDouble),
        shape = WdsTheme.shapes.double,
        colors = CardDefaults.cardColors(
            containerColor = colors.colorSurfaceDefault
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensions.wdsSpacingSinglePlus),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "android.resource://com.example.chatapp/drawable/avatar_my_status",
                    contentDescription = "Business profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(dimensions.wdsSpacingSingle))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Lucky Shrub",
                        style = typography.body2Emphasized,
                        color = colors.colorContentDefault
                    )
                    Text(
                        text = "Sponsored",
                        style = typography.body3,
                        color = colors.colorContentDeemphasized
                    )
                }
                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = "More",
                    tint = colors.colorContentDeemphasized
                )
            }

            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color(0xFFE8E5E1)),
                contentAlignment = Alignment.Center
            ) {
                if (selectedMediaUri != null) {
                    AsyncImage(
                        model = android.net.Uri.parse(selectedMediaUri),
                        contentDescription = "Ad image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "🌿🪴🌱",
                        style = typography.largeTitle1
                    )
                }
            }

            // CTA button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.wdsSpacingSinglePlus,
                        vertical = dimensions.wdsSpacingSingle
                    )
            ) {
                WDSButton(
                    onClick = { },
                    text = "WhatsApp",
                    variant = WDSButtonVariant.OUTLINE,
                    size = WDSButtonSize.SMALL,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Description
            Text(
                text = "Delhi's organic nursery offers the best selection of plants and tools for your weekend plant projects.",
                style = typography.body2,
                color = colors.colorContentDefault,
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingSinglePlus,
                    vertical = dimensions.wdsSpacingHalf
                )
            )

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf),
        modifier = Modifier.clickableWithSound { }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(18.dp),
            tint = colors.colorContentDeemphasized
        )
        Text(
            text = label,
            style = typography.body2,
            color = colors.colorContentDeemphasized
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAdScreenPreview() {
    WdsTheme(darkTheme = false) {
        PreviewAdScreen(onNavigateBack = {})
    }
}
