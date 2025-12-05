@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.tools

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.WavingHand
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.example.chatapp.features.chatlist.ChatListBottomBar
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun ToolsScreen(
    selectedTab: Int = 3,
    unreadChats: Int = 0,
    onChatsClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onUpdatesClick: () -> Unit = {},
    onToolsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tools",
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
        },
        bottomBar = {
            ChatListBottomBar(
                selectedTab = selectedTab,
                unreadChats = unreadChats,
                hasUpdates = false,
                callsBadgeCount = 0,
                onChatsClick = onChatsClick,
                onCallsClick = onCallsClick,
                onUpdatesClick = onUpdatesClick,
                onToolsClick = onToolsClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Performance Section Header (subheader style)
            item {
                SectionDividerWithHeader(
                    headerText = "Last 7 days performance",
                    isSubheader = true,
                    showInfoIcon = true,
                    showDivider = false
                )
            }

            // Metrics Row
            item {
                MetricsRow()
            }

            // For You Section (main header with divider)
            item {
                SectionDividerWithHeader(
                    headerText = "For you",
                    isSubheader = false,
                    showDivider = true
                )
            }

            // Recommendation Cards
            item {
                RecommendationCardsRow()
            }

            // Grow Your Business Section
            item {
                SectionDividerWithHeader(
                    headerText = "Grow your business",
                    isSubheader = false,
                    showDivider = false
                )
            }

            // Business Growth Settings Rows
            item {
                SettingsRow(
                    icon = Icons.Outlined.Verified,
                    title = "Meta Verified",
                    subtitle = "Show that your profile is verified"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.AutoAwesome,
                    title = "Business AI",
                    subtitle = "Send friendly responses 24/7"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.GridOn,
                    title = "Catalog",
                    subtitle = "Show products and services"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Campaign,
                    title = "Advertise",
                    subtitle = "Create ads that lead to WhatsApp"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Group,
                    title = "Manage ads",
                    subtitle = "See all your ads in one place"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Mail,
                    title = "Marketing messages",
                    subtitle = "Re-engage with offers & announcements"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Store,
                    title = "Business directory",
                    subtitle = "Help people find you on WhatsApp"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Receipt,
                    title = "Orders",
                    subtitle = "Manage orders and payments"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Payments,
                    title = "Payments",
                    subtitle = "View history and manage info"
                )
            }

            // Organize Your Chats Section
            item {
                SectionDividerWithHeader(
                    headerText = "Organize your chats",
                    isSubheader = false,
                    showDivider = false
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Outlined.Label,
                    title = "Labels",
                    subtitle = "Organise chats and customers"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.SentimentSatisfied,
                    title = "Greeting message",
                    subtitle = "Welcome new customers automatically"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Timer,
                    title = "Away message",
                    subtitle = "Reply automatically when you're away"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Bolt,
                    title = "Quick replies",
                    subtitle = "Reuse frequent messages"
                )
            }

            // Manage Your Account Section
            item {
                SectionDividerWithHeader(
                    headerText = "Manage your account",
                    isSubheader = false,
                    showDivider = false
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Outlined.AccountCircle,
                    title = "Profile",
                    subtitle = "Manage address, hours, and websites"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Link,
                    title = "Facebook & Instagram",
                    subtitle = "Add WhatsApp to your profiles"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Sync,
                    title = "Ads data sharing",
                    subtitle = "Share customer-related activities to help improve your ads"
                )
            }
            item {
                SettingsRow(
                    icon = Icons.Outlined.Settings,
                    title = "Business Platform",
                    subtitle = "Connect to API solutions"
                )
            }

            // Divider before Help Centre
            item {
                SettingsDivider()
            }

            // Business Help Centre
            item {
                SettingsRow(
                    icon = Icons.Outlined.HelpOutline,
                    title = "Business help centre",
                    subtitle = "Get help, contact us"
                )
            }

            // Final Divider
            item {
                SettingsDivider()
            }
        }
    }
}

