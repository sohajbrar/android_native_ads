@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.chatapp.R
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView
import java.io.File

@Composable
fun MediaSelectionScreen(
    onNavigateBack: () -> Unit,
    onNextClick: (selectedMediaUri: String?) -> Unit,
    onChooseStatus: () -> Unit = {},
    onChooseCatalog: () -> Unit = {}
) {
    val context = LocalContext.current
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    var showMediaPicker by rememberSaveable { mutableStateOf(false) }

    // Camera capture support
    var capturedPhotoUri by rememberSaveable { mutableStateOf<String?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && capturedPhotoUri != null) {
            onNextClick(capturedPhotoUri)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val photoFile = File(
                context.cacheDir,
                "ad_photo_${System.currentTimeMillis()}.jpg"
            )
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                photoFile
            )
            capturedPhotoUri = uri.toString()
            cameraLauncher.launch(uri)
        }
    }

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Advertise",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK) }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "More options",
                            tint = colors.colorContentDefault
                        )
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.colorSurfaceDefault)
                    .navigationBarsPadding()
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSinglePlus
                    )
            ) {
                WDSButton(
                    onClick = {
                        when (selectedIndex) {
                            0 -> onNextClick("https://images.unsplash.com/photo-1459411552884-841db9b3cc2a?w=800&h=800&fit=crop")
                            1 -> onNextClick("https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=800&h=800&fit=crop")
                            2 -> onChooseStatus()
                            3 -> showMediaPicker = true
                            4 -> {
                                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                            5 -> onChooseCatalog()
                            else -> onNextClick(null)
                        }
                    },
                    text = "Next",
                    variant = WDSButtonVariant.FILLED,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Text(
                    text = "Create ad from",
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault,
                    modifier = Modifier.padding(
                        start = dimensions.wdsSpacingDouble,
                        top = dimensions.wdsSpacingDouble,
                        bottom = dimensions.wdsSpacingSingle
                    )
                )
            }

            item {
                MediaOptionRow(
                    avatarContent = {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1459411552884-841db9b3cc2a?w=200&h=200&fit=crop",
                            contentDescription = "Catalog item",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    },
                    title = "Spring bestsellers",
                    subtitle = "Your newest catalog item",
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 }
                )
            }

            item {
                MediaOptionRow(
                    avatarContent = {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=200&h=200&fit=crop",
                            contentDescription = "Lucky Shrub",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    },
                    title = "Lucky Shrub",
                    subtitle = "Your business",
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 }
                )
            }

            item {
                MediaOptionRow(
                    avatarContent = {
                        Box(
                            modifier = Modifier.size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_notes),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = colors.colorContentDeemphasized
                            )
                        }
                    },
                    title = "Your status",
                    subtitle = null,
                    selected = selectedIndex == 2,
                    onClick = { selectedIndex = 2 }
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))
                Text(
                    text = "Other options",
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault,
                    modifier = Modifier.padding(
                        start = dimensions.wdsSpacingDouble,
                        top = dimensions.wdsSpacingSingle,
                        bottom = dimensions.wdsSpacingSingle
                    )
                )
            }

            item {
                MediaOptionRow(
                    avatarContent = {
                        IconCircle(icon = Icons.Outlined.Image)
                    },
                    title = "Upload photos or videos",
                    subtitle = "from your gallery",
                    selected = selectedIndex == 3,
                    onClick = { selectedIndex = 3 }
                )
            }

            item {
                MediaOptionRow(
                    avatarContent = {
                        IconCircle(icon = Icons.Outlined.CameraAlt)
                    },
                    title = "Take a new photo or video",
                    subtitle = null,
                    selected = selectedIndex == 4,
                    onClick = { selectedIndex = 4 }
                )
            }

            item {
                MediaOptionRow(
                    avatarContent = {
                        IconCircle(icon = Icons.Outlined.GridOn)
                    },
                    title = "Your catalog",
                    subtitle = "0 items",
                    selected = selectedIndex == 5,
                    onClick = { selectedIndex = 5 }
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableWithSound { }
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSinglePlus
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSinglePlus)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.HelpOutline,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = colors.colorContentDeemphasized
                    )
                    Text(
                        text = "About ad media",
                        style = typography.body1,
                        color = colors.colorContentDefault
                    )
                }
            }
        }
    }

    if (showMediaPicker) {
        MediaPickerBottomSheet(
            onDismiss = { showMediaPicker = false },
            onMediaConfirmed = { uri ->
                showMediaPicker = false
                onNextClick(uri.toString())
            }
        )
    }
}

@Composable
private fun IconCircle(icon: ImageVector) {
    val colors = WdsTheme.colors
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = colors.colorContentDeemphasized
        )
    }
}

@Composable
private fun MediaOptionRow(
    avatarContent: @Composable () -> Unit,
    title: String,
    subtitle: String?,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithSound(onClick = onClick)
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        avatarContent()

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = typography.body1,
                color = colors.colorContentDefault
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = typography.body2,
                    color = colors.colorContentDeemphasized
                )
            }
        }

        RadioButton(
            selected = selected,
            onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onClick() },
            colors = RadioButtonDefaults.colors(
                selectedColor = colors.colorAccent,
                unselectedColor = colors.colorOutlineDefault
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaSelectionScreenPreview() {
    WdsTheme(darkTheme = false) {
        MediaSelectionScreen(
            onNavigateBack = {},
            onNextClick = { _ -> }
        )
    }
}
