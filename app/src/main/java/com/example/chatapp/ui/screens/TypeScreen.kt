package com.example.chatapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.wds.theme.WdsTheme
import com.example.chatapp.wds.tokens.WdsTypography

private data class TypeStyle(
    val name: String,
    val description: String,
    val style: TextStyle
)

// Constants
private val DESCRIPTION_TEXT_STYLE = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal
)

private fun getTypeStyles(typography: WdsTypography): List<TypeStyle> = listOf(
    TypeStyle(
        name = "largeTitle1",
        description = "32sp • Hero/primary navigation title",
        style = typography.largeTitle1
    ),
    TypeStyle(
        name = "largeTitle2",
        description = "28sp • Full page headlines",
        style = typography.largeTitle2
    ),
    TypeStyle(
        name = "headline1",
        description = "24sp • Title bar actions",
        style = typography.headline1
    ),
    TypeStyle(
        name = "headline2",
        description = "22sp • Title bar headlines, dialog headlines",
        style = typography.headline2
    ),
    TypeStyle(
        name = "body1",
        description = "16sp • Bottom sheet header actions",
        style = typography.body1
    ),
    TypeStyle(
        name = "body1Emphasized",
        description = "16sp Medium • Bottom sheet headlines",
        style = typography.body1Emphasized
    ),
    TypeStyle(
        name = "body2",
        description = "14sp • Footnotes, metadata",
        style = typography.body2
    ),
    TypeStyle(
        name = "body2Emphasized",
        description = "14sp Medium • Footnote actions, tappable metadata",
        style = typography.body2Emphasized
    ),
    TypeStyle(
        name = "body3",
        description = "12sp • Body text",
        style = typography.body3
    ),
    TypeStyle(
        name = "body3Emphasized",
        description = "12sp Medium • Body text actions",
        style = typography.body3Emphasized
    ),
    TypeStyle(
        name = "chatBody1",
        description = "16sp • Chat messages",
        style = typography.chatBody1
    ),
    TypeStyle(
        name = "chatBody1Emphasized",
        description = "16sp Medium • Chat messages emphasized",
        style = typography.chatBody1Emphasized
    ),
    TypeStyle(
        name = "chatBody2",
        description = "14sp • Chat metadata",
        style = typography.chatBody2
    ),
    TypeStyle(
        name = "chatBody2Emphasized",
        description = "14sp Medium • Chat metadata emphasized",
        style = typography.chatBody2Emphasized
    ),
    TypeStyle(
        name = "chatBody3",
        description = "12sp • Chat timestamps",
        style = typography.chatBody3
    ),
    TypeStyle(
        name = "chatBody3Emphasized",
        description = "12sp Medium • Chat timestamps emphasized",
        style = typography.chatBody3Emphasized
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeScreen(
    onNavigateBack: () -> Unit
) {
    val typography = WdsTheme.typography
    val typeStyles = remember(typography) {
        getTypeStyles(typography)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Typography",
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
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            itemsIndexed(
                items = typeStyles,
                key = { _, typeStyle -> typeStyle.name }
            ) { index, typeStyle ->
                TypeStyleItem(typeStyle = typeStyle)
                
                if (index < typeStyles.size - 1) {
                    HorizontalDivider(
                        color = WdsTheme.colors.colorDivider,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TypeStyleItem(
    typeStyle: TypeStyle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Style name - displayed in its own style
        Text(
            text = typeStyle.name,
            style = typeStyle.style,
            color = WdsTheme.colors.colorContentDefault,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Description - in Roboto Mono
        Text(
            text = typeStyle.description,
            style = DESCRIPTION_TEXT_STYLE,
            color = WdsTheme.colors.colorContentDeemphasized
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TypeScreenPreview() {
    WdsTheme {
        TypeScreen(onNavigateBack = {})
    }
}
