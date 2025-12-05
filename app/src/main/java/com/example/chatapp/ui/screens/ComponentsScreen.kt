package com.example.chatapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.wds.theme.WdsTheme
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonAction
import com.example.chatapp.wds.components.WDSButtonSize
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSChip
import com.example.chatapp.wds.components.WDSChipAction
import com.example.chatapp.wds.components.WDSChipSize
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.components.WDSFab
import com.example.chatapp.wds.components.WDSFabSize
import com.example.chatapp.wds.components.WDSFabStyle
import com.example.chatapp.wds.components.WDSMultiLineTextField
import com.example.chatapp.wds.components.WDSSingleLineTextField
import com.example.chatapp.wds.components.WdsCheckbox
import com.example.chatapp.wds.components.WdsDialog
import com.example.chatapp.wds.components.WdsDialogButton
import com.example.chatapp.wds.components.WdsRadioButton
import com.example.chatapp.wds.components.WdsSwitch
import com.example.chatapp.wds.components.WDSBottomSheet
import com.example.chatapp.wds.components.WDSBottomSheetItem
import com.example.chatapp.wds.components.WDSContextMenu
import com.example.chatapp.wds.components.WDSContextMenuItem
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentsScreen(
    onNavigateBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showContextMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Components",
                        color = WdsTheme.colors.colorContentDefault
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = WdsTheme.colors.colorContentDefault
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WdsTheme.colors.colorBackgroundWashInset
                )
            )
        },
        containerColor = WdsTheme.colors.colorBackgroundWashInset
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = WdsTheme.dimensions.wdsSpacingDouble)
        ) {
            // Buttons Section
            item {
                ComponentSection(title = "Buttons") {
                    ButtonsSection()
                }
            }
            
            // Chips Section
            item {
                ComponentSection(title = "Chips") {
                    ChipsSection()
                }
            }
            
            // Text Fields Section
            item {
                ComponentSection(title = "Text Fields") {
                    TextFieldsSection()
                }
            }
            
            // Selection Controls Section
            item {
                ComponentSection(title = "Selection Controls") {
                    SelectionControlsSection()
                }
            }
            
            // FAB Section
            item {
                ComponentSection(title = "Floating Action Buttons") {
                    FabSection()
                }
            }
            
            // Divider Section
            item {
                ComponentSection(title = "Dividers") {
                    DividerSection()
                }
            }
            
            // Dialog Section
            item {
                ComponentSection(title = "Dialogs") {
                    DialogSection(onShowDialog = { showDialog = true })
                }
            }
            
            // Bottom Sheet Section
            item {
                ComponentSection(title = "Bottom Sheets") {
                    BottomSheetSection(onShowBottomSheet = { showBottomSheet = true })
                }
            }
            
            // Context Menu Section
            item {
                ComponentSection(title = "Context Menus") {
                    ContextMenuSection(
                        showMenu = showContextMenu,
                        onShowMenu = { showContextMenu = true },
                        onDismissMenu = { showContextMenu = false }
                    )
                }
            }
        }
    }
    
    // Dialog
    if (showDialog) {
        WdsDialog(
            title = "Sample Dialog",
            message = "This is an example of the WDS Dialog component with all design system tokens applied.",
            icon = Icons.Default.Info,
            positiveButton = WdsDialogButton(
                text = "OK",
                onClick = { showDialog = false }
            ),
            negativeButton = WdsDialogButton(
                text = "Cancel",
                onClick = { showDialog = false }
            ),
            onDismissRequest = { showDialog = false }
        )
    }
    
    // Bottom Sheet
    if (showBottomSheet) {
        WDSBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            headline = "Announces a big update or new feature benefit",
            bodyText = "Only if needed for clarity, fill info gaps from the title or contextualize bullets.",
            items = listOf(
                WDSBottomSheetItem(
                    icon = Icons.Default.Check,
                    text = "Say the main, general idea in titles"
                ),
                WDSBottomSheetItem(
                    icon = Icons.Default.Info,
                    text = "Have similar structure across titles"
                ),
                WDSBottomSheetItem(
                    icon = Icons.Default.Edit,
                    text = "Use icons to reenforce ideas"
                )
            ),
            primaryButtonText = "Primary action",
            onPrimaryClick = { showBottomSheet = false },
            secondaryButtonText = "Secondary action",
            onSecondaryClick = { showBottomSheet = false }
        )
    }
}

