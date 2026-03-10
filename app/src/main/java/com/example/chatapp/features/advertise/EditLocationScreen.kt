@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.example.chatapp.features.advertise

import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSChip
import com.example.chatapp.wds.components.WDSChipAction
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.components.WdsRadioButton
import com.example.chatapp.wds.components.clickableWithSound
import com.example.chatapp.wds.theme.WdsTheme

data class LocationSuggestion(
    val name: String,
    val detail: String
)

private val allSuggestions = listOf(
    LocationSuggestion("United States", "Country"),
    LocationSuggestion("United Kingdom", "Country"),
    LocationSuggestion("India", "Country"),
    LocationSuggestion("Canada", "Country"),
    LocationSuggestion("Australia", "Country"),
    LocationSuggestion("Germany", "Country"),
    LocationSuggestion("France", "Country"),
    LocationSuggestion("New York", "New York, United States"),
    LocationSuggestion("Los Angeles", "California, United States"),
    LocationSuggestion("San Francisco", "California, United States"),
    LocationSuggestion("Chicago", "Illinois, United States"),
    LocationSuggestion("Houston", "Texas, United States"),
    LocationSuggestion("Miami", "Florida, United States"),
    LocationSuggestion("Seattle", "Washington, United States"),
    LocationSuggestion("Boston", "Massachusetts, United States"),
    LocationSuggestion("Dallas", "Texas, United States"),
    LocationSuggestion("Denver", "Colorado, United States"),
    LocationSuggestion("London", "England, United Kingdom"),
    LocationSuggestion("Mumbai", "Maharashtra, India"),
    LocationSuggestion("Delhi", "Delhi, India"),
    LocationSuggestion("Bangalore", "Karnataka, India"),
    LocationSuggestion("Bangalore Ave", "Gate Town, Western Cape, South Africa"),
    LocationSuggestion("Pune Bangalore Highway", "Bangalore, Karnataka, India"),
    LocationSuggestion("Bangalore Road", "Bangalore, Twin Oaks, India"),
    LocationSuggestion("Bangalore Nishanthanagar Road", "Bangalore, Karnataka, India"),
    LocationSuggestion("Bangalore Elevated Tollway Limited", "Bangalore, Karnataka, India"),
    LocationSuggestion("Bangalore Indiranagar Road", "Bangalore, Karnataka, India"),
    LocationSuggestion("Hyderabad", "Telangana, India"),
    LocationSuggestion("Chennai", "Tamil Nadu, India"),
    LocationSuggestion("Toronto", "Ontario, Canada"),
    LocationSuggestion("Vancouver", "British Columbia, Canada"),
    LocationSuggestion("Sydney", "New South Wales, Australia"),
    LocationSuggestion("Melbourne", "Victoria, Australia"),
    LocationSuggestion("Berlin", "Germany"),
    LocationSuggestion("Paris", "France"),
    LocationSuggestion("Tokyo", "Japan"),
    LocationSuggestion("Singapore", "Country"),
    LocationSuggestion("Dubai", "United Arab Emirates")
)

private enum class LocationMode { REGIONAL, LOCAL }

