@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

import androidx.compose.foundation.background
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.ui.draw.clip
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.components.WdsCheckbox
import com.example.chatapp.wds.components.WdsRadioButton
import com.example.chatapp.wds.theme.WdsTheme
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView

@Composable
fun AudienceScreen(
    onNavigateBack: () -> Unit,
    onNextClick: () -> Unit,
    onCreateNewAudience: () -> Unit = {},
    onEditAudience: (String) -> Unit = {},
    reviewMode: Boolean = false,
    adViewModel: AdCreationViewModel
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    val hasSavedAudiences = adViewModel.savedAudiences.isNotEmpty()

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Audience",
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estimated daily impressions",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
                        ) {
                            Text(
                                text = "425 - 1.3K",
                                style = typography.body2Emphasized,
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
                AdCreationProgressBar(toProgress = 0.5f, adViewModel = adViewModel)
            }

            // Headline
            Column(
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingDouble
                )
            ) {
                Text(
                    text = "Choose who should see your ad",
                    style = typography.headline1,
                    color = colors.colorContentDefault
                )
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingHalf))
                Text(
                    text = "If you want to use previous ad settings from your Facebook account, log into Facebook.",
                    style = typography.body2,
                    color = colors.colorContentDeemphasized
                )
            }

            if (hasSavedAudiences) {
                // --- Layout with radio buttons when saved audiences exist ---
                val isDefaultSelected = adViewModel.selectedAudienceId == null

                // Suggested audience option
                AudienceRadioOption(
                    label = "Suggested audience",
                    subtitle = "Uses advantage+ audience to find people most likely to engage with your ad.",
                    selected = isDefaultSelected,
                    onClick = { adViewModel.selectAudience(null) },
                    detailsContent = if (isDefaultSelected) {
                        {
                            SuggestedAudienceDetailsCard(
                                location = adViewModel.audienceLocation,
                                minimumAge = adViewModel.defaultAgeMin,
                                advantagePlusEnabled = adViewModel.defaultAdvantagePlusEnabled,
                                onEditClick = { onEditAudience("default") }
                            )
                        }
                    } else null
                )

                // Saved audience options
                adViewModel.savedAudiences.forEach { audience ->
                    val isSelected = adViewModel.selectedAudienceId == audience.id

                    AudienceRadioOption(
                        label = audience.name,
                        selected = isSelected,
                        onClick = { adViewModel.selectAudience(audience) },
                        detailsContent = if (isSelected) {
                            {
                                AudienceDetailsCard(
                                    location = audience.location,
                                    ageRange = "${audience.ageMin} - ${audience.ageMax}+",
                                    gender = audience.gender,
                                    interests = audience.interests,
                                    advantagePlusEnabled = audience.advantagePlusEnabled,
                                    onEditClick = { onEditAudience(audience.id) }
                                )
                            }
                        } else null
                    )
                }
            } else {
                // --- Original layout when no saved audiences ---
                Column(
                    modifier = Modifier.padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSingle
                    )
                ) {
                    Text(
                        text = "Suggested audience",
                        style = typography.body1Emphasized,
                        color = colors.colorContentDefault
                    )
                    Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuarter))
                    Text(
                        text = "Uses advantage+ audience to find people most likely to engage with your ad.",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                }

                SuggestedAudienceDetailsCard(
                    location = adViewModel.audienceLocation,
                    minimumAge = adViewModel.defaultAgeMin,
                    advantagePlusEnabled = adViewModel.defaultAdvantagePlusEnabled,
                    onEditClick = { onEditAudience("default") },
                    modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
                )
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

            // Create new audience button
            Box(
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
            ) {
                WDSButton(
                    onClick = onCreateNewAudience,
                    text = "Create new audience",
                    variant = WDSButtonVariant.OUTLINE
                )
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingTriple))

            // Special requirements section
            Text(
                text = "Special requirements",
                style = typography.body1Emphasized,
                color = colors.colorContentDefault,
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingSingle
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.wdsSpacingDouble),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
            ) {
                WdsCheckbox(
                    checked = adViewModel.specialAdChecked,
                    onCheckedChange = { adViewModel.specialAdChecked = it }
                )
                Text(
                    text = "Is your ad about credit, employment, house, social issues, elections or politics?",
                    style = typography.body2,
                    color = colors.colorContentDefault,
                    modifier = Modifier.padding(top = dimensions.wdsSpacingSinglePlus)
                )
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuad))
        }
    }
}

