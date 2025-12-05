package com.example.chatapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconsScreen(
    onNavigateBack: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Icons",
                        color = colors.colorContentDefault
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.colorContentDefault
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.colorBackgroundWashInset
                )
            )
        },
        containerColor = colors.colorBackgroundWashInset
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensions.wdsSpacingTriple)
                .padding(vertical = dimensions.wdsSpacingDouble),
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingDouble)
        ) {
            Text(
                text = "This project does have access to the Material Icon library. This includes Material Icons Extended, which gives you access to:",
                style = typography.body1,
                color = colors.colorContentDefault
            )

            Column(
                modifier = Modifier.padding(start = dimensions.wdsSpacingDouble),
                verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
            ) {
                Text(
                    text = "1. ~2000 Material icons (the extended set)",
                    style = typography.body1,
                    color = colors.colorContentDefault
                )
                
                Text(
                    text = "2. All filled and outlined variants",
                    style = typography.body1,
                    color = colors.colorContentDefault
                )
            }

            Text(
                text = "Use by referencing the icon name from fonts.google.com/icons and the LLM will add them to your screens.",
                style = typography.body1,
                color = colors.colorContentDefault
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun IconsScreenPreviewLight() {
    WdsTheme(darkTheme = false) {
        IconsScreen(onNavigateBack = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun IconsScreenPreviewDark() {
    WdsTheme(darkTheme = true) {
        IconsScreen(onNavigateBack = {})
    }
}
