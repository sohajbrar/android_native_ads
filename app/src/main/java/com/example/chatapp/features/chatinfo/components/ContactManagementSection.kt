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

@Composable
fun ContactManagementSection(
    hasDisappearingMessages: Boolean,
    onToggleDisappearingMessages: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingSingle, vertical = WdsTheme.dimensions.wdsSpacingHalf),
        colors = CardDefaults.cardColors(containerColor = WdsTheme.colors.colorSurfaceDefault),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleDisappearingMessages() }
                .padding(WdsTheme.dimensions.wdsSpacingDouble),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = disappearingMessagesIcon,
                contentDescription = "Disappearing messages",
                modifier = Modifier.size(24.dp),
                tint = WdsTheme.colors.colorContentDeemphasized
            )

            Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingDouble))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingQuarter)
            ) {
                Text(
                    text = "Disappearing messages",
                    style = WdsTheme.typography.body1,
                    color = WdsTheme.colors.colorContentDefault
                )

                Text(
                    text = if (hasDisappearingMessages) "On" else "Off",
                    style = WdsTheme.typography.body2,
                    color = WdsTheme.colors.colorContentDeemphasized
                )
            }
        }
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