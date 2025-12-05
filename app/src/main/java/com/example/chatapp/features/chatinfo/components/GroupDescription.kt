package com.example.chatapp.features.chatinfo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun GroupDescription(
    description: String?,
    createdBy: String?,
    createdAt: Long,
    onAddDescription: () -> Unit
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
                .padding(WdsTheme.dimensions.wdsSpacingDouble)
        ) {
            if (description.isNullOrEmpty()) {
                // Add description prompt
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAddDescription() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Add description",
                        modifier = Modifier.size(20.dp),
                        tint = WdsTheme.colors.colorAccent
                    )
                    Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingSingle))
                    Text(
                        text = "Add group description",
                        style = WdsTheme.typography.body2,
                        color = WdsTheme.colors.colorAccent
                    )
                }
            } else {
                // Show description
                Text(
                    text = description,
                    style = WdsTheme.typography.body2,
                    color = WdsTheme.colors.colorContentDefault
                )
            }

            Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingSingle))

            // Created info
            val date = Instant.ofEpochMilli(createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val formattedDate = date.format(formatter)
            val time = Instant.ofEpochMilli(createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalTime()
            val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
            val formattedTime = time.format(timeFormatter)

            Text(
                text = "Created by ${createdBy ?: "Unknown"}, $formattedDate, $formattedTime",
                style = WdsTheme.typography.body2,
                color = WdsTheme.colors.colorContentDeemphasized
            )
        }
    }
}