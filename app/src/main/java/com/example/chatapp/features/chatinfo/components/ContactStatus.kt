package com.example.chatapp.features.chatinfo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun ContactStatus(
    statusMessage: String? = null,
    phoneNumber: String? = null,
    joinedDate: Long? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingSingle, vertical = WdsTheme.dimensions.wdsSpacingHalf),
        colors = CardDefaults.cardColors(containerColor = WdsTheme.colors.colorSurfaceDefault),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WdsTheme.dimensions.wdsSpacingDouble),
            verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingQuarter)
        ) {
            // Status message or default text
            Text(
                text = statusMessage ?: "Can't talk, WhatsApp only",
                style = WdsTheme.typography.body1,
                color = WdsTheme.colors.colorContentDefault
            )

            // Join date
            val dateText = if (joinedDate != null) {
                val date = Instant.ofEpochMilli(joinedDate)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
                date.format(formatter)
            } else {
                "8 March 2024" // Default for demo
            }

            Text(
                text = dateText,
                style = WdsTheme.typography.body2,
                color = WdsTheme.colors.colorContentDeemphasized
            )
        }
    }
}