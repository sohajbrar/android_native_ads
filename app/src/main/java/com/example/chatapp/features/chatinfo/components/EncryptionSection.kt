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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun EncryptionSection() {
    val colors = WdsTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Show encryption details */ }
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingTriple, vertical = WdsTheme.dimensions.wdsSpacingDouble),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = encryptionIcon(colors.colorContentDeemphasized),
            contentDescription = "Encryption",
            modifier = Modifier.size(24.dp),
            tint = colors.colorContentDeemphasized
        )

        Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingDouble))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingQuarter)
        ) {
            Text(
                text = "Encryption",
                style = WdsTheme.typography.body1,
                color = colors.colorContentDefault
            )
            Text(
                text = "Messages and calls are end-to-end encrypted. Tap to verify.",
                style = WdsTheme.typography.body2,
                color = colors.colorContentDeemphasized
            )
        }
    }
}

// Encryption lock icon definition
private fun encryptionIcon(color: Color) = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(color),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(6f, 22f)
        curveTo(5.45f, 22f, 4.97917f, 21.8042f, 4.5875f, 21.4125f)
        curveTo(4.19583f, 21.0208f, 4f, 20.55f, 4f, 20f)
        verticalLineTo(10f)
        curveTo(4f, 9.45f, 4.19583f, 8.97917f, 4.5875f, 8.5875f)
        curveTo(4.97917f, 8.19583f, 5.45f, 8f, 6f, 8f)
        horizontalLineTo(7f)
        verticalLineTo(6f)
        curveTo(7f, 4.61667f, 7.4875f, 3.4375f, 8.4625f, 2.4625f)
        curveTo(9.4375f, 1.4875f, 10.6167f, 1f, 12f, 1f)
        curveTo(13.3833f, 1f, 14.5625f, 1.4875f, 15.5375f, 2.4625f)
        curveTo(16.5125f, 3.4375f, 17f, 4.61667f, 17f, 6f)
        verticalLineTo(8f)
        horizontalLineTo(18f)
        curveTo(18.55f, 8f, 19.0208f, 8.19583f, 19.4125f, 8.5875f)
        curveTo(19.8042f, 8.97917f, 20f, 9.45f, 20f, 10f)
        verticalLineTo(20f)
        curveTo(20f, 20.55f, 19.8042f, 21.0208f, 19.4125f, 21.4125f)
        curveTo(19.0208f, 21.8042f, 18.55f, 22f, 18f, 22f)
        horizontalLineTo(6f)
        close()
        moveTo(6f, 20f)
        horizontalLineTo(18f)
        verticalLineTo(10f)
        horizontalLineTo(6f)
        verticalLineTo(20f)
        close()
        moveTo(12f, 17f)
        curveTo(12.55f, 17f, 13.0208f, 16.8042f, 13.4125f, 16.4125f)
        curveTo(13.8042f, 16.0208f, 14f, 15.55f, 14f, 15f)
        curveTo(14f, 14.45f, 13.8042f, 13.9792f, 13.4125f, 13.5875f)
        curveTo(13.0208f, 13.1958f, 12.55f, 13f, 12f, 13f)
        curveTo(11.45f, 13f, 10.9792f, 13.1958f, 10.5875f, 13.5875f)
        curveTo(10.1958f, 13.9792f, 10f, 14.45f, 10f, 15f)
        curveTo(10f, 15.55f, 10.1958f, 16.0208f, 10.5875f, 16.4125f)
        curveTo(10.9792f, 16.8042f, 11.45f, 17f, 12f, 17f)
        close()
        moveTo(9f, 8f)
        horizontalLineTo(15f)
        verticalLineTo(6f)
        curveTo(15f, 5.16667f, 14.7083f, 4.45833f, 14.125f, 3.875f)
        curveTo(13.5417f, 3.29167f, 12.8333f, 3f, 12f, 3f)
        curveTo(11.1667f, 3f, 10.4583f, 3.29167f, 9.875f, 3.875f)
        curveTo(9.29167f, 4.45833f, 9f, 5.16667f, 9f, 6f)
        verticalLineTo(8f)
        close()
    }
}.build()