@Composable
private fun SectionDividerWithHeader(
    headerText: String,
    isSubheader: Boolean,
    showDivider: Boolean = false,
    showInfoIcon: Boolean = false
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column {
        // Space above
        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))
        
        // Optional divider line (0.5dp)
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.wdsBorderWidthThin)
                    .background(colors.colorDivider)
            )
            Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))
        }
        
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensions.wdsSpacingDouble,
                    end = dimensions.wdsSpacingDouble,
                    top = 0.dp,
                    bottom = dimensions.wdsSpacingSingle
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
        ) {
            Text(
                text = headerText,
                style = if (isSubheader) typography.body2Emphasized else typography.body1Emphasized,
                color = if (isSubheader) colors.colorContentDeemphasized else colors.colorContentDefault
            )
            // Info icon next to title (like Figma)
            if (showInfoIcon) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    modifier = Modifier.size(20.dp),
                    tint = colors.colorContentDeemphasized
                )
            }
        }
    }
}

@Composable
private fun MetricsRow() {
    val dimensions = WdsTheme.dimensions
    val colors = WdsTheme.colors
    val statusIcon = rememberStatusIcon(colors.colorContentDefault)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
    ) {
        MetricTile(
            icon = Icons.Outlined.WavingHand,
            value = "121",
            label = "Conversations",
            modifier = Modifier.weight(1f)
        )
        MetricTile(
            icon = Icons.Outlined.GridOn,
            value = "3.4K",
            label = "Catalog views",
            modifier = Modifier.weight(1f)
        )
        MetricTileWithCustomIcon(
            customIcon = statusIcon,
            value = "1.1K",
            label = "Status views",
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Custom Status icon from Figma SVG
 */
@Composable
private fun rememberStatusIcon(tint: Color): ImageVector {
    return remember(tint) {
        ImageVector.Builder(
            name = "StatusIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(tint)) {
                moveTo(13.5628f, 3.13669f)
                curveTo(13.6587f, 2.59279f, 14.1794f, 2.22472f, 14.711f, 2.37444f)
                curveTo(15.7905f, 2.67848f, 16.8135f, 3.16262f, 17.736f, 3.80864f)
                curveTo(18.9323f, 4.6463f, 19.9305f, 5.73581f, 20.6606f, 7.00056f)
                curveTo(21.3907f, 8.26532f, 21.8349f, 9.67463f, 21.962f, 11.1294f)
                curveTo(22.0601f, 12.2514f, 21.9677f, 13.3794f, 21.6911f, 14.4663f)
                curveTo(21.5549f, 15.0015f, 20.9758f, 15.2683f, 20.4568f, 15.0793f)
                curveTo(19.9378f, 14.8904f, 19.677f, 14.3171f, 19.7998f, 13.7786f)
                curveTo(19.9843f, 12.9694f, 20.0422f, 12.1344f, 19.9696f, 11.3035f)
                curveTo(19.8679f, 10.1397f, 19.5126f, 9.01224f, 18.9285f, 8.00044f)
                curveTo(18.3444f, 6.98864f, 17.5458f, 6.11703f, 16.5888f, 5.4469f)
                curveTo(15.9057f, 4.9685f, 15.1536f, 4.60107f, 14.3606f, 4.35616f)
                curveTo(13.8329f, 4.1932f, 13.4669f, 3.68058f, 13.5628f, 3.13669f)
                close()
            }
            path(fill = SolidColor(tint)) {
                moveTo(18.8944f, 17.785f)
                curveTo(19.3175f, 18.14f, 19.3759f, 18.775f, 18.9804f, 19.1605f)
                curveTo(18.1774f, 19.9433f, 17.2466f, 20.5872f, 16.2259f, 21.0632f)
                curveTo(14.9023f, 21.6803f, 13.4597f, 22.0001f, 11.9993f, 22f)
                curveTo(10.5389f, 21.9999f, 9.09633f, 21.6799f, 7.77287f, 21.0626f)
                curveTo(6.7522f, 20.5865f, 5.82149f, 19.9425f, 5.01855f, 19.1595f)
                curveTo(4.62314f, 18.7739f, 4.68167f, 18.139f, 5.10479f, 17.7841f)
                curveTo(5.52792f, 17.4291f, 6.15484f, 17.4899f, 6.55976f, 17.8654f)
                curveTo(7.16828f, 18.4298f, 7.86245f, 18.8975f, 8.61829f, 19.2501f)
                curveTo(9.67707f, 19.7439f, 10.8312f, 19.9999f, 11.9994f, 20f)
                curveTo(13.1677f, 20f, 14.3218f, 19.7442f, 15.3807f, 19.2505f)
                curveTo(16.1366f, 18.8981f, 16.8308f, 18.4305f, 17.4394f, 17.8662f)
                curveTo(17.8444f, 17.4907f, 18.4713f, 17.43f, 18.8944f, 17.785f)
                close()
            }
            path(fill = SolidColor(tint)) {
                moveTo(3.54277f, 15.0781f)
                curveTo(3.02379f, 15.267f, 2.4447f, 15.0002f, 2.30857f, 14.4649f)
                curveTo(2.03215f, 13.3781f, 1.9399f, 12.2502f, 2.03806f, 11.1284f)
                curveTo(2.16533f, 9.67367f, 2.60965f, 8.2645f, 3.33978f, 6.9999f)
                curveTo(4.06991f, 5.73529f, 5.06815f, 4.64593f, 6.26432f, 3.80838f)
                curveTo(7.1868f, 3.16246f, 8.20975f, 2.6784f, 9.28915f, 2.3744f)
                curveTo(9.82075f, 2.22469f, 10.3414f, 2.59277f, 10.4373f, 3.13667f)
                curveTo(10.5332f, 3.68056f, 10.1672f, 4.19317f, 9.6395f, 4.35613f)
                curveTo(8.84657f, 4.601f, 8.09458f, 4.96837f, 7.41146f, 5.44669f)
                curveTo(6.45452f, 6.11673f, 5.65593f, 6.98822f, 5.07183f, 7.99991f)
                curveTo(4.48772f, 9.01159f, 4.13226f, 10.1389f, 4.03045f, 11.3027f)
                curveTo(3.95776f, 12.1334f, 4.01559f, 12.9684f, 4.19998f, 13.7775f)
                curveTo(4.3227f, 14.316f, 4.06175f, 14.8892f, 3.54277f, 15.0781f)
                close()
            }
            // Center ring with EvenOdd fill rule to create hollow center
            path(
                fill = SolidColor(tint),
                pathFillType = PathFillType.EvenOdd
            ) {
                // Inner circle (smaller)
                moveTo(12.0001f, 16.0001f)
                curveTo(14.2092f, 16.0001f, 16.0001f, 14.2092f, 16.0001f, 12.0001f)
                curveTo(16.0001f, 9.79094f, 14.2092f, 8.00008f, 12.0001f, 8.00008f)
                curveTo(9.79092f, 8.00008f, 8.00006f, 9.79094f, 8.00006f, 12.0001f)
                curveTo(8.00006f, 14.2092f, 9.79092f, 16.0001f, 12.0001f, 16.0001f)
                close()
                // Outer circle (larger) - EvenOdd creates the ring
                moveTo(12.0001f, 18.0001f)
                curveTo(15.3138f, 18.0001f, 18.0001f, 15.3138f, 18.0001f, 12.0001f)
                curveTo(18.0001f, 8.68637f, 15.3138f, 6.00008f, 12.0001f, 6.00008f)
                curveTo(8.68635f, 6.00008f, 6.00006f, 8.68637f, 6.00006f, 12.0001f)
                curveTo(6.00006f, 15.3138f, 8.68635f, 18.0001f, 12.0001f, 18.0001f)
                close()
            }
        }.build()
    }
}

@Composable
private fun MetricTileWithCustomIcon(
    customIcon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Card(
        modifier = modifier,
        shape = WdsTheme.shapes.double,
        colors = CardDefaults.cardColors(
            containerColor = colors.colorSurfaceDefault
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(dimensions.wdsSpacingSinglePlus)
        ) {
            Icon(
                imageVector = customIcon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified // Icon already has color baked in
            )
            Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = value,
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                    contentDescription = "Trending up",
                    modifier = Modifier.size(16.dp),
                    tint = colors.colorPositive
                )
            }
            Text(
                text = label,
                style = typography.body3,
                color = colors.colorContentDefault
            )
        }
    }
}

