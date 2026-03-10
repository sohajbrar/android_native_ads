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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Visibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.components.wdsCardBorder
import com.example.chatapp.wds.theme.WdsTheme
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView

@Composable
fun DesignAdScreen(
    onNavigateBack: () -> Unit,
    onNextClick: () -> Unit,
    onPreviewClick: () -> Unit = {},
    selectedMediaUri: String? = null,
    reviewMode: Boolean = false,
    adViewModel: AdCreationViewModel? = null
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Design ad",
                onNavigateBack = onNavigateBack,
                showCloseButton = reviewMode,
                actions = {
                    IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK) }) {
                        Icon(
                            imageVector = Icons.Outlined.HelpOutline,
                            contentDescription = "Help",
                            tint = colors.colorContentDefault
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (!reviewMode) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.colorSurfaceDefault)
                        .navigationBarsPadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableWithSound(onClick = onPreviewClick)
                            .padding(vertical = dimensions.wdsSpacingSingle),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = colors.colorContentDeemphasized
                        )
                        Spacer(modifier = Modifier.width(dimensions.wdsSpacingHalf))
                        Text(
                            text = "Preview ad on Instagram and Facebook",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensions.wdsSpacingDouble,
                                vertical = dimensions.wdsSpacingSingle
                            )
                    ) {
                        WDSButton(
                            onClick = onNextClick,
                            text = "Next",
                            variant = WDSButtonVariant.FILLED,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Text(
                        text = "Your business name, photo or video will be shared with Meta in order to create your ad. Learn more",
                        style = typography.body3,
                        color = colors.colorContentDeemphasized,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSingle
                        )
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
            if (!reviewMode && adViewModel != null) {
                AdCreationProgressBar(toProgress = 0.25f, adViewModel = adViewModel)
            }

            // Headline
            Column(
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingDouble
                )
            ) {
                Text(
                    text = "Show people what you offer",
                    style = typography.headline1,
                    color = colors.colorContentDefault
                )
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingHalf))
                Text(
                    text = "Ads with descriptions can lead to more customer conversations.",
                    style = typography.body2,
                    color = colors.colorContentDeemphasized
                )
            }

            // Ad Preview Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.wdsSpacingDouble),
                shape = WdsTheme.shapes.double,
                colors = CardDefaults.cardColors(
                    containerColor = colors.colorSurfaceDefault
                ),
                border = wdsCardBorder()
            ) {
                Column {
                    // Business name header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensions.wdsSpacingSinglePlus),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(colors.colorSurfaceEmphasized),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "LS",
                                style = typography.body2Emphasized,
                                color = colors.colorContentDefault
                            )
                        }
                        Column {
                            Text(
                                text = "Lucky Shrub",
                                style = typography.body1Emphasized,
                                color = colors.colorContentDefault
                            )
                            Text(
                                text = "Sponsored",
                                style = typography.body3,
                                color = colors.colorContentDeemphasized
                            )
                        }
                    }

                    // Ad image area with edit buttons
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(Color(0xFFE8E5E1))
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
                                style = typography.largeTitle1,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        // Overlay edit buttons
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(dimensions.wdsSpacingSingle),
                            horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(colors.colorSurfaceDefault.copy(alpha = 0.9f))
                                    .clickableWithSound { },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CopyAll,
                                    contentDescription = "Duplicate",
                                    modifier = Modifier.size(20.dp),
                                    tint = colors.colorContentDefault
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(WdsTheme.shapes.circle)
                                    .background(colors.colorSurfaceDefault.copy(alpha = 0.9f))
                                    .clickableWithSound { }
                                    .padding(
                                        horizontal = dimensions.wdsSpacingSinglePlus,
                                        vertical = dimensions.wdsSpacingSingle
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = colors.colorContentDefault
                                    )
                                    Text(
                                        text = "Edit",
                                        style = typography.body2Emphasized,
                                        color = colors.colorContentDefault
                                    )
                                }
                            }
                        }
                    }

                    // Description
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensions.wdsSpacingSinglePlus),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "Delhi's organic nursery offers the best selection of plants and tools for your weekend plant projects.",
                            style = typography.body2,
                            color = colors.colorContentDefault,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { view.playSoundEffect(SoundEffectConstants.CLICK) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit description",
                                modifier = Modifier.size(20.dp),
                                tint = colors.colorContentDeemphasized
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DesignAdScreenPreview() {
    WdsTheme(darkTheme = false) {
        DesignAdScreen(
            onNavigateBack = {},
            onNextClick = {}
        )
    }
}
