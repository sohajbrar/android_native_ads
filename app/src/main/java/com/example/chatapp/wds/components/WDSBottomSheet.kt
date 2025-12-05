package com.example.chatapp.wds.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.tokens.BaseDimensions
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS Bottom Sheet Item
 *
 * Represents a single item in the bottom sheet list with an icon and text.
 *
 * @param icon The icon to display
 * @param text The text to display
 * @param onClick The callback when the item is clicked
 */
data class WDSBottomSheetItem(
    val icon: ImageVector,
    val text: String,
    val onClick: () -> Unit = {}
)

/**
 * WDS Bottom Sheet Component
 *
 * A modal bottom sheet that follows the WhatsApp Design System guidelines.
 * Animates in from the bottom and can be dismissed by dragging down or tapping outside.
 *
 * Layout structure (top to bottom):
 * - Image (optional)
 * - Headline
 * - Body text (optional)
 * - List of items with icons (optional)
 * - Primary action button
 * - Secondary action button (optional)
 *
 * @param onDismissRequest Callback when the bottom sheet is dismissed
 * @param headline The main headline text
 * @param modifier The modifier to be applied to the bottom sheet
 * @param sheetState The state of the bottom sheet
 * @param image Optional image painter to display at the top
 * @param bodyText Optional body text below the headline
 * @param items Optional list of items with icons
 * @param primaryButtonText Text for the primary action button
 * @param onPrimaryClick Callback for primary button click
 * @param secondaryButtonText Optional text for the secondary action button
 * @param onSecondaryClick Optional callback for secondary button click
 * @param containerColor The background color of the bottom sheet
 * @param contentColor The default content color for text
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WDSBottomSheet(
    onDismissRequest: () -> Unit,
    headline: String,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    image: Painter? = null,
    bodyText: String? = null,
    items: List<WDSBottomSheetItem>? = null,
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    secondaryButtonText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    containerColor: Color = WdsTheme.colors.colorSurfaceElevatedDefault,
    contentColor: Color = WdsTheme.colors.colorContentDefault
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(
            topStart = BaseDimensions.wdsCornerRadiusTriplePlus,
            topEnd = BaseDimensions.wdsCornerRadiusTriplePlus,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        )
    ) {
        WDSBottomSheetContent(
            image = image,
            headline = headline,
            bodyText = bodyText,
            items = items,
            primaryButtonText = primaryButtonText,
            onPrimaryClick = onPrimaryClick,
            secondaryButtonText = secondaryButtonText,
            onSecondaryClick = onSecondaryClick
        )
    }
}

/**
 * Internal content layout for the bottom sheet
 */
@Composable
private fun WDSBottomSheetContent(
    image: Painter?,
    headline: String,
    bodyText: String?,
    items: List<WDSBottomSheetItem>?,
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    secondaryButtonText: String?,
    onSecondaryClick: (() -> Unit)?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingTriple)
            .padding(bottom = WdsTheme.dimensions.wdsSpacingDouble),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top spacing
        Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingTriple))
        
        // Image (240x118 dp)
        image?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = Modifier
                    .width(240.dp)
                    .height(118.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingTriple))

        // Headline
        Text(
            text = headline,
            style = WdsTheme.typography.headline1,
            color = WdsTheme.colors.colorContentDefault,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = WdsTheme.dimensions.wdsSpacingDouble)
        )

        // Body Text
        bodyText?.let {
            Text(
                text = it,
                style = WdsTheme.typography.body1,
                color = WdsTheme.colors.colorContentDeemphasized,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = WdsTheme.dimensions.wdsSpacingTriple)
            )
        }

        // Items List
        items?.let { itemsList ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = WdsTheme.dimensions.wdsSpacingSinglePlus)
                    .padding(bottom = WdsTheme.dimensions.wdsSpacingTriple),
                verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingDouble)
            ) {
                itemsList.forEach { item ->
                    WDSBottomSheetListItem(item = item)
                }
            }
        }

        // Primary Button
        WDSButton(
            onClick = onPrimaryClick,
            text = primaryButtonText,
            variant = WDSButtonVariant.FILLED,
            modifier = Modifier.fillMaxWidth()
        )

        // Secondary Button
        if (secondaryButtonText != null && onSecondaryClick != null) {
            Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingSingle))
            WDSButton(
                onClick = onSecondaryClick,
                text = secondaryButtonText,
                variant = WDSButtonVariant.BORDERLESS,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Individual list item in the bottom sheet
 */
@Composable
private fun WDSBottomSheetListItem(
    item: WDSBottomSheetItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = WdsTheme.colors.colorContentDeemphasized,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingDouble))
        
        Text(
            text = item.text,
            style = WdsTheme.typography.body1,
            color = WdsTheme.colors.colorContentDefault
        )
    }
}

