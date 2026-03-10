@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.components.clickableWithSound
import com.example.chatapp.wds.theme.WdsTheme

private data class InterestItem(
    val name: String,
    val parentCategory: String
)

private data class InterestCategory(
    val name: String,
    val subcategory: String,
    val interests: List<InterestItem>
)

private val interestCategories = listOf(
    InterestCategory(
        name = "Business and industry",
        subcategory = "business",
        interests = listOf(
            InterestItem("Advertising", "business"),
            InterestItem("Agriculture", "business"),
            InterestItem("Architecture", "business"),
            InterestItem("Banking", "business"),
            InterestItem("Business", "business"),
            InterestItem("Construction", "business"),
            InterestItem("Entrepreneurship", "business"),
            InterestItem("Finance", "business"),
            InterestItem("Marketing", "business"),
            InterestItem("Real estate", "business"),
            InterestItem("Small business", "business")
        )
    ),
    InterestCategory(
        name = "Entertainment (leisure)",
        subcategory = "entertainment",
        interests = listOf(
            InterestItem("Games", "entertainment"),
            InterestItem("Live events", "entertainment"),
            InterestItem("Movies", "entertainment"),
            InterestItem("Music", "entertainment"),
            InterestItem("Reading", "entertainment"),
            InterestItem("Television", "entertainment")
        )
    ),
    InterestCategory(
        name = "Family and relationships",
        subcategory = "family",
        interests = listOf(
            InterestItem("Dating", "family"),
            InterestItem("Family", "family"),
            InterestItem("Fatherhood", "family"),
            InterestItem("Friendship", "family"),
            InterestItem("Marriage", "family"),
            InterestItem("Motherhood", "family"),
            InterestItem("Parenting", "family"),
            InterestItem("Weddings", "family")
        )
    ),
    InterestCategory(
        name = "Fitness and wellness (fitness)",
        subcategory = "fitness",
        interests = listOf(
            InterestItem("Bodybuilding", "fitness"),
            InterestItem("Dieting", "fitness"),
            InterestItem("Gyms", "fitness"),
            InterestItem("Meditation", "fitness"),
            InterestItem("Nutrition", "fitness"),
            InterestItem("Physical fitness", "fitness"),
            InterestItem("Running", "fitness"),
            InterestItem("Weight training", "fitness"),
            InterestItem("Yoga", "fitness")
        )
    ),
    InterestCategory(
        name = "Food and drink (consumables)",
        subcategory = "food",
        interests = listOf(
            InterestItem("Alcoholic beverages", "food"),
            InterestItem("Beverages", "food"),
            InterestItem("Coffee", "food"),
            InterestItem("Cooking", "food"),
            InterestItem("Cuisine", "food"),
            InterestItem("Food", "food"),
            InterestItem("Organic food", "food"),
            InterestItem("Restaurants", "food"),
            InterestItem("Tea", "food")
        )
    ),
    InterestCategory(
        name = "Hobbies and activities",
        subcategory = "hobbies",
        interests = listOf(
            InterestItem("Arts and music (art)", "hobbies"),
            InterestItem("Current events (politics)", "hobbies"),
            InterestItem("Home and garden", "hobbies"),
            InterestItem("Pets (animals)", "hobbies"),
            InterestItem("Politics and social issues (political)", "hobbies"),
            InterestItem("Travel (travel & tourism)", "hobbies"),
            InterestItem("Vehicles (transportation)", "hobbies")
        )
    ),
    InterestCategory(
        name = "Shopping and fashion",
        subcategory = "shopping",
        interests = listOf(
            InterestItem("Beauty", "shopping"),
            InterestItem("Clothing", "shopping"),
            InterestItem("Fashion accessories", "shopping"),
            InterestItem("Fashion design", "shopping"),
            InterestItem("Online shopping", "shopping"),
            InterestItem("Shopping", "shopping"),
            InterestItem("Toys", "shopping")
        )
    ),
    InterestCategory(
        name = "Sports and outdoors",
        subcategory = "sports",
        interests = listOf(
            InterestItem("Cycling", "sports"),
            InterestItem("Fishing", "sports"),
            InterestItem("Football", "sports"),
            InterestItem("Hiking", "sports"),
            InterestItem("Outdoor recreation", "sports"),
            InterestItem("Soccer", "sports"),
            InterestItem("Swimming", "sports")
        )
    ),
    InterestCategory(
        name = "Technology (computers & electronics)",
        subcategory = "tech",
        interests = listOf(
            InterestItem("Computers", "tech"),
            InterestItem("Consumer electronics", "tech"),
            InterestItem("Mobile phones", "tech"),
            InterestItem("Software", "tech"),
            InterestItem("Technology", "tech")
        )
    )
)

private val allInterests: List<InterestItem> =
    interestCategories.flatMap { it.interests }

private val suggestedInterests = listOf(
    InterestItem("Garden (home & garden)", "suggested"),
    InterestItem("Garden design (gardening)", "suggested"),
    InterestItem("Interior design (design)", "suggested"),
    InterestItem("Home (home & garden)", "suggested"),
    InterestItem("Furniture (home furnishings)", "suggested")
)

