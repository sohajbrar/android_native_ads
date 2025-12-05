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
fun ContactDangerZone(
    isBlocked: Boolean,
    userName: String,
    onBlockToggle: () -> Unit,
    onReport: () -> Unit
) {
    var showBlockDialog by remember { mutableStateOf(false) }
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
            // Block/Unblock
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showBlockDialog = true }
                    .padding(WdsTheme.dimensions.wdsSpacingDouble),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = blockIcon,
                    contentDescription = if (isBlocked) "Unblock" else "Block",
                    modifier = Modifier.size(24.dp),
                    tint = WdsTheme.colors.colorNegative
                )

                Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingDouble))

                Text(
                    text = if (isBlocked) "Unblock $userName" else "Block $userName",
                    style = WdsTheme.typography.body1,
                    color = WdsTheme.colors.colorNegative
                )
            }

            // Report
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showReportDialog = true }
                    .padding(WdsTheme.dimensions.wdsSpacingDouble),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = reportIcon,
                    contentDescription = "Report",
                    modifier = Modifier.size(24.dp),
                    tint = WdsTheme.colors.colorNegative
                )

                Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingDouble))

                Text(
                    text = "Report $userName",
                    style = WdsTheme.typography.body1,
                    color = WdsTheme.colors.colorNegative
                )
            }
        }
    }

    // Block/Unblock confirmation dialog
    if (showBlockDialog) {
        AlertDialog(
            onDismissRequest = { showBlockDialog = false },
            title = {
                Text(
                    text = if (isBlocked) "Unblock $userName?" else "Block $userName?",
                    style = WdsTheme.typography.headline1
                )
            },
            text = {
                Text(
                    text = if (isBlocked) {
                        "You will be able to receive messages and calls from $userName again."
                    } else {
                        "Blocked contacts will no longer be able to call you or send you messages."
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onBlockToggle()
                        showBlockDialog = false
                    }
                ) {
                    Text(
                        text = if (isBlocked) "Unblock" else "Block",
                        color = WdsTheme.colors.colorNegative
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showBlockDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Report confirmation dialog
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = {
                Text(
                    text = "Report $userName?",
                    style = WdsTheme.typography.headline1
                )
            },
            text = {
                Text("This contact will be reported to WhatsApp. Recent messages from this contact will be forwarded to WhatsApp.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onReport()
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

// Block icon definition
private val blockIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFFEA0038)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(12f, 22f)
        curveTo(10.6167f, 22f, 9.31667f, 21.7375f, 8.1f, 21.2125f)
        curveTo(6.88333f, 20.6875f, 5.825f, 19.975f, 4.925f, 19.075f)
        curveTo(4.025f, 18.175f, 3.3125f, 17.1167f, 2.7875f, 15.9f)
        curveTo(2.2625f, 14.6833f, 2f, 13.3833f, 2f, 12f)
        curveTo(2f, 10.6167f, 2.2625f, 9.31667f, 2.7875f, 8.1f)
        curveTo(3.3125f, 6.88333f, 4.025f, 5.825f, 4.925f, 4.925f)
        curveTo(5.825f, 4.025f, 6.88333f, 3.3125f, 8.1f, 2.7875f)
        curveTo(9.31667f, 2.2625f, 10.6167f, 2f, 12f, 2f)
        curveTo(13.3833f, 2f, 14.6833f, 2.2625f, 15.9f, 2.7875f)
        curveTo(17.1167f, 3.3125f, 18.175f, 4.025f, 19.075f, 4.925f)
        curveTo(19.975f, 5.825f, 20.6875f, 6.88333f, 21.2125f, 8.1f)
        curveTo(21.7375f, 9.31667f, 22f, 10.6167f, 22f, 12f)
        curveTo(22f, 13.3833f, 21.7375f, 14.6833f, 21.2125f, 15.9f)
        curveTo(20.6875f, 17.1167f, 19.975f, 18.175f, 19.075f, 19.075f)
        curveTo(18.175f, 19.975f, 17.1167f, 20.6875f, 15.9f, 21.2125f)
        curveTo(14.6833f, 21.7375f, 13.3833f, 22f, 12f, 22f)
        close()
        moveTo(12f, 20f)
        curveTo(14.2333f, 20f, 16.125f, 19.225f, 17.675f, 17.675f)
        curveTo(19.225f, 16.125f, 20f, 14.2333f, 20f, 12f)
        curveTo(20f, 11.1f, 19.8542f, 10.2333f, 19.5625f, 9.4f)
        curveTo(19.2708f, 8.56667f, 18.85f, 7.8f, 18.3f, 7.1f)
        lineTo(7.1f, 18.3f)
        curveTo(7.8f, 18.85f, 8.56667f, 19.2708f, 9.4f, 19.5625f)
        curveTo(10.2333f, 19.8542f, 11.1f, 20f, 12f, 20f)
        close()
        moveTo(5.7f, 16.9f)
        lineTo(16.9f, 5.7f)
        curveTo(16.2f, 5.15f, 15.4333f, 4.72917f, 14.6f, 4.4375f)
        curveTo(13.7667f, 4.14583f, 12.9f, 4f, 12f, 4f)
        curveTo(9.76667f, 4f, 7.875f, 4.775f, 6.325f, 6.325f)
        curveTo(4.775f, 7.875f, 4f, 9.76667f, 4f, 12f)
        curveTo(4f, 12.9f, 4.14583f, 13.7667f, 4.4375f, 14.6f)
        curveTo(4.72917f, 15.4333f, 5.15f, 16.2f, 5.7f, 16.9f)
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