@Composable
private fun ComponentSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = WdsTheme.dimensions.wdsSpacingTriple)
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = WdsTheme.colors.colorContentDeemphasized,
            modifier = Modifier.padding(
                horizontal = WdsTheme.dimensions.wdsSpacingDouble,
                vertical = WdsTheme.dimensions.wdsSpacingSingle
            )
        )
        HorizontalDivider(
            color = WdsTheme.colors.colorDivider,
            modifier = Modifier.padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble)
        )
        Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingDouble))
        content()
    }
}

@Composable
private fun SelectionControlsSection() {
    var checkboxState by remember { mutableStateOf(true) }
    var radioState by remember { mutableStateOf(0) }
    var switchState by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble),
        verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        // Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
        ) {
            WdsCheckbox(
                checked = checkboxState,
                onCheckedChange = { checkboxState = it }
            )
            Text(text = "Checkbox", color = WdsTheme.colors.colorContentDefault)
        }
        
        // Radio Buttons
        Column(verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
            ) {
                WdsRadioButton(
                    selected = radioState == 0,
                    onClick = { radioState = 0 }
                )
                Text(text = "Option 1", color = WdsTheme.colors.colorContentDefault)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
            ) {
                WdsRadioButton(
                    selected = radioState == 1,
                    onClick = { radioState = 1 }
                )
                Text(text = "Option 2", color = WdsTheme.colors.colorContentDefault)
            }
        }
        
        // Switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
        ) {
            WdsSwitch(
                checked = switchState,
                onCheckedChange = { switchState = it }
            )
            Text(text = "Switch", color = WdsTheme.colors.colorContentDefault)
        }
    }
}

@Composable
private fun ButtonsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble),
        verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        // Variants - Normal Action
        Text("Variants (Normal)", style = WdsTheme.typography.body3, color = WdsTheme.colors.colorContentDeemphasized)
        Row(
            horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalf),
            modifier = Modifier.fillMaxWidth()
        ) {
            WDSButton(
                onClick = {},
                text = "Filled",
                variant = WDSButtonVariant.FILLED,
                action = WDSButtonAction.NORMAL,
                size = WDSButtonSize.SMALL
            )
            WDSButton(
                onClick = {},
                text = "Tonal",
                variant = WDSButtonVariant.TONAL,
                action = WDSButtonAction.NORMAL,
                size = WDSButtonSize.SMALL
            )
            WDSButton(
                onClick = {},
                text = "Outline",
                variant = WDSButtonVariant.OUTLINE,
                action = WDSButtonAction.NORMAL,
                size = WDSButtonSize.SMALL
            )
        }
        
        // Actions - Filled Variant
        Text("Actions (Filled)", style = WdsTheme.typography.body3, color = WdsTheme.colors.colorContentDeemphasized)
        Row(
            horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalf),
            modifier = Modifier.fillMaxWidth()
        ) {
            WDSButton(
                onClick = {},
                text = "Normal",
                variant = WDSButtonVariant.FILLED,
                action = WDSButtonAction.NORMAL,
                size = WDSButtonSize.SMALL
            )
            WDSButton(
                onClick = {},
                text = "Destructive",
                variant = WDSButtonVariant.FILLED,
                action = WDSButtonAction.DESTRUCTIVE,
                size = WDSButtonSize.SMALL
            )
            WDSButton(
                onClick = {},
                text = "Media",
                variant = WDSButtonVariant.FILLED,
                action = WDSButtonAction.MEDIA,
                size = WDSButtonSize.SMALL
            )
        }
        
        // Sizes - Filled Normal
        Text("Sizes", style = WdsTheme.typography.body3, color = WdsTheme.colors.colorContentDeemphasized)
        Row(horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalf)) {
            WDSButton(
                onClick = {},
                text = "Small",
                size = WDSButtonSize.SMALL
            )
            WDSButton(
                onClick = {},
                text = "Normal",
                size = WDSButtonSize.NORMAL
            )
            WDSButton(
                onClick = {},
                text = "Large",
                size = WDSButtonSize.LARGE
            )
        }
        
        // With Icons
        Text("With Icons", style = WdsTheme.typography.body3, color = WdsTheme.colors.colorContentDeemphasized)
        Row(horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalf)) {
            WDSButton(
                onClick = {},
                text = "Add",
                icon = Icons.Default.Add,
                variant = WDSButtonVariant.FILLED
            )
            WDSButton(
                onClick = {},
                text = "Check",
                icon = Icons.Default.Check,
                variant = WDSButtonVariant.TONAL
            )
            WDSButton(
                onClick = {},
                icon = Icons.Default.Favorite,
                variant = WDSButtonVariant.OUTLINE
            )
        }
    }
}