@Composable
private fun MetricTile(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Card(
        modifier = modifier,
        shape = WdsTheme.shapes.double,
        colors = CardDefaults.cardColors(
            containerColor = colors.colorSurfaceDefault
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(dimensions.wdsSpacingSinglePlus)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = colors.colorContentDefault
            )
            Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = value,
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                    contentDescription = "Trending up",
                    modifier = Modifier.size(16.dp),
                    tint = colors.colorPositive
                )
            }
            Text(
                text = label,
                style = typography.body3,
                color = colors.colorContentDefault
            )
        }
    }
}

@Composable
private fun RecommendationCardsRow() {
    val dimensions = WdsTheme.dimensions

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle),
        contentPadding = PaddingValues(
            horizontal = dimensions.wdsSpacingSinglePlus,
            vertical = dimensions.wdsSpacingSinglePlus
        )
    ) {
        items(3) { index ->
            RecommendationCard(
                title = when (index) {
                    0 -> "Verify your business"
                    1 -> "Create a catalog"
                    else -> "Set up greeting"
                },
                description = when (index) {
                    0 -> "Increase customer trust and do more with Meta Verified"
                    1 -> "Show your products and services to customers."
                    else -> "Welcome new customers automatically."
                },
                ctaText = "Label"
            )
        }
    }
}

