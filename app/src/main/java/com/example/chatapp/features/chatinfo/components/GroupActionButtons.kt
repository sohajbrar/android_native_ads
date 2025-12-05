package com.example.chatapp.features.chatinfo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.chatapp.wds.theme.WdsTheme
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GroupActionButtons(
    onAudioCall: () -> Unit,
    onVideoCall: () -> Unit,
    onAddMember: () -> Unit,
    onSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingTriple)
            .padding(top = WdsTheme.dimensions.wdsSpacingSinglePlus, bottom = WdsTheme.dimensions.wdsSpacingDouble),
        horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
    ) {
        // Audio Call Button
        ActionTile(
            icon = phoneCallIcon,
            label = "Audio",
            onClick = onAudioCall,
            modifier = Modifier.weight(1f)
        )

        // Video Call Button
        ActionTile(
            icon = videoCallIcon,
            label = "Video",
            onClick = onVideoCall,
            modifier = Modifier.weight(1f)
        )

        // Search Button
        ActionTile(
            icon = Icons.Default.Search,
            label = "Search",
            onClick = onSearch,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ActionTile(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = WdsTheme.colors.colorSurfaceElevatedDefault,
        border = BorderStroke(WdsTheme.dimensions.wdsBorderWidthThin, WdsTheme.colors.colorOutlineDeemphasized),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = WdsTheme.dimensions.wdsSpacingHalf, vertical = WdsTheme.dimensions.wdsSpacingSinglePlus),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalf)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = WdsTheme.colors.colorAccent // WhatsApp green accent
            )
            Text(
                text = label,
                style = WdsTheme.typography.body2,
                color = WdsTheme.colors.colorContentDefault,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

// Video call icon definition
private val videoCallIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFF1DAA61)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(4f, 20f)
        curveTo(3.45f, 20f, 2.97917f, 19.8042f, 2.5875f, 19.4125f)
        curveTo(2.19583f, 19.0208f, 2f, 18.55f, 2f, 18f)
        verticalLineTo(6f)
        curveTo(2f, 5.45f, 2.19583f, 4.97917f, 2.5875f, 4.5875f)
        curveTo(2.97917f, 4.19583f, 3.45f, 4f, 4f, 4f)
        horizontalLineTo(16f)
        curveTo(16.55f, 4f, 17.0208f, 4.19583f, 17.4125f, 4.5875f)
        curveTo(17.8042f, 4.97917f, 18f, 5.45f, 18f, 6f)
        verticalLineTo(10.5f)
        lineTo(21.15f, 7.35f)
        curveTo(21.3167f, 7.18333f, 21.5f, 7.14167f, 21.7f, 7.225f)
        curveTo(21.9f, 7.30833f, 22f, 7.46667f, 22f, 7.7f)
        verticalLineTo(16.3f)
        curveTo(22f, 16.5333f, 21.9f, 16.6917f, 21.7f, 16.775f)
        curveTo(21.5f, 16.8583f, 21.3167f, 16.8167f, 21.15f, 16.65f)
        lineTo(18f, 13.5f)
        verticalLineTo(18f)
        curveTo(18f, 18.55f, 17.8042f, 19.0208f, 17.4125f, 19.4125f)
        curveTo(17.0208f, 19.8042f, 16.55f, 20f, 16f, 20f)
        horizontalLineTo(4f)
        close()
        moveTo(4f, 18f)
        horizontalLineTo(16f)
        verticalLineTo(6f)
        horizontalLineTo(4f)
        verticalLineTo(18f)
        close()
    }
}.build()

// Phone call icon definition
private val phoneCallIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFF1DAA61)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(19.95f, 21f)
        curveTo(17.8667f, 21f, 15.8083f, 20.5458f, 13.775f, 19.6375f)
        curveTo(11.7417f, 18.7292f, 9.89167f, 17.4417f, 8.225f, 15.775f)
        curveTo(6.55833f, 14.1083f, 5.27083f, 12.2583f, 4.3625f, 10.225f)
        curveTo(3.45417f, 8.19167f, 3f, 6.13333f, 3f, 4.05f)
        curveTo(3f, 3.75f, 3.1f, 3.5f, 3.3f, 3.3f)
        curveTo(3.5f, 3.1f, 3.75f, 3f, 4.05f, 3f)
        horizontalLineTo(8.1f)
        curveTo(8.33333f, 3f, 8.54167f, 3.07917f, 8.725f, 3.2375f)
        curveTo(8.90833f, 3.39583f, 9.01667f, 3.58333f, 9.05f, 3.8f)
        lineTo(9.7f, 7.3f)
        curveTo(9.73333f, 7.56667f, 9.725f, 7.79167f, 9.675f, 7.975f)
        curveTo(9.625f, 8.15833f, 9.53333f, 8.31667f, 9.4f, 8.45f)
        lineTo(6.975f, 10.9f)
        curveTo(7.30833f, 11.5167f, 7.70417f, 12.1125f, 8.1625f, 12.6875f)
        curveTo(8.62083f, 13.2625f, 9.125f, 13.8167f, 9.675f, 14.35f)
        curveTo(10.1917f, 14.8667f, 10.7333f, 15.3458f, 11.3f, 15.7875f)
        curveTo(11.8667f, 16.2292f, 12.4667f, 16.6333f, 13.1f, 17f)
        lineTo(15.45f, 14.65f)
        curveTo(15.6f, 14.5f, 15.7958f, 14.3875f, 16.0375f, 14.3125f)
        curveTo(16.2792f, 14.2375f, 16.5167f, 14.2167f, 16.75f, 14.25f)
        lineTo(20.2f, 14.95f)
        curveTo(20.4333f, 15.0167f, 20.625f, 15.1375f, 20.775f, 15.3125f)
        curveTo(20.925f, 15.4875f, 21f, 15.6833f, 21f, 15.9f)
        verticalLineTo(19.95f)
        curveTo(21f, 20.25f, 20.9f, 20.5f, 20.7f, 20.7f)
        curveTo(20.5f, 20.9f, 20.25f, 21f, 19.95f, 21f)
        close()
        moveTo(6.025f, 9f)
        lineTo(7.675f, 7.35f)
        lineTo(7.25f, 5f)
        horizontalLineTo(5.025f)
        curveTo(5.10833f, 5.68333f, 5.225f, 6.35833f, 5.375f, 7.025f)
        curveTo(5.525f, 7.69167f, 5.74167f, 8.35f, 6.025f, 9f)
        close()
        moveTo(14.975f, 17.95f)
        curveTo(15.625f, 18.2333f, 16.2875f, 18.4583f, 16.9625f, 18.625f)
        curveTo(17.6375f, 18.7917f, 18.3167f, 18.9f, 19f, 18.95f)
        verticalLineTo(16.75f)
        lineTo(16.65f, 16.275f)
        lineTo(14.975f, 17.95f)
        close()
    }
}.build()