@Composable
private fun ChipsSection() {
    var selectedDefaultChip by remember { mutableStateOf(0) }
    var selectedInputChip by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble),
        verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        // Default Action Chips
        Text("Default Action", style = WdsTheme.typography.body3, color = WdsTheme.colors.colorContentDeemphasized)
        Row(
            horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalf),
            modifier = Modifier.fillMaxWidth()
        ) {
            WDSChip(
                text = "All",
                selected = selectedDefaultChip == 0,
                onClick = { selectedDefaultChip = 0 },
                action = WDSChipAction.DEFAULT
            )
            WDSChip(
                text = "Photos",
                selected = selectedDefaultChip == 1,
                onClick = { selectedDefaultChip = 1 },
                action = WDSChipAction.DEFAULT,
                icon = Icons.Default.Favorite
            )
            WDSChip(
                text = "Videos",
                selected = selectedDefaultChip == 2,
                onClick = { selectedDefaultChip = 2 },
                action = WDSChipAction.DEFAULT,
                badgeText = "12"
            )
        }

        // Input Action Chips
        Text("Input Action", style = WdsTheme.typography.body3, color = WdsTheme.colors.colorContentDeemphasized)
        Row(
            horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalf),
            modifier = Modifier.fillMaxWidth()
        ) {
            WDSChip(
                text = "Tag 1",
                selected = selectedInputChip == 0,
                onClick = { selectedInputChip = 0 },
                action = WDSChipAction.INPUT
            )
            WDSChip(
                text = "Tag 2",
                selected = selectedInputChip == 1,
                onClick = { selectedInputChip = 1 },
                action = WDSChipAction.INPUT
            )
        }

        // Close and Dropdown Actions
        Text("Close & Dropdown Actions", style = WdsTheme.typography.body3, color = WdsTheme.colors.colorContentDeemphasized)
        Row(
            horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalf),
            modifier = Modifier.fillMaxWidth()
        ) {
            WDSChip(
                text = "Close",
                selected = false,
                onClick = {},
                action = WDSChipAction.CLOSE
            )
            WDSChip(
                text = "Dropdown",
                selected = false,
                onClick = {},
                action = WDSChipAction.DROPDOWN
            )
        }

        // Large Size
        Text("Large Size", style = WdsTheme.typography.body3, color = WdsTheme.colors.colorContentDeemphasized)
        Row(
            horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalf),
            modifier = Modifier.fillMaxWidth()
        ) {
            WDSChip(
                text = "Large Chip",
                selected = false,
                onClick = {},
                size = WDSChipSize.LARGE
            )
            WDSChip(
                text = "Selected",
                selected = true,
                onClick = {},
                size = WDSChipSize.LARGE,
                icon = Icons.Default.Check
            )
        }
    }
}

