package com.example.chatapp.wds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.wds.theme.WdsTheme
import com.example.chatapp.wds.theme.WhatsAppIconsFont

/**
 * Test screen to find the correct icon characters in WhatsAppIconsNew font
 *
 * Display a grid of characters from the Private Use Area (U+E000 to U+F8FF)
 * where icon fonts typically store their glyphs
 */
@Composable
fun IconFontTesterScreen() {
    val colors = WdsTheme.colors

    // Test range: Private Use Area where icon fonts typically store glyphs
    val startChar = 0xE000
    val endChar = 0xE100 // Test first 256 characters
    val chars = (startChar..endChar).map { it.toChar() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.colorSurfaceDefault)
            .padding(16.dp)
    ) {
        Text(
            text = "WhatsApp Icon Font Tester",
            style = WdsTheme.typography.headline1,
            color = colors.colorContentDefault,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Testing U+E000 to U+E0FF range",
            style = WdsTheme.typography.body2,
            color = colors.colorContentDeemphasized,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(8),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chars) { char ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(colors.colorSurfaceElevatedDefault)
                        .padding(8.dp)
                ) {
                    // Icon from font
                    Text(
                        text = char.toString(),
                        fontFamily = WhatsAppIconsFont,
                        fontSize = 24.sp,
                        color = colors.colorContentDefault
                    )

                    // Unicode value
                    Text(
                        text = "U+${char.code.toString(16).uppercase().padStart(4, '0')}",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Normal,
                        color = colors.colorContentDeemphasized
                    )
                }
            }
        }
    }
}
