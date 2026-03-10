package com.example.chatapp.features.advertise

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.BaseColors
import com.example.chatapp.wds.theme.WdsTheme
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView

@Composable
fun ChooseStatusScreen(
    onNavigateBack: () -> Unit,
    onMediaConfirmed: (Uri) -> Unit
) {
    val context = LocalContext.current
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    var hasPermission by remember { mutableStateOf(false) }
    var selectedUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        @Suppress("DEPRECATION")
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) onNavigateBack()
    }

    LaunchedEffect(Unit) {
        hasPermission = ContextCompat.checkSelfPermission(
            context, permission
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            permissionLauncher.launch(permission)
        }
    }

    val sections = remember(hasPermission) {
        if (hasPermission) {
            groupPhotosByDate(queryDevicePhotos(context, limit = 30))
        } else emptyList()
    }

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Choose status",
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(2.dp)
            ) {
                // Subtitle
                item(span = { GridItemSpan(3) }) {
                    Text(
                        text = "Select up to 10 items or 1 video",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized,
                        modifier = Modifier.padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSingle
                        )
                    )
                }

                // My status header
                item(span = { GridItemSpan(3) }) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSingle
                        )
                    ) {
                        Text(
                            text = "My status",
                            style = typography.body1Emphasized,
                            color = colors.colorContentDefault
                        )
                        Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuarter))
                        Text(
                            text = "Updates can be viewed by others for 24 hours",
                            style = typography.body2,
                            color = colors.colorContentDeemphasized
                        )
                    }
                }

                // Photos grid grouped by date
                sections.forEach { section ->
                    item(span = { GridItemSpan(3) }) {
                        Text(
                            text = section.title,
                            style = typography.body1Emphasized,
                            color = colors.colorContentDefault,
                            modifier = Modifier.padding(
                                start = dimensions.wdsSpacingDouble - 2.dp,
                                top = dimensions.wdsSpacingSinglePlus,
                                bottom = dimensions.wdsSpacingSingle
                            )
                        )
                    }

                    items(
                        count = section.items.size,
                        key = { section.items[it].uri.toString() }
                    ) { index ->
                        val photo = section.items[index]
                        val selectionIndex = selectedUris.indexOf(photo.uri)
                        val isSelected = selectionIndex >= 0

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(1.dp)
                                .clickableWithSound {
                                    selectedUris = if (isSelected) {
                                        selectedUris - photo.uri
                                    } else if (selectedUris.size < 10) {
                                        selectedUris + photo.uri
                                    } else selectedUris
                                }
                        ) {
                            AsyncImage(
                                model = photo.uri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(6.dp)
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(BaseColors.wdsGreen500),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${selectionIndex + 1}",
                                        style = typography.body3Emphasized,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // Bottom spacer for FAB clearance
                item(span = { GridItemSpan(3) }) {
                    Spacer(modifier = Modifier.height(88.dp))
                }
            }

            // Bottom bar with selected thumbnail + confirm FAB
            if (selectedUris.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(colors.colorSurfaceDefault)
                        .navigationBarsPadding()
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSinglePlus
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = selectedUris.last(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    FloatingActionButton(
                        onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onMediaConfirmed(selectedUris.first()) },
                        containerColor = BaseColors.wdsGreen500,
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Confirm selection"
                        )
                    }
                }
            }
        }
    }
}