@Composable
private fun AudienceRadioOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    subtitle: String? = null,
    detailsContent: (@Composable () -> Unit)? = null
) {
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val colors = WdsTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithSound(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensions.wdsSpacingDouble,
                    end = dimensions.wdsSpacingDouble,
                    top = dimensions.wdsSpacingSingle,
                    bottom = if (detailsContent != null) dimensions.wdsSpacingHalf else dimensions.wdsSpacingSingle
                ),
            verticalAlignment = if (subtitle != null) Alignment.Top else Alignment.CenterVertically
        ) {
            WdsRadioButton(
                selected = selected,
                onClick = onClick
            )
            Column(
                modifier = Modifier
                    .padding(start = dimensions.wdsSpacingSingle)
                    .weight(1f)
            ) {
                Text(
                    text = label,
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuarter))
                    Text(
                        text = subtitle,
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                }
                if (detailsContent != null) {
                    Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))
                    detailsContent()
                    Spacer(modifier = Modifier.height(dimensions.wdsSpacingHalf))
                }
            }
        }
    }
}

@Composable
private fun AudienceDetailsCard(
    location: String,
    ageRange: String,
    gender: String,
    interests: String = "",
    advantagePlusEnabled: Boolean = true,
    onEditClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(WdsTheme.shapes.double)
            .background(colors.colorSurfaceEmphasized)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.wdsSpacingSinglePlus),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Location: $location",
                    style = typography.body2,
                    color = colors.colorContentDefault
                )
                Text(
                    text = "Age: $ageRange",
                    style = typography.body2,
                    color = colors.colorContentDefault
                )
                Text(
                    text = "Gender: $gender",
                    style = typography.body2,
                    color = colors.colorContentDefault
                )
                if (interests.isNotEmpty()) {
                    Text(
                        text = "Interests: $interests",
                        style = typography.body2,
                        color = colors.colorContentDefault
                    )
                }
                Text(
                    text = "Advantage+ audience:  ${if (advantagePlusEnabled) "On" else "Off"}",
                    style = typography.body2,
                    color = colors.colorContentDefault
                )
            }
            IconButton(
                onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onEditClick() },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit audience",
                    modifier = Modifier.size(20.dp),
                    tint = colors.colorContentDeemphasized
                )
            }
        }
    }
}

@Composable
private fun SuggestedAudienceDetailsCard(
    location: String,
    minimumAge: Int,
    advantagePlusEnabled: Boolean = true,
    onEditClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(WdsTheme.shapes.double)
            .background(colors.colorSurfaceEmphasized)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.wdsSpacingSinglePlus),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Location:  $location",
                    style = typography.body2,
                    color = colors.colorContentDefault
                )
                Text(
                    text = "Minimum age:  $minimumAge",
                    style = typography.body2,
                    color = colors.colorContentDefault
                )
                Text(
                    text = "Advantage+ audience:  ${if (advantagePlusEnabled) "On" else "Off"}",
                    style = typography.body2,
                    color = colors.colorContentDefault
                )
            }
            IconButton(
                onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onEditClick() },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit audience",
                    modifier = Modifier.size(20.dp),
                    tint = colors.colorContentDeemphasized
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AudienceScreenPreview() {
    WdsTheme(darkTheme = false) {
        AudienceScreen(
            onNavigateBack = {},
            onNextClick = {},
            adViewModel = AdCreationViewModel(CreatedAdsStore())
        )
    }
}
