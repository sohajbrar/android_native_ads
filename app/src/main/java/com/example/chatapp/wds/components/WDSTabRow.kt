package com.example.chatapp.wds.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.sp
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS Tab Row Component
 *
 * A tab row component that follows the WhatsApp Design System guidelines.
 * Renders a row of tabs with an underline indicator on the selected tab.
 * Tab bar height is 46dp with 15sp font size per WDS spec.
 *
 * @param tabs The list of tab labels to display
 * @param selectedTabIndex The index of the currently selected tab
 * @param onTabSelected Callback invoked with the index when a tab is tapped
 * @param modifier Optional modifier
 */
@Composable
fun WDSTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 46.dp),
        containerColor = colors.colorSurfaceDefault,
        contentColor = colors.colorContentDefault,
        indicator = { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 2.dp,
                    color = colors.colorAlwaysBranded
                )
            }
        },
        divider = {
            WDSDivider()
        }
    ) {
        tabs.forEachIndexed { index, title ->
            val selected = selectedTabIndex == index
            Tab(
                selected = selected,
                onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onTabSelected(index) },
                modifier = Modifier.defaultMinSize(minHeight = 46.dp),
                text = {
                    Text(
                        text = title,
                        style = typography.body1.copy(
                            fontSize = 15.sp,
                            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                        ),
                        color = if (selected) colors.colorContentDefault else colors.colorContentDeemphasized
                    )
                }
            )
        }
    }
}
