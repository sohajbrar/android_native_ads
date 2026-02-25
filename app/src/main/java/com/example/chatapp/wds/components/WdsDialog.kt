package com.example.chatapp.wds.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.chatapp.wds.theme.WdsTheme

data class WdsDialogButton(
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun WdsDialog(
    positiveButton: WdsDialogButton,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    icon: ImageVector? = null,
    negativeButton: WdsDialogButton? = null,
    neutralButton: WdsDialogButton? = null
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier.width(280.dp),
            shape = RoundedCornerShape(dimensions.wdsSpacingTriple),
            color = colors.colorSurfaceElevatedDefault
        ) {
            Column(
                modifier = Modifier.padding(dimensions.wdsSpacingTriple)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.colorAccent,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = dimensions.wdsSpacingDouble)
                    )
                }

                if (title != null) {
                    Text(
                        text = title,
                        style = typography.headline1,
                        color = colors.colorContentDefault,
                        modifier = Modifier.padding(bottom = dimensions.wdsSpacingSingle)
                    )
                }

                if (!message.isNullOrEmpty()) {
                    Text(
                        text = message,
                        style = typography.body2,
                        color = colors.colorContentDeemphasized,
                        modifier = Modifier.padding(bottom = dimensions.wdsSpacingTriple)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (neutralButton != null) {
                        TextButton(onClick = neutralButton.onClick) {
                            Text(
                                text = neutralButton.text,
                                style = typography.body2Emphasized,
                                color = colors.colorContentActionEmphasized
                            )
                        }
                    }

                    if (negativeButton != null) {
                        TextButton(onClick = negativeButton.onClick) {
                            Text(
                                text = negativeButton.text,
                                style = typography.body2Emphasized,
                                color = colors.colorContentActionEmphasized
                            )
                        }
                    }

                    TextButton(onClick = positiveButton.onClick) {
                        Text(
                            text = positiveButton.text,
                            style = typography.body2Emphasized,
                            color = colors.colorContentActionEmphasized
                        )
                    }
                }
            }
        }
    }
}
