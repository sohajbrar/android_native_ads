@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.wds.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chatapp.wds.theme.WdsTheme

/**
 * WDS Top Bar – simple string-based API.
 *
 * @param title The title text displayed in the top bar
 * @param onNavigateBack Callback when the back arrow is pressed
 * @param modifier Optional modifier
 * @param subtitle Optional subtitle text displayed below the title
 * @param actions Optional trailing action icons slot
 */
@Composable
fun WDSTopBar(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val colors = WdsTheme.colors
    val typography = WdsTheme.typography

    TopAppBar(
        title = {
            if (subtitle != null) {
                Column {
                    Text(
                        text = title,
                        style = typography.chatHeaderTitle,
                        color = colors.colorContentDefault
                    )
                    Text(
                        text = subtitle,
                        style = typography.body3,
                        color = colors.colorContentDefault
                    )
                }
            } else {
                Text(
                    text = title,
                    style = typography.headline2,
                    color = colors.colorContentDefault
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = colors.colorContentDefault
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.colorSurfaceDefault,
            titleContentColor = colors.colorContentDefault
        ),
        modifier = modifier
    )
}

/**
 * WDS Top Bar – flexible slot-based API for custom title and navigation content.
 *
 * Use this overload when you need full control over the title area
 * (e.g. avatar + title row) or a custom navigation icon (e.g. animated back button).
 *
 * @param titleContent Composable content for the title area
 * @param navigationIcon Composable content for the leading navigation icon
 * @param modifier Optional modifier
 * @param actions Optional trailing action icons slot
 */
@Composable
fun WDSTopBar(
    titleContent: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val colors = WdsTheme.colors

    TopAppBar(
        title = { titleContent() },
        navigationIcon = { navigationIcon() },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.colorSurfaceDefault,
            titleContentColor = colors.colorContentDefault,
            navigationIconContentColor = colors.colorContentDefault,
            actionIconContentColor = colors.colorContentDefault
        ),
        modifier = modifier
    )
}
