package com.example.chatapp.features.chatinfo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun GroupManagementSection(
    isLocked: Boolean,
    onToggleLock: () -> Unit,
    hasDisappearingMessages: Boolean,
    onToggleDisappearingMessages: () -> Unit,
    onGroupPermissions: () -> Unit
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
            // Disappearing messages
            ManagementItem(
                icon = disappearingMessagesIcon,
                title = "Disappearing messages",
                subtitle = if (hasDisappearingMessages) "On" else "Off",
                onClick = onToggleDisappearingMessages
            )

            // Chat lock
            ManagementItem(
                icon = chatLockIcon,
                title = "Chat lock",
                subtitle = "Lock and hide this chat on this device",
                trailing = {
                    WdsSwitch(
                        checked = isLocked,
                        onCheckedChange = { onToggleLock() }
                    )
                },
                onClick = onToggleLock
            )

            // Group permissions
            ManagementItem(
                icon = groupPermissionsIcon,
                title = "Group permissions",
                subtitle = null,
                onClick = onGroupPermissions
            )
        }
    }
}

@Composable
private fun ManagementItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
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

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingQuarter)
        ) {
            Text(
                text = title,
                style = WdsTheme.typography.body1,
                color = WdsTheme.colors.colorContentDefault
            )

            subtitle?.let {
                Text(
                    text = it,
                    style = WdsTheme.typography.body2,
                    color = WdsTheme.colors.colorContentDeemphasized
                )
            }
        }

        trailing?.invoke()
    }
}

