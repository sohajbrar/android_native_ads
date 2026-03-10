@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

import androidx.compose.foundation.background
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.wdsCardBorder
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView

enum class AdStatus { ACTIVE, COMPLETED, PAUSED, IN_REVIEW }

data class AdItem(
    val createdDate: String,
    val status: AdStatus,
    val statusDetail: String? = null,
    val reach: Int,
    val amountSpent: String,
    val conversationsStarted: Int,
    val costPerConversation: String,
    val performanceNote: String? = null,
    val actionButton: String? = null,
    val imageUrl: String = "",
    val linkClicks: Int = 0,
    val postEngagements: Int = 0,
    val costPerPostEngagement: String = "$0.00",
    val adDuration: String = "",
    val audienceDescription: String = "",
    val platform: String = "Facebook & Instagram"
)

@Composable
fun ManageAdsScreen(
    onNavigateBack: () -> Unit,
    onCreateAdClick: () -> Unit,
    onAdClick: (AdItem) -> Unit = {},
    createdAds: List<CreatedAd> = emptyList(),
    showSnackbar: Boolean = false,
    onSnackbarShown: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(
                message = "Your ad is in review",
                duration = SnackbarDuration.Short
            )
            onSnackbarShown()
        }
    }

    val createdAdItems = createdAds.map { ad ->
        AdItem(
            createdDate = ad.adName,
            status = AdStatus.IN_REVIEW,
            statusDetail = "In review",
            reach = 0,
            amountSpent = "$0.00",
            conversationsStarted = 0,
            costPerConversation = "$0.00",
            imageUrl = ad.mediaUri
                ?: "https://images.unsplash.com/photo-1459411552884-841db9b3cc2a?w=200&h=200&fit=crop"
        )
    }

    val hardcodedAds = remember {
        listOf(
            AdItem(
                createdDate = "Monstera Deliciosa Sale",
                status = AdStatus.ACTIVE,
                statusDetail = "Active · Ends in 10 days",
                reach = 2840,
                amountSpent = "$156.30",
                conversationsStarted = 47,
                costPerConversation = "$3.33",
                performanceNote = "Take advantage of this ad's performance.",
                actionButton = "Extend duration",
                imageUrl = "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=200&h=200&fit=crop",
                linkClicks = 59,
                postEngagements = 85,
                costPerPostEngagement = "$0.34",
                adDuration = "Feb 24 – Mar 16",
                audienceDescription = "India · Indoor Plants, Home Décor +2\n18–65+ · All"
            ),
            AdItem(
                createdDate = "Snake Plant Collection",
                status = AdStatus.COMPLETED,
                statusDetail = "Completed · Ended Feb 21",
                reach = 1560,
                amountSpent = "$89.45",
                conversationsStarted = 28,
                costPerConversation = "$3.19",
                imageUrl = "https://images.unsplash.com/photo-1593482892540-90fe7b3c4bfa?w=200&h=200&fit=crop",
                linkClicks = 34,
                postEngagements = 52,
                costPerPostEngagement = "$0.41",
                adDuration = "Jan 22 – Feb 21",
                audienceDescription = "India · Indoor Plants, Succulents\n18–65+ · All"
            ),
            AdItem(
                createdDate = "Spring Succulent Bundle",
                status = AdStatus.PAUSED,
                statusDetail = "Paused",
                reach = 920,
                amountSpent = "$52.18",
                conversationsStarted = 15,
                costPerConversation = "$3.48",
                actionButton = "Resume ad",
                imageUrl = "https://images.unsplash.com/photo-1459411552884-841db9b3cc2a?w=200&h=200&fit=crop",
                linkClicks = 18,
                postEngagements = 31,
                costPerPostEngagement = "$0.52",
                adDuration = "Feb 1 – Mar 3",
                audienceDescription = "India · Succulents, Gifts\n18–45 · Women"
            ),
            AdItem(
                createdDate = "Pothos & Trailing Plants",
                status = AdStatus.COMPLETED,
                statusDetail = "Completed · Ended Jan 15",
                reach = 3200,
                amountSpent = "$154.20",
                conversationsStarted = 62,
                costPerConversation = "$2.49",
                imageUrl = "https://images.unsplash.com/photo-1572688484438-313a56e6a33f?w=200&h=200&fit=crop",
                linkClicks = 78,
                postEngagements = 110,
                costPerPostEngagement = "$0.28",
                adDuration = "Dec 16 – Jan 15",
                audienceDescription = "India · Indoor Plants, Home Décor\n18–65+ · All"
            ),
            AdItem(
                createdDate = "Peace Lily Clearance",
                status = AdStatus.ACTIVE,
                statusDetail = "Active · Ends in 4 days",
                reach = 1980,
                amountSpent = "$110.50",
                conversationsStarted = 36,
                costPerConversation = "$3.07",
                performanceNote = "This ad is performing well!",
                actionButton = "Extend duration",
                imageUrl = "https://images.unsplash.com/photo-1616690710400-a16d146927c5?w=200&h=200&fit=crop",
                linkClicks = 44,
                postEngagements = 67,
                costPerPostEngagement = "$0.38",
                adDuration = "Feb 15 – Mar 8",
                audienceDescription = "India · Indoor Plants, Air Purifying\n25–55 · All"
            ),
            AdItem(
                createdDate = "Fiddle Leaf Fig Promo",
                status = AdStatus.COMPLETED,
                statusDetail = "Completed · Ended Dec 30",
                reach = 4150,
                amountSpent = "$210.75",
                conversationsStarted = 81,
                costPerConversation = "$2.60",
                imageUrl = "https://images.unsplash.com/photo-1545241047-6083a3684587?w=200&h=200&fit=crop",
                linkClicks = 102,
                postEngagements = 143,
                costPerPostEngagement = "$0.25",
                adDuration = "Dec 1 – Dec 30",
                audienceDescription = "India · Indoor Plants, Premium Plants\n22–50 · All"
            ),
            AdItem(
                createdDate = "ZZ Plant Special Offer",
                status = AdStatus.ACTIVE,
                statusDetail = "Active · Ends in 18 days",
                reach = 1120,
                amountSpent = "$64.80",
                conversationsStarted = 19,
                costPerConversation = "$3.41",
                imageUrl = "https://images.unsplash.com/photo-1632207691143-643e2a9a9361?w=200&h=200&fit=crop",
                linkClicks = 27,
                postEngagements = 38,
                costPerPostEngagement = "$0.45",
                adDuration = "Mar 1 – Mar 22",
                audienceDescription = "India · Low Maintenance Plants\n18–65+ · All"
            ),
            AdItem(
                createdDate = "Hanging Planters Sale",
                status = AdStatus.PAUSED,
                statusDetail = "Paused",
                reach = 1740,
                amountSpent = "$95.60",
                conversationsStarted = 33,
                costPerConversation = "$2.90",
                actionButton = "Resume ad",
                imageUrl = "https://images.unsplash.com/photo-1521334884684-d80222895322?w=200&h=200&fit=crop",
                linkClicks = 41,
                postEngagements = 59,
                costPerPostEngagement = "$0.36",
                adDuration = "Jan 10 – Feb 10",
                audienceDescription = "India · Planters, Home Décor\n25–45 · Women"
            ),
            AdItem(
                createdDate = "Rubber Plant Weekend Deal",
                status = AdStatus.COMPLETED,
                statusDetail = "Completed · Ended Nov 25",
                reach = 2650,
                amountSpent = "$132.40",
                conversationsStarted = 54,
                costPerConversation = "$2.45",
                imageUrl = "https://images.unsplash.com/photo-1637967886160-fd761519fb90?w=200&h=200&fit=crop",
                linkClicks = 65,
                postEngagements = 92,
                costPerPostEngagement = "$0.30",
                adDuration = "Nov 5 – Nov 25",
                audienceDescription = "India · Indoor Plants, Gardening\n18–65+ · All"
            ),
            AdItem(
                createdDate = "Ceramic Pot Collection",
                status = AdStatus.COMPLETED,
                statusDetail = "Completed · Ended Oct 31",
                reach = 3480,
                amountSpent = "$178.90",
                conversationsStarted = 71,
                costPerConversation = "$2.52",
                imageUrl = "https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=200&h=200&fit=crop",
                linkClicks = 88,
                postEngagements = 125,
                costPerPostEngagement = "$0.27",
                adDuration = "Oct 1 – Oct 31",
                audienceDescription = "India · Planters, Home Décor, Gifts\n18–55 · All"
            )
        )
    }

    val ads = createdAdItems + hardcodedAds

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            WDSTopBar(
                title = "Manage ads",
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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onCreateAdClick() },
                containerColor = colors.colorAccent,
                contentColor = colors.colorContentOnAccent,
                shape = WdsTheme.shapes.circle,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                text = {
                    Text(
                        text = "Create ad",
                        style = typography.body1Emphasized
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Performance overview section
            item {
                PerformanceOverviewSection()
            }

            // For you section
            item {
                SectionHeader(title = "For you")
            }

            item {
                ForYouCardsRow()
            }

            // Your ads section
            item {
                SectionHeader(title = "Your ads")
            }

            items(ads) { ad ->
                AdItemRow(ad = ad, onClick = { onAdClick(ad) })
                WDSDivider(
                    modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun PerformanceOverviewSection() {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.wdsSpacingDouble)
    ) {
        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

        // Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
            ) {
                Text(
                    text = "Performance overview",
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault
                )
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    modifier = Modifier.size(18.dp),
                    tint = colors.colorContentDeemphasized
                )
            }
            WDSButton(
                onClick = { },
                text = "See details",
                variant = WDSButtonVariant.OUTLINE
            )
        }

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

        Text(
            text = "You've spent \$452.13 on 4 ads in the last 30 days (Feb 6 – Mar 7).",
            style = typography.body2,
            color = colors.colorContentDeemphasized
        )

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSinglePlus))

        // Metric cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
        ) {
            MetricCard(
                icon = Icons.Outlined.Group,
                value = "5.6K",
                label = "Reach",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                icon = Icons.Outlined.Payments,
                value = "\$452.13",
                label = "Amount spent",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))
    }
}