@Composable
fun EditLocationScreen(
    onNavigateBack: () -> Unit,
    onSave: (List<String>) -> Unit,
    initialLocations: List<String>
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    var mode by rememberSaveable { mutableStateOf(LocationMode.REGIONAL) }

    // Regional state
    var selectedLocations by rememberSaveable {
        mutableStateOf(initialLocations.toSet())
    }
    var regionalSearch by rememberSaveable { mutableStateOf("") }

    // Local state
    var localLocation by rememberSaveable { mutableStateOf("") }
    var localRadius by rememberSaveable { mutableIntStateOf(8) }
    var localSearch by rememberSaveable { mutableStateOf("") }
    var isLocalSearching by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Edit location",
                onNavigateBack = {
                    if (isLocalSearching) {
                        isLocalSearching = false
                        localSearch = ""
                    } else {
                        onNavigateBack()
                    }
                },
                showCloseButton = !isLocalSearching
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.colorSurfaceDefault)
                    .navigationBarsPadding()
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSinglePlus
                    )
            ) {
                WDSButton(
                    onClick = {
                        when (mode) {
                            LocationMode.REGIONAL -> onSave(selectedLocations.toList())
                            LocationMode.LOCAL -> {
                                val label = if (localLocation.isNotEmpty())
                                    "$localLocation (${localRadius} km)"
                                else "Current location (${localRadius} km)"
                                onSave(listOf(label))
                            }
                        }
                    },
                    text = "Save",
                    variant = WDSButtonVariant.FILLED,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        if (isLocalSearching) {
            LocalSearchContent(
                modifier = Modifier.padding(paddingValues),
                searchQuery = localSearch,
                onSearchChange = { localSearch = it },
                onLocationSelected = { suggestion ->
                    localLocation = suggestion.name
                    isLocalSearching = false
                    localSearch = ""
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Location label
                Text(
                    text = "Location",
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    modifier = Modifier.padding(
                        start = dimensions.wdsSpacingDouble,
                        top = dimensions.wdsSpacingDouble,
                        bottom = dimensions.wdsSpacingSingle
                    )
                )

                // Regional option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableWithSound { mode = LocationMode.REGIONAL }
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSingle
                        ),
                    verticalAlignment = Alignment.Top
                ) {
                    WdsRadioButton(
                        selected = mode == LocationMode.REGIONAL,
                        onClick = { mode = LocationMode.REGIONAL }
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = dimensions.wdsSpacingSingle)
                            .weight(1f)
                    ) {
                        Text(
                            text = "Regional",
                            style = typography.body1Emphasized,
                            color = colors.colorContentDefault
                        )
                        if (selectedLocations.isNotEmpty()) {
                            Text(
                                text = selectedLocations.joinToString(", "),
                                style = typography.body2,
                                color = colors.colorContentDeemphasized,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Local option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableWithSound { mode = LocationMode.LOCAL }
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSingle
                        ),
                    verticalAlignment = Alignment.Top
                ) {
                    WdsRadioButton(
                        selected = mode == LocationMode.LOCAL,
                        onClick = { mode = LocationMode.LOCAL }
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = dimensions.wdsSpacingSingle)
                            .weight(1f)
                    ) {
                        Text(
                            text = "Local",
                            style = typography.body1Emphasized,
                            color = colors.colorContentDefault
                        )
                        Text(
                            text = "Set a radius around a chosen location",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized
                        )
                    }
                    if (mode == LocationMode.LOCAL) {
                        IconButton(
                            onClick = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                isLocalSearching = true
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit location",
                                modifier = Modifier.size(20.dp),
                                tint = colors.colorContentDeemphasized
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

                when (mode) {
                    LocationMode.REGIONAL -> RegionalContent(
                        selectedLocations = selectedLocations,
                        searchQuery = regionalSearch,
                        onSearchChange = { regionalSearch = it },
                        onAddLocation = { selectedLocations = selectedLocations + it },
                        onRemoveLocation = { selectedLocations = selectedLocations - it }
                    )
                    LocationMode.LOCAL -> LocalContent(
                        localLocation = localLocation,
                        radius = localRadius,
                        onRadiusChange = { localRadius = it },
                        onUseMyLocation = { localLocation = "Current location" },
                        onSearchClick = { isLocalSearching = true }
                    )
                }
            }
        }
    }
}

// ─── Regional Mode ──────────────────────────────────────────────────────────

@Composable
private fun RegionalContent(
    selectedLocations: Set<String>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onAddLocation: (String) -> Unit,
    onRemoveLocation: (String) -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val filteredSuggestions by remember(searchQuery) {
        derivedStateOf {
            if (searchQuery.isBlank()) emptyList()
            else allSuggestions.filter { s ->
                s.name.contains(searchQuery, ignoreCase = true) && s.name !in selectedLocations
            }
        }
    }

    // Search bar
    SearchBar(
        query = searchQuery,
        onQueryChange = onSearchChange,
        focusRequester = focusRequester
    )

    AnimatedVisibility(visible = searchQuery.isBlank()) {
        Column {
            Text(
                text = "Add a broad range of locations to cover the largest surrounding areas.",
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingSingle
                )
            )

            Text(
                text = "Towns or cities",
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                modifier = Modifier.padding(
                    start = dimensions.wdsSpacingDouble,
                    top = dimensions.wdsSpacingSingle,
                    bottom = dimensions.wdsSpacingHalf
                )
            )

            FlowRow(
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble),
                horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf),
                verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
            ) {
                selectedLocations.forEach { loc ->
                    WDSChip(
                        text = loc,
                        selected = true,
                        onClick = { onRemoveLocation(loc) },
                        action = WDSChipAction.CLOSE
                    )
                }
            }
        }
    }

    AnimatedVisibility(visible = searchQuery.isNotBlank()) {
        Column {
            filteredSuggestions.forEach { suggestion ->
                LocationResultRow(
                    suggestion = suggestion,
                    isSelected = suggestion.name in selectedLocations,
                    onClick = {
                        if (suggestion.name in selectedLocations) onRemoveLocation(suggestion.name)
                        else onAddLocation(suggestion.name)
                    }
                )
            }
            if (filteredSuggestions.isEmpty()) {
                Text(
                    text = "No locations found for \"$searchQuery\"",
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    modifier = Modifier.padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingDouble
                    )
                )
            }
        }
    }
}

// ─── Local Mode ─────────────────────────────────────────────────────────────

