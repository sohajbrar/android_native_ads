@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

import androidx.compose.foundation.background
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.components.wdsCardBorder
import com.example.chatapp.wds.theme.WdsTheme
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView

@Composable
fun AdDetailsScreen(
    ad: AdItem,
    onNavigateBack: () -> Unit,
    onSeePerformanceDetails: () -> Unit = {},
    onRecreateAd: () -> Unit = {},
    onAdPreviewClick: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Ad details",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK) }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "More options",
                            tint = colors.colorContentDefault
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.colorSurfaceDefault)
                    .navigationBarsPadding()
            ) {
                WDSDivider()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSinglePlus
                        )
                ) {
                    WDSButton(
                        onClick = onRecreateAd,
                        text = "Recreate ad",
                        variant = WDSButtonVariant.FILLED,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Ad preview section ──
            Text(
                text = "Ad preview",
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                modifier = Modifier.padding(
                    start = dimensions.wdsSpacingDouble,
                    top = dimensions.wdsSpacingDouble,
                    bottom = dimensions.wdsSpacingSingle
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableWithSound(onClick = onAdPreviewClick)
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSingle
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (ad.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ad.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(WdsTheme.shapes.single),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(WdsTheme.shapes.single)
                            .background(colors.colorSurfaceEmphasized),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = colors.colorContentDeemphasized
                        )
                    }
                }

                Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ad.createdDate,
                        style = typography.body1Emphasized,
                        color = colors.colorContentDefault
                    )
                    Text(
                        text = ad.platform,
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = colors.colorContentDeemphasized
                )
            }

            WDSDivider(modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble))

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

            // ── Ad performance section ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.wdsSpacingDouble),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
                ) {
                    Text(
                        text = "Ad performance",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info",
                        modifier = Modifier.size(16.dp),
                        tint = colors.colorContentDeemphasized
                    )
                }
                WDSButton(
                    onClick = onSeePerformanceDetails,
                    text = "See details",
                    variant = WDSButtonVariant.OUTLINE
                )
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingSinglePlus))

            // 2×2 metric grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.wdsSpacingDouble),
                verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
                ) {
                    DetailMetricCard(
                        icon = Icons.Outlined.Group,
                        value = formatReach(ad.reach),
                        label = "Reach",
                        modifier = Modifier.weight(1f)
                    )
                    DetailMetricCard(
                        icon = Icons.Outlined.Payments,
                        value = ad.amountSpent,
                        label = "Amount spent",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
                ) {
                    DetailMetricCard(
                        icon = Icons.Outlined.ChatBubbleOutline,
                        value = ad.conversationsStarted.toString(),
                        label = "Conversations started",
                        modifier = Modifier.weight(1f)
                    )
                    DetailMetricCard(
                        icon = Icons.Outlined.Payments,
                        value = ad.costPerConversation,
                        label = "Cost per conversation",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))
            WDSDivider(modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble))

            // ── Ad settings section ──
            Text(
                text = "Ad settings",
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                modifier = Modifier.padding(
                    start = dimensions.wdsSpacingDouble,
                    top = dimensions.wdsSpacingDouble,
                    bottom = dimensions.wdsSpacingSingle
                )
            )

            // Status row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSingle
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val statusIcon: ImageVector
                val statusColor = when (ad.status) {
                    AdStatus.ACTIVE -> {
                        statusIcon = Icons.Default.CheckCircle
                        colors.colorPositive
                    }
                    AdStatus.PAUSED -> {
                        statusIcon = Icons.Default.PauseCircle
                        colors.colorContentDeemphasized
                    }
                    AdStatus.IN_REVIEW -> {
                        statusIcon = Icons.Outlined.Schedule
                        colors.colorContentDeemphasized
                    }
                    AdStatus.COMPLETED -> {
                        statusIcon = Icons.Default.CheckCircle
                        colors.colorContentDeemphasized
                    }
                }
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = statusColor
                )
                Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))
                Column {
                    Text(
                        text = when (ad.status) {
                            AdStatus.ACTIVE -> "Active"
                            AdStatus.PAUSED -> "Paused"
                            AdStatus.COMPLETED -> "Completed"
                            AdStatus.IN_REVIEW -> "In review"
                        },
                        style = typography.body1,
                        color = colors.colorContentDefault
                    )
                    Text(
                        text = "Status",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                }
            }

            WDSDivider(
                startIndent = dimensions.wdsSpacingDouble,
                endIndent = dimensions.wdsSpacingDouble
            )

            // Audience row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSingle
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.People,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colors.colorContentDeemphasized
                )
                Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ad.audienceDescription.ifEmpty { "Default audience" },
                        style = typography.body1,
                        color = colors.colorContentDefault
                    )
                    Text(
                        text = "Audience",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                }
                IconButton(
                    onClick = { view.playSoundEffect(SoundEffectConstants.CLICK) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(20.dp),
                        tint = colors.colorContentDeemphasized
                    )
                }
            }

            WDSDivider(
                startIndent = dimensions.wdsSpacingDouble,
                endIndent = dimensions.wdsSpacingDouble
            )

            // Duration row
            if (ad.adDuration.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSingle
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = colors.colorContentDeemphasized
                    )
                    Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))
                    Column {
                        Text(
                            text = ad.adDuration,
                            style = typography.body1,
                            color = colors.colorContentDefault
                        )
                        Text(
                            text = "Duration",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuad))
        }
    }
}

@Composable
private fun DetailMetricCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Card(
        modifier = modifier.height(88.dp),
        shape = WdsTheme.shapes.double,
        colors = CardDefaults.cardColors(containerColor = colors.colorSurfaceDefault),
        border = wdsCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensions.wdsSpacingSinglePlus)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = colors.colorContentDeemphasized
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                style = typography.body1Emphasized,
                color = colors.colorContentDefault
            )
            Text(
                text = label,
                style = typography.body3,
                color = colors.colorContentDeemphasized
            )
        }
    }
}

private fun formatReach(reach: Int): String = when {
    reach >= 1000 -> {
        val k = reach / 1000.0
        if (k == k.toLong().toDouble()) "${k.toLong()}K" else "${"%.1f".format(k)}K"
    }
    else -> reach.toString()
}
