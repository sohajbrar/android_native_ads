@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.broadcast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.chatapp.R
import com.example.chatapp.data.local.entity.UserEntity
import com.example.chatapp.features.newchat.NewChatViewModel
import com.example.chatapp.wds.components.WDSFab
import com.example.chatapp.wds.components.WDSFabStyle
import com.example.chatapp.wds.components.WDSSectionDivider
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme

data class ContactList(
    val id: String,
    val name: String,
    val recipientCount: Int,
    val dotColor: Color? = null,
    val dotStrokeColor: Color? = null,
    val dotStrokeAlpha: Float = 1f,
    val icon: ImageVector? = null,
    val iconTint: Color? = null
)

@Composable
fun SelectContactScreen(
    onNavigateBack: () -> Unit = {},
    onNextClick: (title: String, recipientCount: Int, linkedListCount: Int) -> Unit = { _, _, _ -> },
    viewModel: NewChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val colors = WdsTheme.colors
    val lists = remember(colors) {
        listOf(
            ContactList(
                id = "favorites",
                name = "Favorites",
                recipientCount = 124,
                icon = Icons.Filled.Favorite,
                iconTint = colors.colorContentDisabled
            ),
            ContactList(
                id = "new_order",
                name = "New order",
                recipientCount = 303,
                dotColor = Color(0xFFFFD429),
                dotStrokeColor = Color.Black,
                dotStrokeAlpha = 0.22f
            ),
            ContactList(
                id = "lead",
                name = "Lead",
                recipientCount = 124,
                dotColor = Color(0xFF9BA6FF),
                dotStrokeColor = Color.Black,
                dotStrokeAlpha = 0.22f
            )
        )
    }

    var selectedListIds by remember { mutableStateOf(setOf<String>()) }
    var selectedContactIds by remember { mutableStateOf(setOf<String>()) }

    val selectedLists = lists.filter { it.id in selectedListIds }
    val selectedContacts = uiState.displayContacts.filter { it.userId in selectedContactIds }

    val selectedCount = selectedLists.sumOf { it.recipientCount } + selectedContactIds.size
    val totalCount = 2000

    val showFab = selectedListIds.isNotEmpty() || selectedContactIds.size >= 2
    val hasSelections = selectedListIds.isNotEmpty() || selectedContactIds.isNotEmpty()

    SelectContactContent(
        contacts = uiState.displayContacts,
        lists = lists,
        selectedListIds = selectedListIds,
        selectedContactIds = selectedContactIds,
        selectedLists = selectedLists,
        selectedContacts = selectedContacts,
        selectedCount = selectedCount,
        totalCount = totalCount,
        showFab = showFab,
        hasSelections = hasSelections,
        onToggleList = { listId ->
            selectedListIds = if (listId in selectedListIds) {
                selectedListIds - listId
            } else {
                selectedListIds + listId
            }
        },
        onToggleContact = { contactId ->
            selectedContactIds = if (contactId in selectedContactIds) {
                selectedContactIds - contactId
            } else {
                selectedContactIds + contactId
            }
        },
        onNavigateBack = onNavigateBack,
        onNextClick = {
            val title = when {
                selectedLists.size == 1 && selectedContactIds.isEmpty() -> selectedLists.first().name
                selectedLists.isNotEmpty() && selectedContactIds.isEmpty() ->
                    selectedLists.joinToString(", ") { it.name }
                selectedContactIds.isNotEmpty() && selectedListIds.isEmpty() ->
                    selectedContacts.joinToString(", ") { it.displayName }
                else -> {
                    val parts = mutableListOf<String>()
                    parts.addAll(selectedLists.map { it.name })
                    parts.addAll(selectedContacts.map { it.displayName })
                    parts.joinToString(", ")
                }
            }
            onNextClick(title, selectedCount, selectedListIds.size)
        }
    )
}