@Composable
private fun MetricCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
        colors = CardDefaults.cardColors(
            containerColor = colors.colorSurfaceDefault
        ),
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

@Composable
private fun SectionHeader(title: String) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Text(
        text = title,
        style = typography.body1Emphasized,
        color = colors.colorContentDefault,
        modifier = Modifier.padding(
            horizontal = dimensions.wdsSpacingDouble,
            vertical = dimensions.wdsSpacingSingle
        )
    )
}

@Composable
private fun ForYouCardsRow() {
    val dimensions = WdsTheme.dimensions

    data class AdRecommendation(
        val title: String,
        val subtitle: String,
        val description: String,
        val ctaText: String,
        val imageUrl: String
    )

    val recommendations = remember {
        listOf(
            AdRecommendation(
                title = "Fiddle Leaf Fig Promo",
                subtitle = "Ended Feb 22 · 106 conversations started",
                description = "This ad was in the top 25% of ads from similar advertisers.",
                ctaText = "Recreate ad",
                imageUrl = "https://images.unsplash.com/photo-1459411552884-841db9b3cc2a?w=200&h=200&fit=crop"
            ),
            AdRecommendation(
                title = "Peace Lily Weekend Deal",
                subtitle = "Ended Jan 15 · 89 conversations started",
                description = "This ad had a lower cost per conversation than 60% of similar ads.",
                ctaText = "Recreate ad",
                imageUrl = "https://images.unsplash.com/photo-1598880940080-ff9a29891b85?w=200&h=200&fit=crop"
            ),
            AdRecommendation(
                title = "Rare Houseplants Drop",
                subtitle = "Ended Dec 1 · 72 conversations started",
                description = "This ad reached 2x more people than your average ad.",
                ctaText = "Recreate ad",
                imageUrl = "https://images.unsplash.com/photo-1509423350716-97f9360b4e09?w=200&h=200&fit=crop"
            )
        )
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle),
        contentPadding = PaddingValues(
            horizontal = dimensions.wdsSpacingSinglePlus,
            vertical = dimensions.wdsSpacingSinglePlus
        )
    ) {
        items(recommendations.size) { index ->
            val rec = recommendations[index]
            AdRecommendationCard(
                title = rec.title,
                subtitle = rec.subtitle,
                description = rec.description,
                ctaText = rec.ctaText,
                imageUrl = rec.imageUrl
            )
        }
    }
}

