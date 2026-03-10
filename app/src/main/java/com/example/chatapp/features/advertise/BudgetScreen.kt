@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import com.example.chatapp.wds.components.WdsRadioButton
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView

@Composable
fun BudgetScreen(
    onNavigateBack: () -> Unit,
    onNextClick: () -> Unit,
    reviewMode: Boolean = false,
    adViewModel: AdCreationViewModel
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    val dailyBudget = adViewModel.dailyBudget
    val durationDays = adViewModel.durationDays
    val totalBudget = adViewModel.totalBudget
    val estimatedReachLow = adViewModel.estimatedReachLow
    val estimatedReachHigh = adViewModel.estimatedReachHigh

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Set your budget",
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
                            .padding(
                                horizontal = dimensions.wdsSpacingDouble,
                                vertical = dimensions.wdsSpacingSingle
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (adViewModel.isSetDuration) "Total budget" else "Daily budget",
                            style = typography.body1,
                            color = colors.colorContentDefault
                        )
                        Text(
                            text = if (adViewModel.isSetDuration) "₹${formatNumber(totalBudget)}" else "₹$dailyBudget",
                            style = typography.body1Emphasized,
                            color = colors.colorContentDefault
                        )
                    }

                    // Estimated reach row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensions.wdsSpacingDouble,
                                vertical = dimensions.wdsSpacingHalf
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estimated reach",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
                        ) {
                            Text(
                                text = "$estimatedReachLow–$estimatedReachHigh accounts",
                                style = typography.body2,
                                color = colors.colorContentDefault
                            )
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Info",
                                modifier = Modifier.size(16.dp),
                                tint = colors.colorContentDeemphasized
                            )
                        }
                    }

                    // Next button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensions.wdsSpacingDouble,
                                vertical = dimensions.wdsSpacingSinglePlus
                            )
                    ) {
                        WDSButton(
                            onClick = onNextClick,
                            text = "Next",
                            variant = WDSButtonVariant.FILLED,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
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
            if (!reviewMode) {
                AdCreationProgressBar(toProgress = 0.75f, adViewModel = adViewModel)
            }

            // Headline
            Column(
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingDouble
                )
            ) {
                Text(
                    text = "What's your ad budget?",
                    style = typography.headline1,
                    color = colors.colorContentDefault
                )
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingHalf))
                Text(
                    text = "The budget and duration you set impact your ads' reach.",
                    style = typography.body2,
                    color = colors.colorContentDeemphasized
                )
            }

            // Daily Budget section
            Text(
                text = "Daily Budget",
                style = typography.body1Emphasized,
                color = colors.colorContentDefault,
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingSingle
                )
            )

            // Budget value
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹$dailyBudget",
                    style = typography.largeTitle1,
                    color = colors.colorContentDefault
                )
                IconButton(
                    onClick = { view.playSoundEffect(SoundEffectConstants.CLICK) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit budget",
                        modifier = Modifier.size(18.dp),
                        tint = colors.colorContentDefault
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

            // Budget slider
            Column(
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
            ) {
                Slider(
                    value = adViewModel.budgetSliderPosition,
                    onValueChange = { adViewModel.budgetSliderPosition = it },
                    colors = SliderDefaults.colors(
                        thumbColor = colors.colorContentDefault,
                        activeTrackColor = colors.colorContentDefault,
                        inactiveTrackColor = colors.colorDivider
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "₹90",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                    Text(
                        text = "₹20,000",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingTriple))

            // Duration section
            Text(
                text = "Duration",
                style = typography.body1Emphasized,
                color = colors.colorContentDefault,
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingSingle
                )
            )

            // Option 1: Run until I pause it
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableWithSound { adViewModel.isSetDuration = false }
                    .padding(
                        start = dimensions.wdsSpacingDouble,
                        end = dimensions.wdsSpacingDouble,
                        top = dimensions.wdsSpacingSingle,
                        bottom = dimensions.wdsSpacingSingle
                    ),
                verticalAlignment = if (adViewModel.isSetDuration) Alignment.CenterVertically else Alignment.Top
            ) {
                WdsRadioButton(
                    selected = !adViewModel.isSetDuration,
                    onClick = { adViewModel.isSetDuration = false }
                )
                Column(
                    modifier = Modifier.padding(start = dimensions.wdsSpacingSingle)
                ) {
                    Text(
                        text = "Run this ad until I pause it",
                        style = typography.body1,
                        color = colors.colorContentDefault
                    )
                    if (!adViewModel.isSetDuration) {
                        Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuarter))
                        Text(
                            text = "Your ad will continue to run unless you pause it, which you can do at any time under \u2018Manage ads\u2019.",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingHalf))

            // Option 2: Run for a set duration
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableWithSound { adViewModel.isSetDuration = true }
                    .padding(
                        start = dimensions.wdsSpacingDouble,
                        end = dimensions.wdsSpacingDouble,
                        top = dimensions.wdsSpacingSingle,
                        bottom = dimensions.wdsSpacingSingle
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WdsRadioButton(
                    selected = adViewModel.isSetDuration,
                    onClick = { adViewModel.isSetDuration = true }
                )
                Text(
                    text = "Run this ad for a set duration",
                    style = typography.body1,
                    color = colors.colorContentDefault,
                    modifier = Modifier.padding(start = dimensions.wdsSpacingSingle)
                )
            }

            // Duration slider — only visible when set duration is selected
            if (adViewModel.isSetDuration) {
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

                Text(
                    text = "$durationDays days",
                    style = typography.largeTitle1,
                    color = colors.colorContentDefault,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

                Column(
                    modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
                ) {
                    Slider(
                        value = adViewModel.durationSliderPosition,
                        onValueChange = { adViewModel.durationSliderPosition = it },
                        colors = SliderDefaults.colors(
                            thumbColor = colors.colorContentDefault,
                            activeTrackColor = colors.colorContentDefault,
                            inactiveTrackColor = colors.colorDivider
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuad))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetScreenPreview() {
    WdsTheme(darkTheme = false) {
        BudgetScreen(
            onNavigateBack = {},
            onNextClick = {},
            adViewModel = AdCreationViewModel(CreatedAdsStore())
        )
    }
}
