@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.broadcast

import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.example.chatapp.wds.components.clickableWithSound
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Reply
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.components.WdsComingSoonDialog
import com.example.chatapp.wds.theme.WdsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageDetailsScreen(
    messageContent: String,
    sentTimestamp: Long,
    recipientCount: Int,
    onBackClick: () -> Unit = {},
    onMessageRowClick: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    var showComingSoonDialog by remember { mutableStateOf(false) }

    if (showComingSoonDialog) {
        WdsComingSoonDialog(onDismissRequest = { showComingSoonDialog = false })
    }

    val delivered = recipientCount
    val readCount = remember(delivered) { (delivered * 0.65).toInt() }
    val replyCount = remember(delivered) { (delivered * 0.16).toInt() }
    val readRate = remember(delivered, readCount) {
        if (delivered > 0) (readCount * 100) / delivered else 0
    }
    val replyRate = remember(delivered, replyCount) {
        if (delivered > 0) (replyCount * 100) / delivered else 0
    }

    val sentDateShort = remember(sentTimestamp) {
        val formatter = SimpleDateFormat("MMM d", Locale.getDefault())
        "Sent ${formatter.format(Date(sentTimestamp))}"
    }
    val sentDateFull = remember(sentTimestamp) {
        val formatter = SimpleDateFormat("MMM d, h:mma", Locale.getDefault())
        "Sent ${formatter.format(Date(sentTimestamp))}"
    }

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Message details",
                onNavigateBack = onBackClick,
                actions = {
                    IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); showComingSoonDialog = true }) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            MessagePreviewRow(
                messageContent = messageContent,
                sentDateShort = sentDateShort,
                readRate = readRate,
                onClick = onMessageRowClick
            )

            MessagePerformanceSection(
                sentDateFull = sentDateFull,
                delivered = delivered,
                readCount = readCount,
                replyCount = replyCount
            )

            MetricRow(
                label = "Delivered",
                value = "$delivered",
                showChevron = true,
                onClick = { showComingSoonDialog = true }
            )

            MetricRow(
                label = "Read rate",
                value = "$readRate%",
                secondaryLabel = "Read",
                secondaryValue = "$readCount",
                onClick = { showComingSoonDialog = true }
            )

            MetricRow(
                label = "Reply rate",
                value = "$replyRate%",
                secondaryLabel = "Replies",
                secondaryValue = "$replyCount",
                onClick = { showComingSoonDialog = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            WDSButton(
                onClick = { showComingSoonDialog = true },
                text = "Duplicate broadcast",
                variant = WDSButtonVariant.OUTLINE,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingDouble
                    )
            )
        }
    }
}

@Composable
private fun MessagePreviewRow(
    messageContent: String,
    sentDateShort: String,
    readRate: Int,
    onClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickableWithSound(onClick = onClick)
            .padding(horizontal = dimensions.wdsSpacingDouble),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(WdsTheme.shapes.single)
                .background(colors.colorBroadcastAvatar),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_business_broadcast),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = colors.colorContentOnAccent
            )
        }

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = messageContent,
                style = typography.body1,
                color = colors.colorContentDefault,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$sentDateShort \u00B7 $readRate% read rate",
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                modifier = Modifier.padding(top = dimensions.wdsSpacingQuarter)
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = colors.colorContentDeemphasized,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun MessagePerformanceSection(
    sentDateFull: String,
    delivered: Int,
    readCount: Int,
    replyCount: Int
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensions.wdsSpacingDouble,
                end = dimensions.wdsSpacingDouble,
                bottom = dimensions.wdsSpacingSingle
            )
    ) {
        Row(
            modifier = Modifier.height(68.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
                ) {
                    Text(
                        text = "Message performance",
                        style = typography.body1Emphasized,
                        color = colors.colorContentDefault
                    )
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Message performance info",
                        modifier = Modifier.size(18.dp),
                        tint = colors.colorContentDeemphasized
                    )
                }

                Spacer(modifier = Modifier.height(dimensions.wdsSpacingHalf))

                Text(
                    text = sentDateFull,
                    style = typography.body2,
                    color = colors.colorContentDeemphasized
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
        ) {
            MetricCard(
                icon = Icons.Outlined.Groups,
                value = "$delivered",
                label = "Delivered",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                icon = Icons.Outlined.DoneAll,
                value = "$readCount",
                label = "Read",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                icon = Icons.AutoMirrored.Outlined.Reply,
                value = "$replyCount",
                label = "Replies",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MetricCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = colors.colorDivider,
                shape = WdsTheme.shapes.single
            )
            .padding(dimensions.wdsSpacingSinglePlus),
        verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = colors.colorContentDeemphasized
        )
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

@Composable
private fun MetricRow(
    label: String,
    value: String,
    secondaryLabel: String? = null,
    secondaryValue: String? = null,
    showChevron: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickableWithSound(onClick = onClick) else Modifier
            )
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus // 12dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = typography.body1,
                color = colors.colorContentDefault
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
            ) {
                Text(
                    text = value,
                    style = typography.body1,
                    color = colors.colorContentDefault
                )
                if (showChevron) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = colors.colorContentDeemphasized
                    )
                }
            }
        }

        if (secondaryLabel != null && secondaryValue != null) {
            Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuarter))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = secondaryLabel,
                    style = typography.body2,
                    color = colors.colorContentDeemphasized
                )
                Text(
                    text = secondaryValue,
                    style = typography.body2,
                    color = colors.colorContentDeemphasized
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
private fun MessageDetailsScreenPreviewLight() {
    WdsTheme(darkTheme = false) {
        MessageDetailsScreen(
            messageContent = "Welcome, VIP clients! \uD83C\uDF8B Thank you for your loyalty!",
            sentTimestamp = System.currentTimeMillis() - 86400000L * 7,
            recipientCount = 256
        )
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
private fun MessageDetailsScreenPreviewDark() {
    WdsTheme(darkTheme = true) {
        MessageDetailsScreen(
            messageContent = "Welcome, VIP clients! \uD83C\uDF8B Thank you for your loyalty!",
            sentTimestamp = System.currentTimeMillis() - 86400000L * 7,
            recipientCount = 256
        )
    }
}
