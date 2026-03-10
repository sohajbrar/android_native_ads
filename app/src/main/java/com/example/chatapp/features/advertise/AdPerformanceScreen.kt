@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun AdPerformanceScreen(
    ad: AdItem,
    onNavigateBack: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Interactions", "Audience", "Placements")

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Ad performance",
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Ad header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSinglePlus
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (ad.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ad.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(WdsTheme.shapes.single),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))
                Column {
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
            }

            // Tab row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = colors.colorSurfaceDefault,
                contentColor = colors.colorContentDefault,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = colors.colorAccent
                        )
                    }
                },
                divider = { WDSDivider() }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = if (selectedTab == index) typography.body2Emphasized
                                else typography.body2
                            )
                        },
                        selectedContentColor = colors.colorContentDefault,
                        unselectedContentColor = colors.colorContentDeemphasized
                    )
                }
            }

            // Tab content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                when (selectedTab) {
                    0 -> InteractionsTab(ad)
                    1 -> AudienceTab(ad)
                    2 -> PlacementsTab(ad)
                }
            }
        }
    }
}

// ── Interactions ──

@Composable
private fun InteractionsTab(ad: AdItem) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val totalInteractions = ad.linkClicks + ad.conversationsStarted + ad.postEngagements

    Column(modifier = Modifier.padding(dimensions.wdsSpacingDouble)) {
        // Duration header
        DurationHeader(ad.adDuration)

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

        Text(
            text = "Interactions with your ad",
            style = typography.body1Emphasized,
            color = colors.colorContentDefault
        )

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

        // Big number
        Text(
            text = totalInteractions.toString(),
            style = typography.headline1,
            color = colors.colorContentDefault,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Total interactions",
            style = typography.body2,
            color = colors.colorContentDeemphasized,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))
        WDSDivider()

        PerformanceStatRow("Link clicks", ad.linkClicks.toString())
        PerformanceStatRow("Conversations started", ad.conversationsStarted.toString())
        PerformanceStatRow("Cost per conversation", ad.costPerConversation)
        PerformanceStatRow("Post engagements", ad.postEngagements.toString())
        PerformanceStatRow("Cost per post engagement", ad.costPerPostEngagement)
    }
}

// ── Audience ──

@Composable
private fun AudienceTab(ad: AdItem) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column(modifier = Modifier.padding(dimensions.wdsSpacingDouble)) {
        DurationHeader(ad.adDuration)

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

        // Reach
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
        ) {
            Text(
                text = "Reach",
                style = typography.body1Emphasized,
                color = colors.colorContentDefault
            )
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = colors.colorContentDeemphasized
            )
        }

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

        val reachStr = if (ad.reach >= 1000) {
            val k = ad.reach / 1000.0
            if (k == k.toLong().toDouble()) "${k.toLong()}K" else "${"%.1f".format(k)}K"
        } else ad.reach.toString()

        Text(
            text = reachStr,
            style = typography.headline1,
            color = colors.colorContentDefault,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "People reached",
            style = typography.body2,
            color = colors.colorContentDeemphasized,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))
        WDSDivider()
        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSinglePlus))

        // Gender breakdown
        Text(
            text = "Gender",
            style = typography.body2,
            color = colors.colorContentDeemphasized
        )
        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

        val women = (ad.reach * 0.59).toInt()
        val men = (ad.reach * 0.34).toInt()
        val nonBinary = ad.reach - women - men

        PerformanceStatRow("Women", women.toString())
        PerformanceStatRow("Men", men.toString())
        PerformanceStatRow("Non-binary", nonBinary.toString())

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSinglePlus))
        WDSDivider()
        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSinglePlus))

        // Age range
        Text(
            text = "Age range",
            style = typography.body2,
            color = colors.colorContentDeemphasized
        )
        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

        PerformanceStatRow("18-24", "8.2%")
        PerformanceStatRow("25-34", "18.5%")
        PerformanceStatRow("35-44", "21.3%")
        PerformanceStatRow("45-54", "29.4%")
        PerformanceStatRow("55-64", "16.1%")
        PerformanceStatRow("65+", "6.5%")
    }
}

// ── Placements ──

@Composable
private fun PlacementsTab(ad: AdItem) {
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val colors = WdsTheme.colors

    Column(modifier = Modifier.padding(dimensions.wdsSpacingDouble)) {
        DurationHeader(ad.adDuration)

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

        Text(
            text = "Where your ad is shown",
            style = typography.body1Emphasized,
            color = colors.colorContentDefault
        )

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSinglePlus))

        val total = ad.postEngagements.coerceAtLeast(1)
        val fbDesktop = (total * 0.34).toInt().coerceAtLeast(1)
        val fbMobile = (total * 0.26).toInt().coerceAtLeast(1)
        val igFeed = (total * 0.19).toInt().coerceAtLeast(1)
        val igStories = (total * 0.14).toInt().coerceAtLeast(1)
        val fbStories = total - fbDesktop - fbMobile - igFeed - igStories

        PerformanceStatRow("Facebook desktop feed", fbDesktop.toString())
        PerformanceStatRow("Facebook mobile feed", fbMobile.toString())
        PerformanceStatRow("Instagram feed", igFeed.toString())
        PerformanceStatRow("Instagram stories", igStories.toString())
        PerformanceStatRow("Facebook stories", fbStories.coerceAtLeast(0).toString())
    }
}

// ── Shared components ──

@Composable
private fun DurationHeader(duration: String) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    if (duration.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.colorSurfaceEmphasized)
                .padding(
                    horizontal = dimensions.wdsSpacingSinglePlus,
                    vertical = dimensions.wdsSpacingSingle
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Ad duration",
                style = typography.body2,
                color = colors.colorContentDeemphasized
            )
            Text(
                text = duration,
                style = typography.body2,
                color = colors.colorContentDefault
            )
        }
    }
}

@Composable
private fun PerformanceStatRow(label: String, value: String) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensions.wdsSpacingSingle),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = typography.body1,
            color = colors.colorContentDefault
        )
        Text(
            text = value,
            style = typography.body1,
            color = colors.colorContentDefault,
            textAlign = TextAlign.End
        )
    }
}
