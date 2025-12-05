package com.example.chatapp.wds.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.chatapp.wds.theme.WdsTheme

data class WdsDialogButton(
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun WdsDialog(
    message: String,
    positiveButton: WdsDialogButton,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: ImageVector? = null,
    negativeButton: WdsDialogButton? = null,
    neutralButton: WdsDialogButton? = null
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier.width(280.dp),
            shape = RoundedCornerShape(WdsTheme.dimensions.wdsSpacingTriple),
            color = WdsTheme.colors.colorSurfaceElevatedDefault
        ) {
            Column(
                modifier = Modifier.padding(WdsTheme.dimensions.wdsSpacingTriple),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = WdsTheme.colors.colorAccent,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = WdsTheme.dimensions.wdsSpacingDouble)
                    )
                }
                
                // Title
                if (title != null) {
                    Text(
                        text = title,
                        style = WdsTheme.typography.headline2,
                        color = WdsTheme.colors.colorContentDefault,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = WdsTheme.dimensions.wdsSpacingSingle)
                    )
                }
                
                // Message
                Text(
                    text = message,
                    style = WdsTheme.typography.body2,
                    color = WdsTheme.colors.colorContentDeemphasized,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = WdsTheme.dimensions.wdsSpacingTriple)
                )
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
                ) {
                    // Neutral button (left)
                    if (neutralButton != null) {
                        TextButton(
                            onClick = neutralButton.onClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = WdsTheme.colors.colorAccent
                            )
                        ) {
                            Text(neutralButton.text)
                        }
                    }
                    
                    // Negative button
                    if (negativeButton != null) {
                        TextButton(
                            onClick = negativeButton.onClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = WdsTheme.colors.colorAccent
                            )
                        ) {
                            Text(negativeButton.text)
                        }
                    }
                    
                    // Positive button
                    Button(
                        onClick = positiveButton.onClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WdsTheme.colors.colorAccent,
                            contentColor = WdsTheme.colors.colorContentOnAccent
                        ),
                        shape = WdsTheme.shapes.single
                    ) {
                        Text(positiveButton.text)
                    }
                }
            }
        }
    }
}
