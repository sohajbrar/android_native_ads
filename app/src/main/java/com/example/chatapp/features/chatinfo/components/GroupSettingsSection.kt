package com.example.chatapp.features.chatinfo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme
import com.example.chatapp.wds.components.WdsSwitch

@Composable
fun GroupSettingsSection(
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    onCustomNotifications: () -> Unit,
    onMediaVisibility: () -> Unit
) {
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
            // Mute notifications
            SettingItem(
                icon = if (isMuted) Icons.Default.NotificationsOff else notificationBellIcon,
                title = "Mute notifications",
                trailing = {
                    WdsSwitch(
                        checked = isMuted,
                        onCheckedChange = { onToggleMute() }
                    )
                },
                onClick = onToggleMute
            )

            // Custom notifications
            SettingItem(
                icon = musicNoteIcon,
                title = "Custom notifications",
                onClick = onCustomNotifications
            )

            // Media visibility
            SettingItem(
                icon = mediaVisibilityIcon,
                title = "Media visibility",
                onClick = onMediaVisibility
            )
        }
    }
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    title: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(WdsTheme.dimensions.wdsSpacingDouble),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = WdsTheme.colors.colorContentDeemphasized
        )

        Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingDouble))

        Text(
            text = title,
            style = WdsTheme.typography.body1,
            color = WdsTheme.colors.colorContentDefault,
            modifier = Modifier.weight(1f)
        )

        trailing?.invoke()
    }
}

// Music note icon definition
private val musicNoteIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(10f, 21f)
        curveTo(8.9f, 21f, 7.95833f, 20.6083f, 7.175f, 19.825f)
        curveTo(6.39167f, 19.0417f, 6f, 18.1f, 6f, 17f)
        curveTo(6f, 15.9f, 6.39167f, 14.9583f, 7.175f, 14.175f)
        curveTo(7.95833f, 13.3917f, 8.9f, 13f, 10f, 13f)
        curveTo(10.3833f, 13f, 10.7375f, 13.0458f, 11.0625f, 13.1375f)
        curveTo(11.3875f, 13.2292f, 11.7f, 13.3667f, 12f, 13.55f)
        verticalLineTo(5f)
        curveTo(12f, 4.45f, 12.1958f, 3.97917f, 12.5875f, 3.5875f)
        curveTo(12.9792f, 3.19583f, 13.45f, 3f, 14f, 3f)
        horizontalLineTo(16f)
        curveTo(16.55f, 3f, 17.0208f, 3.19583f, 17.4125f, 3.5875f)
        curveTo(17.8042f, 3.97917f, 18f, 4.45f, 18f, 5f)
        curveTo(18f, 5.55f, 17.8042f, 6.02083f, 17.4125f, 6.4125f)
        curveTo(17.0208f, 6.80417f, 16.55f, 7f, 16f, 7f)
        horizontalLineTo(14f)
        verticalLineTo(17f)
        curveTo(14f, 18.1f, 13.6083f, 19.0417f, 12.825f, 19.825f)
        curveTo(12.0417f, 20.6083f, 11.1f, 21f, 10f, 21f)
        close()
    }
}.build()

// Notification bell icon definition
private val notificationBellIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(5f, 19f)
        curveTo(4.71667f, 19f, 4.47917f, 18.9042f, 4.2875f, 18.7125f)
        curveTo(4.09583f, 18.5208f, 4f, 18.2833f, 4f, 18f)
        curveTo(4f, 17.7167f, 4.09583f, 17.4792f, 4.2875f, 17.2875f)
        curveTo(4.47917f, 17.0958f, 4.71667f, 17f, 5f, 17f)
        horizontalLineTo(6f)
        verticalLineTo(10f)
        curveTo(6f, 8.61667f, 6.41667f, 7.3875f, 7.25f, 6.3125f)
        curveTo(8.08333f, 5.2375f, 9.16667f, 4.53333f, 10.5f, 4.2f)
        verticalLineTo(3.5f)
        curveTo(10.5f, 3.08333f, 10.6458f, 2.72917f, 10.9375f, 2.4375f)
        curveTo(11.2292f, 2.14583f, 11.5833f, 2f, 12f, 2f)
        curveTo(12.4167f, 2f, 12.7708f, 2.14583f, 13.0625f, 2.4375f)
        curveTo(13.3542f, 2.72917f, 13.5f, 3.08333f, 13.5f, 3.5f)
        verticalLineTo(4.2f)
        curveTo(14.8333f, 4.53333f, 15.9167f, 5.2375f, 16.75f, 6.3125f)
        curveTo(17.5833f, 7.3875f, 18f, 8.61667f, 18f, 10f)
        verticalLineTo(17f)
        horizontalLineTo(19f)
        curveTo(19.2833f, 17f, 19.5208f, 17.0958f, 19.7125f, 17.2875f)
        curveTo(19.9042f, 17.4792f, 20f, 17.7167f, 20f, 18f)
        curveTo(20f, 18.2833f, 19.9042f, 18.5208f, 19.7125f, 18.7125f)
        curveTo(19.5208f, 18.9042f, 19.2833f, 19f, 19f, 19f)
        horizontalLineTo(5f)
        close()
        moveTo(12f, 22f)
        curveTo(11.45f, 22f, 10.9792f, 21.8042f, 10.5875f, 21.4125f)
        curveTo(10.1958f, 21.0208f, 10f, 20.55f, 10f, 20f)
        horizontalLineTo(14f)
        curveTo(14f, 20.55f, 13.8042f, 21.0208f, 13.4125f, 21.4125f)
        curveTo(13.0208f, 21.8042f, 12.55f, 22f, 12f, 22f)
        close()
        moveTo(8f, 17f)
        horizontalLineTo(16f)
        verticalLineTo(10f)
        curveTo(16f, 8.9f, 15.6083f, 7.95833f, 14.825f, 7.175f)
        curveTo(14.0417f, 6.39167f, 13.1f, 6f, 12f, 6f)
        curveTo(10.9f, 6f, 9.95833f, 6.39167f, 9.175f, 7.175f)
        curveTo(8.39167f, 7.95833f, 8f, 8.9f, 8f, 10f)
        verticalLineTo(17f)
        close()
    }
}.build()

// Media visibility icon definition
private val mediaVisibilityIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(5f, 21f)
        curveTo(4.45f, 21f, 3.97917f, 20.8042f, 3.5875f, 20.4125f)
        curveTo(3.19583f, 20.0208f, 3f, 19.55f, 3f, 19f)
        verticalLineTo(5f)
        curveTo(3f, 4.45f, 3.19583f, 3.97917f, 3.5875f, 3.5875f)
        curveTo(3.97917f, 3.19583f, 4.45f, 3f, 5f, 3f)
        horizontalLineTo(19f)
        curveTo(19.55f, 3f, 20.0208f, 3.19583f, 20.4125f, 3.5875f)
        curveTo(20.8042f, 3.97917f, 21f, 4.45f, 21f, 5f)
        verticalLineTo(19f)
        curveTo(21f, 19.55f, 20.8042f, 20.0208f, 20.4125f, 20.4125f)
        curveTo(20.0208f, 20.8042f, 19.55f, 21f, 19f, 21f)
        horizontalLineTo(5f)
        close()
        moveTo(5f, 19f)
        horizontalLineTo(19f)
        verticalLineTo(5f)
        horizontalLineTo(5f)
        verticalLineTo(19f)
        close()
        moveTo(7f, 17f)
        horizontalLineTo(17f)
        lineTo(14.25f, 13f)
        lineTo(11.25f, 17f)
        lineTo(9f, 14f)
        lineTo(7f, 17f)
        close()
    }
}.build()