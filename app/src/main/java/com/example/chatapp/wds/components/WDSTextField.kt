package com.example.chatapp.wds.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS TextField Type
 *
 * Defines the type/variant of the text field
 */
enum class WDSTextFieldType {
    SINGLE_LINE,    // Single line text input
    MULTI_LINE      // Multi-line text input
}

/**
 * WDS TextField Component
 *
 * A text field component that follows the WhatsApp Design System guidelines.
 * Supports single-line and multi-line input with consistent styling.
 *
 * @param value The current text value
 * @param onValueChange The callback to be invoked when the text changes
 * @param modifier The modifier to be applied to the text field
 * @param enabled Whether the text field is enabled
 * @param readOnly Whether the text field is read-only
 * @param textStyle The text style to be applied to the input text
 * @param label Optional label to display above the text field
 * @param placeholder Optional placeholder text to display when empty
 * @param leadingIcon Optional leading icon composable
 * @param trailingIcon Optional trailing icon composable
 * @param supportingText Optional supporting text to display below the text field
 * @param isError Whether the text field is in an error state
 * @param visualTransformation The visual transformation to apply to the input
 * @param keyboardOptions The keyboard options for the text field
 * @param keyboardActions The keyboard actions for the text field
 * @param singleLine Whether the text field is single line
 * @param maxLines The maximum number of lines for multi-line text fields
 * @param minLines The minimum number of lines for multi-line text fields
 * @param interactionSource The interaction source for the text field
 * @param shape The shape of the text field
 * @param colors The colors to use for the text field
 */
@Composable
fun WDSTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = WdsTheme.typography.body1,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = WdsTheme.shapes.single,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = WdsTheme.colors.colorContentDefault,
        unfocusedTextColor = WdsTheme.colors.colorContentDefault,
        disabledTextColor = WdsTheme.colors.colorContentDeemphasized,
        errorTextColor = WdsTheme.colors.colorContentDefault,
        focusedContainerColor = WdsTheme.colors.colorBackgroundWashInset,
        unfocusedContainerColor = WdsTheme.colors.colorBackgroundWashInset,
        disabledContainerColor = WdsTheme.colors.colorBackgroundWashInset,
        errorContainerColor = WdsTheme.colors.colorBackgroundWashInset,
        cursorColor = WdsTheme.colors.colorAccent,
        errorCursorColor = WdsTheme.colors.colorAccent,
        focusedBorderColor = WdsTheme.colors.colorAccent,
        unfocusedBorderColor = WdsTheme.colors.colorDivider,
        disabledBorderColor = WdsTheme.colors.colorDivider.copy(alpha = 0.38f),
        errorBorderColor = WdsTheme.colors.colorAccent,
        focusedLeadingIconColor = WdsTheme.colors.colorContentDefault,
        unfocusedLeadingIconColor = WdsTheme.colors.colorContentDeemphasized,
        disabledLeadingIconColor = WdsTheme.colors.colorContentDeemphasized.copy(alpha = 0.38f),
        errorLeadingIconColor = WdsTheme.colors.colorContentDefault,
        focusedTrailingIconColor = WdsTheme.colors.colorContentDefault,
        unfocusedTrailingIconColor = WdsTheme.colors.colorContentDeemphasized,
        disabledTrailingIconColor = WdsTheme.colors.colorContentDeemphasized.copy(alpha = 0.38f),
        errorTrailingIconColor = WdsTheme.colors.colorContentDefault,
        focusedLabelColor = WdsTheme.colors.colorAccent,
        unfocusedLabelColor = WdsTheme.colors.colorContentDeemphasized,
        disabledLabelColor = WdsTheme.colors.colorContentDeemphasized.copy(alpha = 0.38f),
        errorLabelColor = WdsTheme.colors.colorAccent,
        focusedPlaceholderColor = WdsTheme.colors.colorContentDeemphasized,
        unfocusedPlaceholderColor = WdsTheme.colors.colorContentDeemphasized,
        disabledPlaceholderColor = WdsTheme.colors.colorContentDeemphasized.copy(alpha = 0.38f),
        errorPlaceholderColor = WdsTheme.colors.colorContentDeemphasized,
        focusedSupportingTextColor = WdsTheme.colors.colorContentDeemphasized,
        unfocusedSupportingTextColor = WdsTheme.colors.colorContentDeemphasized,
        disabledSupportingTextColor = WdsTheme.colors.colorContentDeemphasized.copy(alpha = 0.38f),
        errorSupportingTextColor = WdsTheme.colors.colorAccent
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label?.let { { Text(it, style = WdsTheme.typography.body2) } },
        placeholder = placeholder?.let { { Text(it, style = WdsTheme.typography.body1) } },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText?.let { { Text(it, style = WdsTheme.typography.body3) } },
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )
}

/**
 * Convenience function for a single-line WDS text field
 */
@Composable
fun WDSSingleLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    WDSTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true
    )
}

/**
 * Convenience function for a multi-line WDS text field
 */
@Composable
fun WDSMultiLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    minLines: Int = 3,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    WDSTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        supportingText = supportingText,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = false,
        minLines = minLines,
        maxLines = maxLines
    )
}

