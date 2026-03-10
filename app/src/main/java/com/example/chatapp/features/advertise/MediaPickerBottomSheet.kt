@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.chatapp.wds.theme.BaseColors
import com.example.chatapp.wds.theme.WdsTheme
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView

data class MediaItem(
    val uri: Uri,
    val dateAdded: Long,
    val displayName: String
)

data class MediaSection(
    val title: String,
    val items: List<MediaItem>
)

@Composable
fun MediaPickerBottomSheet(
    onDismiss: () -> Unit,
    onMediaConfirmed: (Uri) -> Unit
) {
    val context = LocalContext.current
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    var hasPermission by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
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
        if (!granted) onDismiss()
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
            groupPhotosByDate(queryDevicePhotos(context))
        } else {
            emptyList()
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.colorSurfaceDefault,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.92f)) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header: X | "N selected" (centered)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = colors.colorContentDefault
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${selectedUris.size} selected",
                        style = typography.headline2,
                        color = colors.colorContentDefault
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.size(48.dp))
                }

                // Tabs: Recent | Gallery
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = colors.colorSurfaceDefault,
                    contentColor = colors.colorContentDefault,
                    indicator = { tabPositions ->
                        if (tabPositions.isNotEmpty()) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = colors.colorContentDefault
                            )
                        }
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "Recent",
                                style = if (selectedTab == 0) typography.body1Emphasized
                                else typography.body1
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Gallery",
                                style = if (selectedTab == 1) typography.body1Emphasized
                                else typography.body1
                            )
                        }
                    )
                }

                // Photo grid with date section headers
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(2.dp)
                ) {
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
                                        } else {
                                            selectedUris + photo.uri
                                        }
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
                }
            }

            // Green confirm FAB
            if (selectedUris.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onMediaConfirmed(selectedUris.first()) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(dimensions.wdsSpacingDouble)
                        .navigationBarsPadding(),
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

internal fun queryDevicePhotos(context: Context, limit: Int = 200): List<MediaItem> {
    val photos = mutableListOf<MediaItem>()
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED
    )
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->
        val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

        var count = 0
        while (cursor.moveToNext() && count < limit) {
            val id = cursor.getLong(idCol)
            val name = cursor.getString(nameCol) ?: "unknown"
            val date = cursor.getLong(dateCol)
            val uri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
            )
            photos.add(MediaItem(uri, date, name))
            count++
        }
    }

    return photos
}

internal fun groupPhotosByDate(photos: List<MediaItem>): List<MediaSection> {
    if (photos.isEmpty()) return emptyList()

    val nowSec = System.currentTimeMillis() / 1000
    val todayStart = (nowSec / 86400) * 86400
    val weekAgo = todayStart - 7 * 86400
    val monthAgo = todayStart - 30 * 86400

    val sections = mutableListOf<MediaSection>()

    val recent = photos.filter { it.dateAdded >= todayStart }
    val lastWeek = photos.filter { it.dateAdded in weekAgo until todayStart }
    val lastMonth = photos.filter { it.dateAdded in monthAgo until weekAgo }
    val older = photos.filter { it.dateAdded < monthAgo }

    if (recent.isNotEmpty()) sections.add(MediaSection("Recent", recent))
    if (lastWeek.isNotEmpty()) sections.add(MediaSection("Last week", lastWeek))
    if (lastMonth.isNotEmpty()) sections.add(MediaSection("Last month", lastMonth))
    if (older.isNotEmpty()) sections.add(MediaSection("Older", older))

    return sections
}
