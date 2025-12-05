package com.example.chatapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemLibraryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToColors: () -> Unit,
    onNavigateToType: () -> Unit,
    onNavigateToComponents: () -> Unit,
    onNavigateToIcons: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Design System Library",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DesignSystemListItem(
                title = "Colors",
                onClick = onNavigateToColors
            )
            
            HorizontalDivider(
                color = WdsTheme.colors.colorDivider,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            DesignSystemListItem(
                title = "Type",
                onClick = onNavigateToType
            )
            
            HorizontalDivider(
                color = WdsTheme.colors.colorDivider,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            DesignSystemListItem(
                title = "Components",
                onClick = onNavigateToComponents
            )
            
            HorizontalDivider(
                color = WdsTheme.colors.colorDivider,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            DesignSystemListItem(
                title = "Icons",
                onClick = onNavigateToIcons
            )
        }
    }
}

@Composable
private fun DesignSystemListItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = WdsTheme.colors.colorContentDefault
        )
        
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = WdsTheme.colors.colorContentDeemphasized
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun DesignSystemLibraryScreenPreviewLight() {
    WdsTheme(darkTheme = false) {
        DesignSystemLibraryScreen(
            onNavigateBack = {},
            onNavigateToColors = {},
            onNavigateToType = {},
            onNavigateToComponents = {},
            onNavigateToIcons = {}
        )
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun DesignSystemLibraryScreenPreviewDark() {
    WdsTheme(darkTheme = true) {
        DesignSystemLibraryScreen(
            onNavigateBack = {},
            onNavigateToColors = {},
            onNavigateToType = {},
            onNavigateToComponents = {},
            onNavigateToIcons = {}
        )
    }
}