@Composable
private fun RecommendationCard(
    title: String,
    description: String,
    ctaText: String
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Card(
        modifier = Modifier.width(314.dp),
        shape = WdsTheme.shapes.double,
        colors = CardDefaults.cardColors(
            containerColor = colors.colorSurfaceDefault
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Box {
            Row(
                modifier = Modifier.padding(dimensions.wdsSpacingSinglePlus),
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSinglePlus)
            ) {
                // Icon placeholder with emphasized surface background
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.colorSurfaceEmphasized),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = colors.colorContentDefault
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
                ) {
                    // Title with right padding for close button
                    Text(
                        text = title,
                        style = typography.body1,
                        color = colors.colorContentDefault,
                        modifier = Modifier.padding(end = 28.dp)
                    )
                    // Description
                    Text(
                        text = description,
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                    // CTA Button
                    Spacer(modifier = Modifier.height(dimensions.wdsSpacingHalf))
                    Box(
                        modifier = Modifier
                            .clip(WdsTheme.shapes.circle)
                            .background(colors.colorAccent)
                            .clickable { }
                            .padding(
                                horizontal = dimensions.wdsSpacingDouble,
                                vertical = 6.dp
                            )
                    ) {
                        Text(
                            text = ctaText,
                            style = typography.body2Emphasized,
                            color = colors.colorContentOnAccent
                        )
                    }
                }
            }
            // Close button (top-right)
            IconButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Dismiss",
                    modifier = Modifier.size(20.dp),
                    tint = colors.colorContentDeemphasized
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(
                horizontal = dimensions.wdsSpacingTriple,
                vertical = dimensions.wdsSpacingDouble
            ),
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingTriple),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = colors.colorContentDeemphasized  // Changed to deemphasized color
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
        ) {
            Text(
                text = title,
                style = typography.body1,
                color = colors.colorContentDefault
            )
            Text(
                text = subtitle,
                style = typography.body2,
                color = colors.colorContentDeemphasized
            )
        }
    }
}

@Composable
private fun SettingsDivider() {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensions.wdsSpacingSingle)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.wdsBorderWidthThin)
                .background(colors.colorDivider)
        )
    }
}