@Composable
private fun TextFieldsSection() {
    var singleLineText by remember { mutableStateOf("") }
    var multiLineText by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble),
        verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
    ) {
        WDSSingleLineTextField(
            value = singleLineText,
            onValueChange = { singleLineText = it },
            label = "Single Line",
            placeholder = "Enter text...",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        )
        
        WDSMultiLineTextField(
            value = multiLineText,
            onValueChange = { multiLineText = it },
            label = "Multi Line",
            placeholder = "Enter multiple lines...",
            minLines = 3,
            maxLines = 5
        )
    }
}

@Composable
private fun FabSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble),
        horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        WDSFab(
            onClick = {},
            icon = Icons.Default.Add,
            size = WDSFabSize.SMALL,
            contentDescription = "Add"
        )
        WDSFab(
            onClick = {},
            icon = Icons.Default.Edit,
            size = WDSFabSize.NORMAL,
            contentDescription = "Edit"
        )
        WDSFab(
            onClick = {},
            icon = Icons.Default.Add,
            size = WDSFabSize.LARGE,
            style = WDSFabStyle.PRIMARY,
            contentDescription = "Add"
        )
        WDSFab(
            onClick = {},
            icon = Icons.Default.Favorite,
            size = WDSFabSize.NORMAL,
            style = WDSFabStyle.SECONDARY,
            contentDescription = "Favorite"
        )
    }
}

@Composable
private fun DividerSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble),
        verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        Text("Default Divider", color = WdsTheme.colors.colorContentDefault)
        WDSDivider()
        
        Text("With Insets", color = WdsTheme.colors.colorContentDefault)
        WDSDivider(
            startIndent = WdsTheme.dimensions.wdsSpacingDouble,
            endIndent = WdsTheme.dimensions.wdsSpacingDouble
        )
        
        Text("Thick Divider", color = WdsTheme.colors.colorContentDefault)
        WDSDivider(thickness = 2.dp)
    }
}

@Composable
private fun DialogSection(onShowDialog: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        WDSButton(
            onClick = onShowDialog,
            text = "Show Dialog",
            modifier = Modifier.fillMaxWidth(),
            variant = WDSButtonVariant.FILLED
        )
    }
}

@Composable
private fun BottomSheetSection(onShowBottomSheet: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        WDSButton(
            onClick = onShowBottomSheet,
            text = "Show Bottom Sheet",
            modifier = Modifier.fillMaxWidth(),
            variant = WDSButtonVariant.FILLED
        )
    }
}

@Composable
private fun ContextMenuSection(
    showMenu: Boolean,
    onShowMenu: () -> Unit,
    onDismissMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        WDSButton(
            onClick = onShowMenu,
            text = "Show Context Menu",
            modifier = Modifier.fillMaxWidth(),
            variant = WDSButtonVariant.FILLED
        )
        
        WDSContextMenu(
            expanded = showMenu,
            onDismissRequest = onDismissMenu,
            offset = androidx.compose.ui.unit.IntOffset(
                x = 0,
                y = 150
            ),
            items = listOf(
                WDSContextMenuItem(
                    icon = androidx.compose.material.icons.Icons.Outlined.Palette,
                    text = "Design Library",
                    onClick = { }
                ),
                WDSContextMenuItem(
                    icon = androidx.compose.material.icons.Icons.Outlined.Settings,
                    text = "Settings",
                    onClick = { }
                ),
                WDSContextMenuItem(
                    icon = androidx.compose.material.icons.Icons.Outlined.Share,
                    text = "Share",
                    onClick = { }
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComponentsScreenPreview() {
    WdsTheme {
        ComponentsScreen(onNavigateBack = {})
    }
}