@Composable
private fun AdRecommendationCard(
    title: String,
    subtitle: String,
    description: String,
    ctaText: String,
    imageUrl: String
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Card(
        modifier = Modifier.width(314.dp),
        shape = WdsTheme.shapes.double,
        colors = CardDefaults.cardColors(
            containerColor = colors.colorSurfaceDefault
        ),
        border = wdsCardBorder()
    ) {
        Box {
            Row(
                modifier = Modifier.padding(dimensions.wdsSpacingSinglePlus),
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSinglePlus)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
                ) {
                    Text(
                        text = title,
                        style = typography.body1,
                        color = colors.colorContentDefault,
                        modifier = Modifier.padding(end = 28.dp)
                    )
                    Text(
                        text = subtitle,
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                    Text(
                        text = description,
                        style = typography.body2,
                        color = colors.colorContentDefault
                    )
                    Spacer(modifier = Modifier.height(dimensions.wdsSpacingHalf))
                    Box(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 64.dp, minHeight = 32.dp)
                            .clip(WdsTheme.shapes.circle)
                            .background(colors.colorAccent)
                            .clickableWithSound { }
                            .padding(
                                horizontal = dimensions.wdsSpacingDouble,
                                vertical = 6.dp
                            ),
                        contentAlignment = Alignment.Center
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
                onClick = { view.playSoundEffect(SoundEffectConstants.CLICK) },
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
private fun AdItemRow(ad: AdItem, onClick: () -> Unit = {}) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithSound(onClick = onClick)
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            )
    ) {
        // Header: badge + thumbnail + date + overflow menu
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status badge + thumbnail
            Box {
                if (ad.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ad.imageUrl,
                        contentDescription = ad.createdDate,
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
                            imageVector = Icons.Outlined.Campaign,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = colors.colorContentDeemphasized
                        )
                    }
                }
                // Status badge overlay
                when (ad.status) {
                    AdStatus.ACTIVE -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Active",
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.BottomStart)
                                .background(colors.colorSurfaceDefault, CircleShape),
                            tint = colors.colorPositive
                        )
                    }
                    AdStatus.PAUSED -> {
                        Icon(
                            imageVector = Icons.Default.PauseCircle,
                            contentDescription = "Paused",
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.BottomStart)
                                .background(colors.colorSurfaceDefault, CircleShape),
                            tint = colors.colorContentDeemphasized
                        )
                    }
                    AdStatus.IN_REVIEW -> {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = "In review",
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.BottomStart)
                                .background(colors.colorSurfaceDefault, CircleShape),
                            tint = colors.colorContentDeemphasized
                        )
                    }
                    AdStatus.COMPLETED -> { }
                }
            }

            Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ad.createdDate,
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault
                )
                ad.statusDetail?.let { detail ->
                    Text(
                        text = detail,
                        style = typography.body2,
                        color = when (ad.status) {
                            AdStatus.ACTIVE -> colors.colorPositive
                            else -> colors.colorContentDeemphasized
                        }
                    )
                }
            }

            IconButton(
                onClick = { view.playSoundEffect(SoundEffectConstants.CLICK) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Options",
                    modifier = Modifier.size(20.dp),
                    tint = colors.colorContentDeemphasized
                )
            }
        }

        if (ad.status != AdStatus.IN_REVIEW) {
            Spacer(modifier = Modifier.height(dimensions.wdsSpacingSinglePlus))

            val statsStartPadding = 48.dp + dimensions.wdsSpacingSinglePlus

            // Stats grid
            Column(
                modifier = Modifier.padding(start = statsStartPadding),
                verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
            ) {
                StatRow(label = "Reach", value = ad.reach.toString())
                StatRow(label = "Amount spent", value = ad.amountSpent)
                StatRow(label = "Conversations started", value = ad.conversationsStarted.toString())
                StatRow(
                    label = "Cost per conversation",
                    value = ad.costPerConversation,
                    valueIcon = if (ad.status == AdStatus.ACTIVE) Icons.Outlined.Campaign else null
                )
            }

            // Performance note
            ad.performanceNote?.let { note ->
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingSinglePlus))
                Text(
                    text = "📈 $note",
                    style = typography.body2,
                    color = colors.colorContentDefault,
                    modifier = Modifier.padding(start = statsStartPadding)
                )
            }

            // Action button
            ad.actionButton?.let { buttonText ->
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingSinglePlus))
                WDSButton(
                    onClick = { },
                    text = buttonText,
                    variant = WDSButtonVariant.OUTLINE,
                    modifier = Modifier.padding(start = statsStartPadding)
                )
            }
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
    valueIcon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = typography.body2,
            color = colors.colorContentDeemphasized
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (valueIcon != null) {
                Icon(
                    imageVector = valueIcon,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = colors.colorPositive
                )
            }
            Text(
                text = value,
                style = typography.body2,
                color = colors.colorContentDefault,
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ManageAdsScreenPreview() {
    WdsTheme(darkTheme = false) {
        ManageAdsScreen(
            onNavigateBack = {},
            onCreateAdClick = {},
            createdAds = emptyList()
        )
    }
}