@Composable
private fun LocalContent(
    localLocation: String,
    radius: Int,
    onRadiusChange: (Int) -> Unit,
    onUseMyLocation: () -> Unit,
    onSearchClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val radiusColor = colors.colorAccentDeemphasized
    val pinColor = colors.colorNegative

    // Map placeholder with radius circle
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.wdsSpacingDouble)
            .aspectRatio(1.4f)
            .clip(WdsTheme.shapes.double)
            .background(colors.colorSurfaceEmphasized),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radiusPx = (size.minDimension / 2) * (radius / 20f).coerceIn(0.15f, 0.9f)

            drawCircle(
                color = radiusColor.copy(alpha = 0.3f),
                radius = radiusPx,
                center = center
            )
            drawCircle(
                color = radiusColor.copy(alpha = 0.5f),
                radius = radiusPx,
                center = center,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )
        }

        // Pin icon at center
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .offset(y = (-8).dp),
            tint = pinColor
        )

        // Location label
        if (localLocation.isNotEmpty()) {
            Text(
                text = localLocation,
                style = typography.body3,
                color = colors.colorContentDefault,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(dimensions.wdsSpacingSingle)
                    .clip(WdsTheme.shapes.single)
                    .background(colors.colorSurfaceDefault.copy(alpha = 0.85f))
                    .padding(
                        horizontal = dimensions.wdsSpacingSingle,
                        vertical = dimensions.wdsSpacingHalf
                    )
            )
        }

        // "Use my location" button
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = dimensions.wdsSpacingSingle)
                .clip(WdsTheme.shapes.circle)
                .background(colors.colorAccent)
                .clickableWithSound(onClick = onUseMyLocation)
                .padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingSingle
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = colors.colorContentOnAccent
            )
            Text(
                text = "Use my location",
                style = typography.body2Emphasized,
                color = colors.colorContentOnAccent
            )
        }
    }

    Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

    // Radius label
    Text(
        text = "Radius: $radius km",
        style = typography.body2,
        color = colors.colorContentDeemphasized,
        modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
    )

    Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

    // Radius slider
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.wdsSpacingDouble),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
    ) {
        Text(
            text = "01",
            style = typography.body2,
            color = colors.colorContentDefault
        )

        var sliderValue by remember { mutableFloatStateOf(radius.toFloat()) }

        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                onRadiusChange(it.toInt())
            },
            valueRange = 1f..20f,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = colors.colorContentDefault,
                activeTrackColor = colors.colorContentDefault,
                inactiveTrackColor = colors.colorDivider
            )
        )

        Text(
            text = "20",
            style = typography.body2,
            color = colors.colorContentDefault
        )
    }

    Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))
}

// ─── Local Search (full screen) ─────────────────────────────────────────────

@Composable
private fun LocalSearchContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onLocationSelected: (LocationSuggestion) -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val focusRequester = remember { FocusRequester() }

    val filteredSuggestions by remember(searchQuery) {
        derivedStateOf {
            if (searchQuery.isBlank()) emptyList()
            else allSuggestions.filter { s ->
                s.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchChange,
            focusRequester = focusRequester
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(filteredSuggestions, key = { it.name }) { suggestion ->
                LocationResultRow(
                    suggestion = suggestion,
                    isSelected = false,
                    onClick = { onLocationSelected(suggestion) }
                )
            }

            if (filteredSuggestions.isEmpty() && searchQuery.isNotBlank()) {
                item {
                    Text(
                        text = "No locations found for \"$searchQuery\"",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized,
                        modifier = Modifier.padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingDouble
                        )
                    )
                }
            }
        }
    }
}

// ─── Shared Components ──────────────────────────────────────────────────────

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSingle
            )
            .clip(WdsTheme.shapes.circle)
            .background(colors.colorSurfaceHighlight)
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSingle
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            modifier = Modifier.size(dimensions.wdsIconSizeMedium),
            tint = colors.colorContentDeemphasized
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimensions.wdsSpacingSingle),
            contentAlignment = Alignment.CenterStart
        ) {
            if (query.isEmpty()) {
                Text(
                    text = "Search",
                    style = typography.body1,
                    color = colors.colorContentDeemphasized
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                textStyle = typography.body1.copy(color = colors.colorContentDefault),
                singleLine = true,
                cursorBrush = SolidColor(colors.colorAccent),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
        }

        if (query.isNotEmpty()) {
            IconButton(
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onQueryChange("")
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    modifier = Modifier.size(20.dp),
                    tint = colors.colorContentDeemphasized
                )
            }
        }
    }
}

@Composable
private fun LocationResultRow(
    suggestion: LocationSuggestion,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithSound(onClick = onClick)
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingDouble)
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = colors.colorContentDeemphasized
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = suggestion.name,
                style = typography.body1,
                color = colors.colorContentDefault
            )
            Text(
                text = suggestion.detail,
                style = typography.body2,
                color = colors.colorContentDeemphasized
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Selected",
                modifier = Modifier.size(24.dp),
                tint = colors.colorPositive
            )
        }
    }
}