@Composable
private fun SelectContactContent(
    contacts: List<UserEntity>,
    lists: List<ContactList>,
    selectedListIds: Set<String>,
    selectedContactIds: Set<String>,
    selectedLists: List<ContactList>,
    selectedContacts: List<UserEntity>,
    selectedCount: Int,
    totalCount: Int,
    showFab: Boolean,
    hasSelections: Boolean,
    onToggleList: (String) -> Unit,
    onToggleContact: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNextClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val formattedSelected = String.format("%,d", selectedCount)
    val formattedTotal = String.format("%,d", totalCount)

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Select contact",
                subtitle = "$formattedSelected of $formattedTotal selected",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = colors.colorContentDefault
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                WDSFab(
                    onClick = onNextClick,
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    style = WDSFabStyle.PRIMARY,
                    contentDescription = "Next"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Selected items row pinned above the scrollable list
            AnimatedVisibility(visible = hasSelections) {
                Column {
                    SelectedItemsRow(
                        selectedLists = selectedLists,
                        selectedContacts = selectedContacts,
                        allContacts = contacts,
                        onRemoveList = { onToggleList(it) },
                        onRemoveContact = { onToggleContact(it) }
                    )
                    HorizontalDivider(color = colors.colorDivider)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Your lists header
                item(key = "lists_header") {
                    WDSSectionDivider(
                        title = "Your lists",
                        actionText = "See all",
                        onActionClick = { }
                    )
                }

                // List items
                items(
                    items = lists,
                    key = { "list_${it.id}" }
                ) { list ->
                    SelectableListRow(
                        list = list,
                        isSelected = list.id in selectedListIds,
                        onToggle = { onToggleList(list.id) }
                    )
                }

                // Contacts on WhatsApp header
                item(key = "contacts_header") {
                    WDSSectionDivider(title = "Contacts on WhatsApp")
                }

            // Contact items
            items(
                items = contacts,
                key = { "contact_${it.userId}" }
            ) { user ->
                val isInSelectedList = selectedListIds.isNotEmpty()
                val isIndividuallySelected = user.userId in selectedContactIds
                SelectableContactRow(
                    user = user,
                    isSelected = isIndividuallySelected || isInSelectedList,
                    isSelectedViaList = isInSelectedList && !isIndividuallySelected,
                    onToggle = { onToggleContact(user.userId) }
                )
            }
            }
        }
    }
}

// --- Selected Items Row ---

@Composable
private fun SelectedItemsRow(
    selectedLists: List<ContactList>,
    selectedContacts: List<UserEntity>,
    allContacts: List<UserEntity>,
    onRemoveList: (String) -> Unit,
    onRemoveContact: (String) -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions

    val listMemberContacts = if (selectedLists.isNotEmpty()) allContacts else emptyList()

    val contactsToShow = if (selectedLists.isNotEmpty() && selectedContacts.isEmpty()) {
        listMemberContacts
    } else if (selectedLists.isEmpty()) {
        selectedContacts
    } else {
        val selectedIds = selectedContacts.map { it.userId }.toSet()
        listMemberContacts + selectedContacts.filter { it.userId !in allContacts.map { c -> c.userId }.toSet() }
    }

    val showDivider = selectedLists.isNotEmpty() && contactsToShow.isNotEmpty()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensions.wdsSpacingSingle),
        contentPadding = PaddingValues(horizontal = dimensions.wdsSpacingDouble),
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSinglePlus)
    ) {
        items(
            items = selectedLists,
            key = { "sel_list_${it.id}" }
        ) { list ->
            SelectedListChip(
                list = list,
                onRemove = { onRemoveList(list.id) }
            )
        }

        if (showDivider) {
            item(key = "sel_divider") {
                Box(
                    modifier = Modifier
                        .padding(horizontal = dimensions.wdsSpacingHalf)
                        .width(1.dp)
                        .height(60.dp)
                        .background(colors.colorDivider)
                )
            }
        }

        items(
            items = contactsToShow,
            key = { "sel_contact_${it.userId}" }
        ) { contact ->
            SelectedContactChip(
                contact = contact,
                showRemoveIcon = selectedLists.isEmpty(),
                onRemove = { onRemoveContact(contact.userId) }
            )
        }
    }
}

