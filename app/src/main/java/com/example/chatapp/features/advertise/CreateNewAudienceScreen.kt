@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

import androidx.compose.foundation.background
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
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSChip
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun CreateNewAudienceScreen(
    onNavigateBack: () -> Unit,
    onSave: () -> Unit,
    onEditLocation: (currentLocation: String) -> Unit = {},
    onEditInterests: (currentInterests: String) -> Unit = {},
    adViewModel: AdCreationViewModel
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    val editId = adViewModel.editingAudienceId
    val isEditDefault = editId == "default"
    val editingSaved = if (editId != null && !isEditDefault) {
        adViewModel.savedAudiences.find { it.id == editId }
    } else null
    val isEditMode = isEditDefault || editingSaved != null

    // Pre-populate from the audience being edited, or use fresh defaults for create.
    // If editingLocations is non-empty, it was updated by EditLocationScreen.
    var location by rememberSaveable {
        mutableStateOf(
            when {
                isEditDefault -> adViewModel.defaultLocation
                editingSaved != null -> editingSaved.location
                else -> "United States"
            }
        )
    }
    if (adViewModel.editingLocations.isNotEmpty()) {
        val updated = adViewModel.editingLocations.joinToString(", ")
        if (updated != location) {
            location = updated
        }
        adViewModel.editingLocations.clear()
    }
    var interests by rememberSaveable {
        mutableStateOf(
            when {
                isEditDefault -> adViewModel.defaultInterests
                editingSaved != null -> editingSaved.interests
                else -> ""
            }
        )
    }
    if (adViewModel.editingInterests.isNotEmpty()) {
        val updated = adViewModel.editingInterests.joinToString(", ")
        if (updated != interests) {
            interests = updated
        }
        adViewModel.editingInterests.clear()
    }
    var ageMin by rememberSaveable {
        mutableStateOf(
            when {
                isEditDefault -> adViewModel.defaultAgeMin
                editingSaved != null -> editingSaved.ageMin
                else -> 18
            }
        )
    }
    var ageMax by rememberSaveable {
        mutableStateOf(
            when {
                isEditDefault -> adViewModel.defaultAgeMax
                editingSaved != null -> editingSaved.ageMax
                else -> 65
            }
        )
    }
    var selectedGender by rememberSaveable {
        mutableStateOf(
            when {
                isEditDefault -> adViewModel.defaultGender
                editingSaved != null -> editingSaved.gender
                else -> "All"
            }
        )
    }
    var advantagePlusEnabled by rememberSaveable {
        mutableStateOf(
            when {
                isEditDefault -> adViewModel.defaultAdvantagePlusEnabled
                editingSaved != null -> editingSaved.advantagePlusEnabled
                else -> true
            }
        )
    }
    var audienceName by rememberSaveable {
        mutableStateOf(
            when {
                editingSaved != null -> editingSaved.name
                else -> {
                    val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    "My Audience $dateStr"
                }
            }
        )
    }

    val ageRange = ageMin.toFloat()..ageMax.toFloat()

    val screenTitle = if (isEditMode) "Edit audience" else "Create new audience"
    val buttonText = "Save audience"

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = screenTitle,
                onNavigateBack = onNavigateBack,
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
                        text = "Estimated daily reach",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
                    ) {
                        Text(
                            text = "4.3K–11K accounts",
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
                        onClick = {
                            when {
                                isEditDefault -> {
                                    adViewModel.updateDefaultAudience(
                                        location = location,
                                        interests = interests,
                                        ageMin = ageMin,
                                        ageMax = ageMax,
                                        gender = selectedGender,
                                        advantagePlusEnabled = advantagePlusEnabled
                                    )
                                }
                                editingSaved != null -> {
                                    adViewModel.updateSavedAudience(
                                        editingSaved.copy(
                                            name = audienceName,
                                            location = location,
                                            interests = interests,
                                            ageMin = ageMin,
                                            ageMax = ageMax,
                                            gender = selectedGender,
                                            advantagePlusEnabled = advantagePlusEnabled
                                        )
                                    )
                                }
                                else -> {
                                    adViewModel.saveNewAudience(
                                        SavedAudience(
                                            id = UUID.randomUUID().toString(),
                                            name = audienceName,
                                            location = location,
                                            interests = interests,
                                            ageMin = ageMin,
                                            ageMax = ageMax,
                                            gender = selectedGender,
                                            advantagePlusEnabled = advantagePlusEnabled
                                        )
                                    )
                                }
                            }
                            adViewModel.editingAudienceId = null
                            onSave()
                        },
                        text = buttonText,
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

            // Advantage+ audience toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSinglePlus
                    ),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Advantage+ audience",
                        style = typography.body1Emphasized,
                        color = colors.colorContentDefault
                    )
                    Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuarter))
                    Text(
                        text = "Automatically finds and adapts your audience to likely help improve ad\u2019s performance.",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                }
                Switch(
                    checked = advantagePlusEnabled,
                    onCheckedChange = { advantagePlusEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colors.colorSurfaceDefault,
                        checkedTrackColor = colors.colorContentDefault,
                        uncheckedThumbColor = colors.colorContentDeemphasized,
                        uncheckedTrackColor = colors.colorSurfaceEmphasized,
                        uncheckedBorderColor = colors.colorOutlineDefault
                    )
                )
            }

            WDSDivider(startIndent = dimensions.wdsSpacingDouble, endIndent = dimensions.wdsSpacingDouble)

            // Locations
            FieldRow(
                label = "Locations",
                value = location,
                onEditClick = { onEditLocation(location) }
            )

            WDSDivider(startIndent = dimensions.wdsSpacingDouble, endIndent = dimensions.wdsSpacingDouble)

            // Interests
            FieldRow(
                label = "Interests",
                value = interests.ifEmpty { "Add interests" },
                isPlaceholder = interests.isEmpty(),
                onEditClick = { onEditInterests(interests) }
            )

            WDSDivider(startIndent = dimensions.wdsSpacingDouble, endIndent = dimensions.wdsSpacingDouble)

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

            // Age section
            Text(
                text = "Age",
                style = typography.body1Emphasized,
                color = colors.colorContentDefault,
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
            )

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.wdsSpacingDouble),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ageMin.toString(),
                    style = typography.body2,
                    color = colors.colorContentDefault
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = dimensions.wdsSpacingSingle)
                ) {
                    RangeSlider(
                        value = ageRange,
                        onValueChange = { range ->
                            ageMin = range.start.toInt()
                            ageMax = range.endInclusive.toInt()
                        },
                        valueRange = 13f..65f,
                        colors = SliderDefaults.colors(
                            thumbColor = colors.colorContentDefault,
                            activeTrackColor = colors.colorContentDefault,
                            inactiveTrackColor = colors.colorDivider
                        )
                    )
                }

                Text(
                    text = ageMax.toString(),
                    style = typography.body2,
                    color = colors.colorContentDefault
                )
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

            // Gender section
            Text(
                text = "Gender",
                style = typography.body1Emphasized,
                color = colors.colorContentDefault,
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
            )

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

            Row(
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble),
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
            ) {
                listOf("All", "Men", "Women").forEach { gender ->
                    WDSChip(
                        text = gender,
                        selected = selectedGender == gender,
                        onClick = { selectedGender = gender }
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

            // Audience name — only show for create new or edit saved (not edit default)
            if (!isEditDefault) {
                WDSDivider(startIndent = dimensions.wdsSpacingDouble, endIndent = dimensions.wdsSpacingDouble)

                FieldRow(
                    label = "Audience name",
                    value = audienceName,
                    onEditClick = { }
                )
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuad))
        }
    }
}

@Composable
private fun FieldRow(
    label: String,
    value: String,
    isPlaceholder: Boolean = false,
    onEditClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = typography.body1Emphasized,
                color = colors.colorContentDefault
            )
            Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuarter))
            Text(
                text = value,
                style = typography.body2,
                color = if (isPlaceholder) colors.colorContentDeemphasized
                        else colors.colorContentDefault
            )
        }
        IconButton(
            onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onEditClick() },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit $label",
                modifier = Modifier.size(20.dp),
                tint = colors.colorContentDeemphasized
            )
        }
    }
}