@Composable
fun EditInterestsScreen(
    onNavigateBack: () -> Unit,
    onSave: (List<String>) -> Unit,
    initialInterests: List<String>
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    var selectedInterests by rememberSaveable {
        mutableStateOf(initialInterests.toSet())
    }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var openCategory by rememberSaveable { mutableStateOf<String?>(null) }

    val currentCategory = interestCategories.find { it.name == openCategory }

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            if (currentCategory != null) {
                WDSTopBar(
                    title = currentCategory.name,
                    onNavigateBack = { openCategory = null }
                )
            } else {
                WDSTopBar(
                    title = "Add interests",
                    onNavigateBack = onNavigateBack,
                    showCloseButton = true
                )
            }
        },
        bottomBar = {
            if (currentCategory == null) {
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
                        onClick = { onSave(selectedInterests.toList()) },
                        text = "Save",
                        variant = WDSButtonVariant.FILLED,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = currentCategory,
            label = "interest_nav",
            modifier = Modifier.padding(paddingValues)
        ) { category ->
            if (category != null) {
                CategoryDetailContent(
                    category = category,
                    selectedInterests = selectedInterests,
                    onToggle = { interest ->
                        selectedInterests = if (interest in selectedInterests)
                            selectedInterests - interest else selectedInterests + interest
                    }
                )
            } else {
                MainInterestsContent(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    selectedInterests = selectedInterests,
                    onToggle = { interest ->
                        selectedInterests = if (interest in selectedInterests)
                            selectedInterests - interest else selectedInterests + interest
                    },
                    onCategoryClick = { openCategory = it }
                )
            }
        }
    }
}

@Composable
private fun MainInterestsContent(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedInterests: Set<String>,
    onToggle: (String) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val focusRequester = remember { FocusRequester() }

    val filteredResults by remember(searchQuery) {
        derivedStateOf {
            if (searchQuery.isBlank()) emptyList()
            else (allInterests + suggestedInterests)
                .distinctBy { it.name }
                .filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    val isSearching = searchQuery.isNotBlank()
    val hasSelected = selectedInterests.isNotEmpty()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Search bar
        item(key = "search") {
            InterestsSearchBar(
                query = searchQuery,
                onQueryChange = onSearchChange,
                focusRequester = focusRequester
            )
        }

        if (isSearching) {
            // Search results
            items(filteredResults, key = { "search_${it.name}" }) { item ->
                InterestToggleRow(
                    name = item.name,
                    isSelected = item.name in selectedInterests,
                    onClick = { onToggle(item.name) }
                )
            }

            if (filteredResults.isEmpty()) {
                item(key = "no_results") {
                    Text(
                        text = "No interests found for \"$searchQuery\"",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized,
                        modifier = Modifier.padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingDouble
                        )
                    )
                }
            }
        } else if (hasSelected) {
            // Selected interests section
            item(key = "selected_header") {
                Text(
                    text = "Selected interests",
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    modifier = Modifier.padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSingle
                    )
                )
            }

            items(selectedInterests.toList(), key = { "sel_$it" }) { interest ->
                InterestToggleRow(
                    name = interest,
                    isSelected = true,
                    onClick = { onToggle(interest) }
                )
            }

            // Suggested for you
            item(key = "suggested_header") {
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))
                Text(
                    text = "Suggested for you",
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    modifier = Modifier.padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSingle
                    )
                )
            }

            val suggestions = suggestedInterests.filter { it.name !in selectedInterests }
            items(suggestions, key = { "sug_${it.name}" }) { item ->
                InterestToggleRow(
                    name = item.name,
                    isSelected = false,
                    onClick = { onToggle(item.name) }
                )
            }

            item(key = "browse_link") {
                Text(
                    text = "Browse interests",
                    style = typography.body1Emphasized,
                    color = colors.colorAccentEmphasized,
                    modifier = Modifier
                        .clickableWithSound { onSearchChange("") }
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSinglePlus
                        )
                )
            }
        } else {
            // Hint text
            item(key = "hint") {
                Text(
                    text = "We suggest adding a broad range of interests to cover the largest target audience.",
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    modifier = Modifier.padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSingle
                    )
                )
            }

            // Category list
            items(interestCategories, key = { it.name }) { category ->
                CategoryRow(
                    name = category.name,
                    onClick = { onCategoryClick(category.name) }
                )
            }
        }
    }
}

@Composable
private fun CategoryDetailContent(
    category: InterestCategory,
    selectedInterests: Set<String>,
    onToggle: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(category.interests, key = { it.name }) { item ->
            InterestToggleRow(
                name = item.name,
                isSelected = item.name in selectedInterests,
                onClick = { onToggle(item.name) }
            )
        }
    }
}

// ─── Shared Components ──────────────────────────────────────────────────────

@Composable
private fun InterestsSearchBar(
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
private fun CategoryRow(
    name: String,
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = typography.body1,
            color = colors.colorContentDefault,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = colors.colorContentDeemphasized
        )
    }
}

@Composable
private fun InterestToggleRow(
    name: String,
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
        Text(
            text = name,
            style = typography.body1,
            color = colors.colorContentDefault,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Selected",
                modifier = Modifier.size(24.dp),
                tint = colors.colorPositive
            )
        } else {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(
                        color = colors.colorDivider,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.Center)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(colors.colorSurfaceDefault)
                )
            }
        }
    }
}
