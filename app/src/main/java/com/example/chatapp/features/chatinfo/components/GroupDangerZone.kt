package com.example.chatapp.features.chatinfo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun GroupDangerZone(
    onExitGroup: () -> Unit,
    onReportGroup: () -> Unit
) {
    var showExitDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingSingle, vertical = WdsTheme.dimensions.wdsSpacingHalf),
        colors = CardDefaults.cardColors(containerColor = WdsTheme.colors.colorSurfaceDefault),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Exit group
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showExitDialog = true }
                    .padding(WdsTheme.dimensions.wdsSpacingDouble),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = exitGroupIcon,
                    contentDescription = "Exit group",
                    modifier = Modifier.size(24.dp),
                    tint = WdsTheme.colors.colorNegative
                )

                Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingDouble))

                Text(
                    text = "Exit group",
                    style = WdsTheme.typography.body1,
                    color = WdsTheme.colors.colorNegative
                )
            }

            // Report group
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showReportDialog = true }
                    .padding(WdsTheme.dimensions.wdsSpacingDouble),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = reportIcon,
                    contentDescription = "Report group",
                    modifier = Modifier.size(24.dp),
                    tint = WdsTheme.colors.colorNegative
                )

                Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingDouble))

                Text(
                    text = "Report group",
                    style = WdsTheme.typography.body1,
                    color = WdsTheme.colors.colorNegative
                )
            }
        }
    }

    // Exit group confirmation dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = "Exit group?",
                    style = WdsTheme.typography.headline1
                )
            },
            text = {
                Text("You will no longer receive messages from this group.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onExitGroup()
                        showExitDialog = false
                    }
                ) {
                    Text(
                        text = "Exit",
                        color = WdsTheme.colors.colorNegative
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Report group confirmation dialog
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = {
                Text(
                    text = "Report this group?",
                    style = WdsTheme.typography.headline1
                )
            },
            text = {
                Text("This group and its recent messages will be forwarded to WhatsApp. WhatsApp will be able to see your phone number.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onReportGroup()
                        showReportDialog = false
                    }
                ) {
                    Text(
                        text = "Report",
                        color = WdsTheme.colors.colorNegative
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showReportDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Exit group icon definition
private val exitGroupIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFFEA0038)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(5f, 21f)
        curveTo(4.45f, 21f, 3.97917f, 20.8042f, 3.5875f, 20.4125f)
        curveTo(3.19583f, 20.0208f, 3f, 19.55f, 3f, 19f)
        verticalLineTo(5f)
        curveTo(3f, 4.45f, 3.19583f, 3.97917f, 3.5875f, 3.5875f)
        curveTo(3.97917f, 3.19583f, 4.45f, 3f, 5f, 3f)
        horizontalLineTo(11f)
        curveTo(11.2833f, 3f, 11.5208f, 3.09583f, 11.7125f, 3.2875f)
        curveTo(11.9042f, 3.47917f, 12f, 3.71667f, 12f, 4f)
        curveTo(12f, 4.28333f, 11.9042f, 4.52083f, 11.7125f, 4.7125f)
        curveTo(11.5208f, 4.90417f, 11.2833f, 5f, 11f, 5f)
        horizontalLineTo(5f)
        verticalLineTo(19f)
        horizontalLineTo(11f)
        curveTo(11.2833f, 19f, 11.5208f, 19.0958f, 11.7125f, 19.2875f)
        curveTo(11.9042f, 19.4792f, 12f, 19.7167f, 12f, 20f)
        curveTo(12f, 20.2833f, 11.9042f, 20.5208f, 11.7125f, 20.7125f)
        curveTo(11.5208f, 20.9042f, 11.2833f, 21f, 11f, 21f)
        horizontalLineTo(5f)
        close()
        moveTo(17.175f, 13f)
        horizontalLineTo(10f)
        curveTo(9.71667f, 13f, 9.47917f, 12.9042f, 9.2875f, 12.7125f)
        curveTo(9.09583f, 12.5208f, 9f, 12.2833f, 9f, 12f)
        curveTo(9f, 11.7167f, 9.09583f, 11.4792f, 9.2875f, 11.2875f)
        curveTo(9.47917f, 11.0958f, 9.71667f, 11f, 10f, 11f)
        horizontalLineTo(17.175f)
        lineTo(15.3f, 9.125f)
        curveTo(15.1167f, 8.94167f, 15.025f, 8.71667f, 15.025f, 8.45f)
        curveTo(15.025f, 8.18333f, 15.1167f, 7.95f, 15.3f, 7.75f)
        curveTo(15.4833f, 7.55f, 15.7167f, 7.44583f, 16f, 7.4375f)
        curveTo(16.2833f, 7.42917f, 16.525f, 7.525f, 16.725f, 7.725f)
        lineTo(20.3f, 11.3f)
        curveTo(20.5f, 11.5f, 20.6f, 11.7333f, 20.6f, 12f)
        curveTo(20.6f, 12.2667f, 20.5f, 12.5f, 20.3f, 12.7f)
        lineTo(16.725f, 16.275f)
        curveTo(16.525f, 16.475f, 16.2875f, 16.5708f, 16.0125f, 16.5625f)
        curveTo(15.7375f, 16.5542f, 15.5f, 16.45f, 15.3f, 16.25f)
        curveTo(15.1167f, 16.05f, 15.0292f, 15.8125f, 15.0375f, 15.5375f)
        curveTo(15.0458f, 15.2625f, 15.1417f, 15.0333f, 15.325f, 14.85f)
        lineTo(17.175f, 13f)
        close()
    }
}.build()

// Report icon definition
private val reportIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFFEA0038)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(3f, 16f)
        curveTo(2.46667f, 16f, 2f, 15.8f, 1.6f, 15.4f)
        curveTo(1.2f, 15f, 1f, 14.5333f, 1f, 14f)
        verticalLineTo(12f)
        curveTo(1f, 11.8833f, 1.01667f, 11.7583f, 1.05f, 11.625f)
        curveTo(1.08333f, 11.4917f, 1.11667f, 11.3667f, 1.15f, 11.25f)
        lineTo(4.15f, 4.2f)
        curveTo(4.3f, 3.86667f, 4.55f, 3.58333f, 4.9f, 3.35f)
        curveTo(5.25f, 3.11667f, 5.61667f, 3f, 6f, 3f)
        horizontalLineTo(17f)
        verticalLineTo(16f)
        lineTo(11f, 21.95f)
        curveTo(10.75f, 22.2f, 10.4542f, 22.3458f, 10.1125f, 22.3875f)
        curveTo(9.77083f, 22.4292f, 9.44167f, 22.3667f, 9.125f, 22.2f)
        curveTo(8.80833f, 22.0333f, 8.575f, 21.8f, 8.425f, 21.5f)
        curveTo(8.275f, 21.2f, 8.24167f, 20.8917f, 8.325f, 20.575f)
        lineTo(9.45f, 16f)
        horizontalLineTo(3f)
        close()
        moveTo(15f, 15.15f)
        verticalLineTo(5f)
        horizontalLineTo(6f)
        lineTo(3f, 12f)
        verticalLineTo(14f)
        horizontalLineTo(12f)
        lineTo(10.65f, 19.5f)
        lineTo(15f, 15.15f)
        close()
        moveTo(20f, 3f)
        curveTo(20.55f, 3f, 21.0208f, 3.19583f, 21.4125f, 3.5875f)
        curveTo(21.8042f, 3.97917f, 22f, 4.45f, 22f, 5f)
        verticalLineTo(14f)
        curveTo(22f, 14.55f, 21.8042f, 15.0208f, 21.4125f, 15.4125f)
        curveTo(21.0208f, 15.8042f, 20.55f, 16f, 20f, 16f)
        horizontalLineTo(17f)
        verticalLineTo(14f)
        horizontalLineTo(20f)
        verticalLineTo(5f)
        horizontalLineTo(17f)
        verticalLineTo(3f)
        horizontalLineTo(20f)
        close()
    }
}.build()