// Disappearing messages icon definition
private val disappearingMessagesIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(12f, 2f)
        curveTo(6.47715f, 2f, 2f, 6.47715f, 2f, 12f)
        curveTo(2f, 17.5228f, 6.47715f, 22f, 12f, 22f)
        curveTo(12.0547f, 22f, 12.1094f, 21.9996f, 12.1639f, 21.9987f)
        curveTo(12.7775f, 21.9888f, 13.2669f, 21.4834f, 13.257f, 20.8698f)
        curveTo(13.2471f, 20.2563f, 12.7417f, 19.7669f, 12.1281f, 19.7767f)
        curveTo(12.0855f, 19.7774f, 12.0428f, 19.7778f, 12f, 19.7778f)
        curveTo(7.70445f, 19.7778f, 4.22222f, 16.2955f, 4.22222f, 12f)
        curveTo(4.22222f, 7.70445f, 7.70445f, 4.22222f, 12f, 4.22222f)
        curveTo(12.0428f, 4.22222f, 12.0855f, 4.22257f, 12.1281f, 4.22325f)
        curveTo(12.7417f, 4.23314f, 13.2471f, 3.74375f, 13.257f, 3.13018f)
        curveTo(13.2669f, 2.51661f, 12.7775f, 2.0112f, 12.1639f, 2.00132f)
        curveTo(12.1094f, 2.00044f, 12.0547f, 2f, 12f, 2f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(16.8592f, 3.25814f)
        curveTo(16.3231f, 2.95957f, 15.6465f, 3.15213f, 15.3479f, 3.68825f)
        curveTo(15.0493f, 4.22437f, 15.2419f, 4.90102f, 15.778f, 5.19959f)
        curveTo(15.8522f, 5.24089f, 15.9256f, 5.28338f, 15.9983f, 5.32703f)
        curveTo(16.5243f, 5.643f, 17.2069f, 5.4727f, 17.5229f, 4.94665f)
        curveTo(17.8389f, 4.4206f, 17.6686f, 3.738f, 17.1425f, 3.42203f)
        curveTo(17.0491f, 3.36591f, 16.9546f, 3.31127f, 16.8592f, 3.25814f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(19.0534f, 6.47712f)
        curveTo(19.5794f, 6.16115f, 20.262f, 6.33145f, 20.578f, 6.8575f)
        curveTo(20.6341f, 6.95093f, 20.6887f, 7.04537f, 20.7419f, 7.14077f)
        curveTo(21.0404f, 7.67689f, 20.8479f, 8.35353f, 20.3118f, 8.65211f)
        curveTo(19.7756f, 8.95068f, 19.099f, 8.75811f, 18.8004f, 8.22199f)
        curveTo(18.7591f, 8.14782f, 18.7166f, 8.07439f, 18.673f, 8.00173f)
        curveTo(18.357f, 7.47568f, 18.5273f, 6.79309f, 19.0534f, 6.47712f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(21.9987f, 11.8361f)
        curveTo(21.9888f, 11.2225f, 21.4834f, 10.7331f, 20.8698f, 10.743f)
        curveTo(20.2563f, 10.7529f, 19.7669f, 11.2583f, 19.7767f, 11.8719f)
        curveTo(19.7774f, 11.9145f, 19.7778f, 11.9572f, 19.7778f, 12f)
        curveTo(19.7778f, 12.0428f, 19.7774f, 12.0855f, 19.7767f, 12.1281f)
        curveTo(19.7669f, 12.7417f, 20.2563f, 13.2471f, 20.8698f, 13.257f)
        curveTo(21.4834f, 13.2669f, 21.9888f, 12.7775f, 21.9987f, 12.1639f)
        curveTo(21.9996f, 12.1094f, 22f, 12.0547f, 22f, 12f)
        curveTo(22f, 11.9453f, 21.9996f, 11.8906f, 21.9987f, 11.8361f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(20.3118f, 15.3479f)
        curveTo(20.8479f, 15.6465f, 21.0404f, 16.3231f, 20.7419f, 16.8592f)
        curveTo(20.6887f, 16.9546f, 20.6341f, 17.0491f, 20.578f, 17.1425f)
        curveTo(20.262f, 17.6686f, 19.5794f, 17.8389f, 19.0534f, 17.5229f)
        curveTo(18.5273f, 17.2069f, 18.357f, 16.5243f, 18.673f, 15.9983f)
        curveTo(18.7166f, 15.9256f, 18.7591f, 15.8522f, 18.8004f, 15.778f)
        curveTo(19.099f, 15.2419f, 19.7756f, 15.0493f, 20.3118f, 15.3479f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(17.1425f, 20.578f)
        curveTo(17.6686f, 20.262f, 17.8389f, 19.5794f, 17.5229f, 19.0534f)
        curveTo(17.2069f, 18.5273f, 16.5243f, 18.357f, 15.9983f, 18.673f)
        curveTo(15.9256f, 18.7166f, 15.8522f, 18.7591f, 15.778f, 18.8004f)
        curveTo(15.2419f, 19.099f, 15.0493f, 19.7756f, 15.3479f, 20.3118f)
        curveTo(15.6465f, 20.8479f, 16.3231f, 21.0404f, 16.8592f, 20.7419f)
        curveTo(16.9546f, 20.6887f, 17.0491f, 20.6341f, 17.1425f, 20.578f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(16.7811f, 7.6229f)
        curveTo(16.5556f, 7.39749f, 16.1988f, 7.37213f, 15.9438f, 7.5634f)
        lineTo(11.3327f, 11.0217f)
        curveTo(10.6836f, 11.5085f, 10.6161f, 12.4574f, 11.1899f, 13.0312f)
        lineTo(11.3728f, 13.2141f)
        curveTo(11.9465f, 13.7878f, 12.8954f, 13.7204f, 13.3823f, 13.0713f)
        lineTo(16.8406f, 8.46018f)
        curveTo(17.0318f, 8.20516f, 17.0065f, 7.84831f, 16.7811f, 7.6229f)
        close()
    }
}.build()

// Chat lock icon definition
private val chatLockIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(6f, 12f)
        curveTo(6f, 11.4477f, 6.44772f, 11f, 7f, 11f)
        lineTo(17f, 11f)
        curveTo(17.5523f, 11f, 18f, 11.4477f, 18f, 12f)
        curveTo(18f, 12.5523f, 17.5523f, 13f, 17f, 13f)
        lineTo(7f, 13f)
        curveTo(6.44772f, 13f, 6f, 12.5523f, 6f, 12f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(6f, 16f)
        curveTo(6f, 15.4477f, 6.44772f, 15f, 7f, 15f)
        lineTo(14f, 15f)
        curveTo(14.5523f, 15f, 15f, 15.4477f, 15f, 16f)
        curveTo(15f, 16.5523f, 14.5523f, 17f, 14f, 17f)
        horizontalLineTo(7f)
        curveTo(6.44772f, 17f, 6f, 16.5523f, 6f, 16f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.EvenOdd
    ) {
        moveTo(7f, 21f)
        lineTo(3.53f, 23.06f)
        curveTo(2.86348f, 23.4599f, 2f, 22.9873f, 2f, 22.21f)
        lineTo(2f, 9.66667f)
        curveTo(2f, 8.19391f, 3.19391f, 7f, 4.66667f, 7f)
        lineTo(7f, 7f)
        verticalLineTo(6f)
        curveTo(7f, 3.23858f, 9.23858f, 1f, 12f, 1f)
        curveTo(14.7614f, 1f, 17f, 3.23858f, 17f, 6f)
        verticalLineTo(7f)
        lineTo(19.3333f, 7f)
        curveTo(20.8061f, 7f, 22f, 8.19391f, 22f, 9.66667f)
        verticalLineTo(18.3333f)
        curveTo(22f, 19.8061f, 20.8061f, 21f, 19.3333f, 21f)
        horizontalLineTo(7f)
        close()
        moveTo(15f, 7f)
        horizontalLineTo(9f)
        lineTo(9f, 6f)
        curveTo(9f, 4.34315f, 10.3431f, 3f, 12f, 3f)
        curveTo(13.6569f, 3f, 15f, 4.34315f, 15f, 6f)
        verticalLineTo(7f)
        close()
        moveTo(19.3333f, 9f)
        curveTo(19.7015f, 9f, 20f, 9.29848f, 20f, 9.66666f)
        verticalLineTo(18.3333f)
        curveTo(20f, 18.7015f, 19.7015f, 19f, 19.3333f, 19f)
        horizontalLineTo(6.44603f)
        lineTo(4f, 20.4676f)
        lineTo(4f, 9.66667f)
        curveTo(4f, 9.29848f, 4.29848f, 9f, 4.66667f, 9f)
        lineTo(19.3333f, 9f)
        close()
    }
}.build()

// Group permissions icon definition
private val groupPermissionsIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFF5B6368)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(10.125f, 22f)
        curveTo(9.87497f, 22f, 9.6583f, 21.9167f, 9.47497f, 21.75f)
        curveTo(9.29164f, 21.5833f, 9.1833f, 21.375f, 9.14997f, 21.125f)
        lineTo(8.84997f, 18.8f)
        curveTo(8.6333f, 18.7167f, 8.42914f, 18.6167f, 8.23747f, 18.5f)
        curveTo(8.0458f, 18.3833f, 7.8583f, 18.2583f, 7.67497f, 18.125f)
        lineTo(5.49997f, 19.025f)
        curveTo(5.26664f, 19.125f, 5.0333f, 19.1375f, 4.79997f, 19.0625f)
        curveTo(4.56664f, 18.9875f, 4.3833f, 18.8417f, 4.24997f, 18.625f)
        lineTo(2.39997f, 15.4f)
        curveTo(2.26664f, 15.1833f, 2.22497f, 14.95f, 2.27497f, 14.7f)
        curveTo(2.32497f, 14.45f, 2.44997f, 14.25f, 2.64997f, 14.1f)
        lineTo(4.52497f, 12.675f)
        curveTo(4.5083f, 12.5583f, 4.49997f, 12.4458f, 4.49997f, 12.3375f)
        verticalLineTo(11.6625f)
        curveTo(4.49997f, 11.5542f, 4.5083f, 11.4417f, 4.52497f, 11.325f)
        lineTo(2.64997f, 9.9f)
        curveTo(2.44997f, 9.75f, 2.32497f, 9.55f, 2.27497f, 9.3f)
        curveTo(2.22497f, 9.05f, 2.26664f, 8.81667f, 2.39997f, 8.6f)
        lineTo(4.24997f, 5.375f)
        curveTo(4.3833f, 5.15833f, 4.56664f, 5.0125f, 4.79997f, 4.9375f)
        curveTo(5.0333f, 4.8625f, 5.26664f, 4.875f, 5.49997f, 4.975f)
        lineTo(7.67497f, 5.875f)
        curveTo(7.8583f, 5.74167f, 8.04997f, 5.61667f, 8.24997f, 5.5f)
        curveTo(8.44997f, 5.38333f, 8.64997f, 5.28333f, 8.84997f, 5.2f)
        lineTo(9.14997f, 2.875f)
        curveTo(9.1833f, 2.625f, 9.29164f, 2.41667f, 9.47497f, 2.25f)
        curveTo(9.6583f, 2.08333f, 9.87497f, 2f, 10.125f, 2f)
        horizontalLineTo(13.875f)
        curveTo(14.125f, 2f, 14.3416f, 2.08333f, 14.525f, 2.25f)
        curveTo(14.7083f, 2.41667f, 14.8166f, 2.625f, 14.85f, 2.875f)
        lineTo(15.15f, 5.2f)
        curveTo(15.3666f, 5.28333f, 15.5708f, 5.38333f, 15.7625f, 5.5f)
        curveTo(15.9541f, 5.61667f, 16.1416f, 5.74167f, 16.325f, 5.875f)
        lineTo(18.5f, 4.975f)
        curveTo(18.7333f, 4.875f, 18.9666f, 4.8625f, 19.2f, 4.9375f)
        curveTo(19.4333f, 5.0125f, 19.6166f, 5.15833f, 19.75f, 5.375f)
        lineTo(21.6f, 8.6f)
        curveTo(21.7333f, 8.81667f, 21.775f, 9.05f, 21.725f, 9.3f)
        curveTo(21.675f, 9.55f, 21.55f, 9.75f, 21.35f, 9.9f)
        lineTo(19.475f, 11.325f)
        curveTo(19.4916f, 11.4417f, 19.5f, 11.5542f, 19.5f, 11.6625f)
        verticalLineTo(12.3375f)
        curveTo(19.5f, 12.4458f, 19.4833f, 12.5583f, 19.45f, 12.675f)
        lineTo(21.325f, 14.1f)
        curveTo(21.525f, 14.25f, 21.65f, 14.45f, 21.7f, 14.7f)
        curveTo(21.75f, 14.95f, 21.7083f, 15.1833f, 21.575f, 15.4f)
        lineTo(19.725f, 18.6f)
        curveTo(19.5916f, 18.8167f, 19.4041f, 18.9667f, 19.1625f, 19.05f)
        curveTo(18.9208f, 19.1333f, 18.6833f, 19.125f, 18.45f, 19.025f)
        lineTo(16.325f, 18.125f)
        curveTo(16.1416f, 18.2583f, 15.95f, 18.3833f, 15.75f, 18.5f)
        curveTo(15.55f, 18.6167f, 15.35f, 18.7167f, 15.15f, 18.8f)
        lineTo(14.85f, 21.125f)
        curveTo(14.8166f, 21.375f, 14.7083f, 21.5833f, 14.525f, 21.75f)
        curveTo(14.3416f, 21.9167f, 14.125f, 22f, 13.875f, 22f)
        horizontalLineTo(10.125f)
        close()
        moveTo(11f, 20f)
        horizontalLineTo(12.975f)
        lineTo(13.325f, 17.35f)
        curveTo(13.8416f, 17.2167f, 14.3208f, 17.0208f, 14.7625f, 16.7625f)
        curveTo(15.2041f, 16.5042f, 15.6083f, 16.1917f, 15.975f, 15.825f)
        lineTo(18.45f, 16.85f)
        lineTo(19.425f, 15.15f)
        lineTo(17.275f, 13.525f)
        curveTo(17.3583f, 13.2917f, 17.4166f, 13.0458f, 17.45f, 12.7875f)
        curveTo(17.4833f, 12.5292f, 17.5f, 12.2667f, 17.5f, 12f)
        curveTo(17.5f, 11.7333f, 17.4833f, 11.4708f, 17.45f, 11.2125f)
        curveTo(17.4166f, 10.9542f, 17.3583f, 10.7083f, 17.275f, 10.475f)
        lineTo(19.425f, 8.85f)
        lineTo(18.45f, 7.15f)
        lineTo(15.975f, 8.2f)
        curveTo(15.6083f, 7.81667f, 15.2041f, 7.49583f, 14.7625f, 7.2375f)
        curveTo(14.3208f, 6.97917f, 13.8416f, 6.78333f, 13.325f, 6.65f)
        lineTo(13f, 4f)
        horizontalLineTo(11.025f)
        lineTo(10.675f, 6.65f)
        curveTo(10.1583f, 6.78333f, 9.67914f, 6.97917f, 9.23747f, 7.2375f)
        curveTo(8.7958f, 7.49583f, 8.39164f, 7.80833f, 8.02497f, 8.175f)
        lineTo(5.54997f, 7.15f)
        lineTo(4.57497f, 8.85f)
        lineTo(6.72497f, 10.45f)
        curveTo(6.64164f, 10.7f, 6.5833f, 10.95f, 6.54997f, 11.2f)
        curveTo(6.51664f, 11.45f, 6.49997f, 11.7167f, 6.49997f, 12f)
        curveTo(6.49997f, 12.2667f, 6.51664f, 12.525f, 6.54997f, 12.775f)
        curveTo(6.5833f, 13.025f, 6.64164f, 13.275f, 6.72497f, 13.525f)
        lineTo(4.57497f, 15.15f)
        lineTo(5.54997f, 16.85f)
        lineTo(8.02497f, 15.8f)
        curveTo(8.39164f, 16.1833f, 8.7958f, 16.5042f, 9.23747f, 16.7625f)
        curveTo(9.67914f, 17.0208f, 10.1583f, 17.2167f, 10.675f, 17.35f)
        lineTo(11f, 20f)
        close()
        moveTo(12.05f, 15.5f)
        curveTo(13.0166f, 15.5f, 13.8416f, 15.1583f, 14.525f, 14.475f)
        curveTo(15.2083f, 13.7917f, 15.55f, 12.9667f, 15.55f, 12f)
        curveTo(15.55f, 11.0333f, 15.2083f, 10.2083f, 14.525f, 9.525f)
        curveTo(13.8416f, 8.84167f, 13.0166f, 8.5f, 12.05f, 8.5f)
        curveTo(11.0666f, 8.5f, 10.2375f, 8.84167f, 9.56247f, 9.525f)
        curveTo(8.88747f, 10.2083f, 8.54997f, 11.0333f, 8.54997f, 12f)
        curveTo(8.54997f, 12.9667f, 8.88747f, 13.7917f, 9.56247f, 14.475f)
        curveTo(10.2375f, 15.1583f, 11.0666f, 15.5f, 12.05f, 15.5f)
        close()
    }
}.build()