@Composable
private fun SelectedListChip(
    list: ContactList,
    onRemove: () -> Unit
) {
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography
    val chipSize = 56.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(64.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Surface(
                shape = CircleShape,
                color = colors.colorSurfaceEmphasized,
                modifier = Modifier.size(chipSize)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    when {
                        list.icon != null -> {
                            Icon(
                                imageVector = list.icon,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = list.iconTint ?: colors.colorContentDefault
                            )
                        }
                        list.dotColor != null -> {
                            val dotModifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(list.dotColor, CircleShape)
                            if (list.dotStrokeColor != null) {
                                Box(
                                    modifier = dotModifier.border(
                                        0.5.dp,
                                        list.dotStrokeColor.copy(alpha = list.dotStrokeAlpha),
                                        CircleShape
                                    )
                                )
                            } else {
                                Box(modifier = dotModifier)
                            }
                        }
                    }
                }
            }

            Surface(
                shape = CircleShape,
                color = colors.colorContentDisabled,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
                    .border(1.5.dp, colors.colorSurfaceDefault, CircleShape)
                    .clickable { onRemove() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remove ${list.name}",
                        modifier = Modifier.size(12.dp),
                        tint = colors.colorAlwaysWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = list.name,
            style = typography.body3,
            color = colors.colorContentDeemphasized,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SelectedContactChip(
    contact: UserEntity,
    showRemoveIcon: Boolean = true,
    onRemove: () -> Unit = {}
) {
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography
    val chipSize = 56.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(64.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            ContactAvatarSmall(
                avatarUrl = contact.avatarUrl,
                displayName = contact.displayName,
                size = chipSize
            )

            if (showRemoveIcon) {
                Surface(
                    shape = CircleShape,
                    color = colors.colorContentDisabled,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomEnd)
                        .border(1.5.dp, colors.colorSurfaceDefault, CircleShape)
                        .clickable { onRemove() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Remove ${contact.displayName}",
                            modifier = Modifier.size(12.dp),
                            tint = colors.colorAlwaysWhite
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = contact.displayName.split(" ").first(),
            style = typography.body3,
            color = colors.colorContentDeemphasized,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

// --- Your Lists Section ---

@Composable
private fun SelectableListRow(
    list: ContactList,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Surface(
                shape = CircleShape,
                color = colors.colorSurfaceEmphasized,
                modifier = Modifier.size(dimensions.wdsAvatarMediumLarge)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    when {
                        list.icon != null -> {
                            Icon(
                                imageVector = list.icon,
                                contentDescription = null,
                                modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                                tint = list.iconTint ?: colors.colorContentDefault
                            )
                        }
                        list.dotColor != null -> {
                            val dotModifier = Modifier
                                .size(dimensions.wdsIconSizeSmall)
                                .clip(CircleShape)
                                .background(list.dotColor, CircleShape)
                            if (list.dotStrokeColor != null) {
                                Box(
                                    modifier = dotModifier.border(
                                        0.5.dp,
                                        list.dotStrokeColor.copy(alpha = list.dotStrokeAlpha),
                                        CircleShape
                                    )
                                )
                            } else {
                                Box(modifier = dotModifier)
                            }
                        }
                    }
                }
            }

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = colors.colorAccent,
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.BottomEnd)
                        .border(2.dp, colors.colorSurfaceDefault, CircleShape)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Selected",
                            modifier = Modifier.size(14.dp),
                            tint = colors.colorAlwaysWhite
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
        ) {
            Text(
                text = list.name,
                style = typography.body1,
                color = colors.colorContentDefault,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${list.recipientCount} recipients",
                style = typography.body2,
                color = colors.colorContentDeemphasized,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// --- Contacts Section ---

@Composable
private fun SelectableContactRow(
    user: UserEntity,
    isSelected: Boolean,
    isSelectedViaList: Boolean = false,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectableContactAvatar(
            avatarUrl = user.avatarUrl,
            displayName = user.displayName,
            isSelected = isSelected,
            isSelectedViaList = isSelectedViaList
        )

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingQuarter)
        ) {
            Text(
                text = user.displayName,
                style = typography.body1,
                color = colors.colorContentDefault,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            user.statusMessage?.let { status ->
                Text(
                    text = status,
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SelectableContactAvatar(
    avatarUrl: String?,
    displayName: String,
    isSelected: Boolean,
    isSelectedViaList: Boolean = false,
    modifier: Modifier = Modifier
) {
    val dimensions = WdsTheme.dimensions
    val colors = WdsTheme.colors

    val badgeColor = if (isSelectedViaList) {
        colors.colorContentDisabled
    } else {
        colors.colorContentDefault
    }

    Box(modifier = modifier) {
        ContactAvatar(
            avatarUrl = avatarUrl,
            displayName = displayName,
            size = dimensions.wdsAvatarMediumLarge
        )

        if (isSelected) {
            Surface(
                shape = CircleShape,
                color = badgeColor,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
                    .border(1.5.dp, colors.colorSurfaceDefault, CircleShape)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected",
                        modifier = Modifier.size(12.dp),
                        tint = colors.colorAlwaysWhite
                    )
                }
            }
        }
    }
}

// --- Avatar Composables ---

@Composable
private fun ContactAvatar(
    avatarUrl: String?,
    displayName: String,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    var imageLoadFailed by remember { mutableStateOf(false) }

    if (!avatarUrl.isNullOrEmpty() && !imageLoadFailed) {
        when {
            avatarUrl.startsWith("drawable://") -> {
                val drawableResId = resolveDrawableAvatar(avatarUrl.removePrefix("drawable://"))
                if (drawableResId != null) {
                    Image(
                        painter = painterResource(id = drawableResId),
                        contentDescription = "Profile picture of $displayName",
                        modifier = modifier
                            .size(size)
                            .clip(CircleShape)
                            .background(colors.colorSurfaceEmphasized, CircleShape),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    InitialsAvatar(displayName = displayName, size = size, modifier = modifier)
                }
            }
            else -> {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(avatarUrl)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "Profile picture of $displayName",
                    modifier = modifier
                        .size(size)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    onError = { imageLoadFailed = true }
                )
            }
        }
    } else {
        InitialsAvatar(displayName = displayName, size = size, modifier = modifier)
    }
}

@Composable
private fun ContactAvatarSmall(
    avatarUrl: String?,
    displayName: String,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    ContactAvatar(
        avatarUrl = avatarUrl,
        displayName = displayName,
        size = size,
        modifier = modifier
    )
}

@Composable
private fun InitialsAvatar(
    displayName: String,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography

    Surface(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        color = colors.colorSurfaceEmphasized
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = displayName.take(1).uppercase(),
                style = typography.body1Emphasized,
                color = colors.colorContentDefault
            )
        }
    }
}

private fun resolveDrawableAvatar(name: String): Int? {
    return when (name) {
        "avatar_contact_placeholder" -> R.drawable.avatar_contact_placeholder
        "avatar_faith" -> R.drawable.avatar_faith
        "avatar_maria" -> R.drawable.avatar_maria
        "avatar_anika" -> R.drawable.avatar_anika
        "avatar_gerald" -> R.drawable.avatar_gerald
        "avatar_yuna" -> R.drawable.avatar_yuna
        "avatar_alice" -> R.drawable.avatar_alice
        "avatar_anna_soe" -> R.drawable.avatar_anna_soe
        "avatar_anika_chavan" -> R.drawable.avatar_anika_chavan
        "avatar_ayesha" -> R.drawable.avatar_ayesha
        "avatar_jordan" -> R.drawable.avatar_jordan
        else -> null
    }
}

// --- Previews ---

@Preview(showBackground = true, name = "Select Contact - No Selection")
@Composable
private fun SelectContactPreviewNoSelection() {
    WdsTheme(darkTheme = false) {
        val sampleContacts = listOf(
            UserEntity(userId = "u1", username = "alice", displayName = "Alice", avatarUrl = "drawable://avatar_alice", statusMessage = "Available"),
            UserEntity(userId = "u2", username = "anna_soe", displayName = "Anna Soe", avatarUrl = "drawable://avatar_anna_soe", statusMessage = "Busy"),
            UserEntity(userId = "u3", username = "anika", displayName = "Anika Chavan", avatarUrl = "drawable://avatar_anika_chavan", statusMessage = "Available"),
            UserEntity(userId = "u4", username = "ayesha", displayName = "Ayesha", avatarUrl = "drawable://avatar_ayesha", statusMessage = "Busy"),
            UserEntity(userId = "u5", username = "jordan", displayName = "Jordan", avatarUrl = "drawable://avatar_jordan", statusMessage = "Available")
        )

        val lists = sampleLists()

        SelectContactContent(
            contacts = sampleContacts,
            lists = lists,
            selectedListIds = emptySet(),
            selectedContactIds = emptySet(),
            selectedLists = emptyList(),
            selectedContacts = emptyList(),
            selectedCount = 0,
            totalCount = 2000,
            showFab = false,
            hasSelections = false,
            onToggleList = {},
            onToggleContact = {},
            onNavigateBack = {},
            onNextClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Select Contact - With Selections")
@Composable
private fun SelectContactPreviewWithSelections() {
    WdsTheme(darkTheme = false) {
        val sampleContacts = listOf(
            UserEntity(userId = "u1", username = "alice", displayName = "Alice", avatarUrl = "drawable://avatar_alice", statusMessage = "Available"),
            UserEntity(userId = "u2", username = "anna_soe", displayName = "Anna Soe", avatarUrl = "drawable://avatar_anna_soe", statusMessage = "Busy"),
            UserEntity(userId = "u3", username = "anika", displayName = "Anika Chavan", avatarUrl = "drawable://avatar_anika_chavan", statusMessage = "Available"),
            UserEntity(userId = "u4", username = "ayesha", displayName = "Ayesha", avatarUrl = "drawable://avatar_ayesha", statusMessage = "Busy"),
            UserEntity(userId = "u5", username = "jordan", displayName = "Jordan", avatarUrl = "drawable://avatar_jordan", statusMessage = "Available")
        )

        val lists = sampleLists()
        val selectedListIds = setOf("new_order")
        val selectedContactIds = emptySet<String>()

        SelectContactContent(
            contacts = sampleContacts,
            lists = lists,
            selectedListIds = selectedListIds,
            selectedContactIds = selectedContactIds,
            selectedLists = lists.filter { it.id in selectedListIds },
            selectedContacts = emptyList(),
            selectedCount = 303,
            totalCount = 2000,
            showFab = true,
            hasSelections = true,
            onToggleList = {},
            onToggleContact = {},
            onNavigateBack = {},
            onNextClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Select Contact - Dark")
@Composable
private fun SelectContactPreviewDark() {
    WdsTheme(darkTheme = true) {
        val sampleContacts = listOf(
            UserEntity(userId = "u1", username = "alice", displayName = "Alice", avatarUrl = "drawable://avatar_alice", statusMessage = "Available"),
            UserEntity(userId = "u2", username = "anna_soe", displayName = "Anna Soe", avatarUrl = "drawable://avatar_anna_soe", statusMessage = "Busy"),
            UserEntity(userId = "u3", username = "anika", displayName = "Anika Chavan", avatarUrl = "drawable://avatar_anika_chavan", statusMessage = "Available")
        )

        val lists = sampleLists()

        SelectContactContent(
            contacts = sampleContacts,
            lists = lists,
            selectedListIds = emptySet(),
            selectedContactIds = emptySet(),
            selectedLists = emptyList(),
            selectedContacts = emptyList(),
            selectedCount = 0,
            totalCount = 2000,
            showFab = false,
            hasSelections = false,
            onToggleList = {},
            onToggleContact = {},
            onNavigateBack = {},
            onNextClick = {}
        )
    }
}

private fun sampleLists() = listOf(
    ContactList(
        id = "favorites",
        name = "Favorites",
        recipientCount = 124,
        icon = Icons.Filled.Favorite,
        iconTint = Color(0xFFB3B9BD)
    ),
    ContactList(
        id = "new_order",
        name = "New order",
        recipientCount = 303,
        dotColor = Color(0xFFFFD429),
        dotStrokeColor = Color.Black,
        dotStrokeAlpha = 0.22f
    ),
    ContactList(
        id = "lead",
        name = "Lead",
        recipientCount = 124,
        dotColor = Color(0xFF9BA6FF),
        dotStrokeColor = Color.Black,
        dotStrokeAlpha = 0.22f
    )
)
