@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.updates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import android.view.SoundEffectConstants
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.chatapp.R
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun MyStatusScreen(
    onBack: () -> Unit = {},
    onStatusClick: (mediaIndex: Int) -> Unit = {}
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val myStatus = remember { StatusData.myStatus }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My status",
                        style = typography.headline2,
                        color = colors.colorContentDefault
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.colorContentDefault
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "More options",
                            tint = colors.colorContentDefault
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.colorSurfaceDefault
                )
            )
        },
        floatingActionButton = {
            MyStatusFabs()
        },
        containerColor = colors.colorSurfaceDefault
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            itemsIndexed(myStatus.media) { index, media ->
                MyStatusRow(
                    myStatus = myStatus,
                    media = media,
                    onClick = { onStatusClick(index) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))
                EncryptionNotice()
            }
        }
    }
}

@Composable
private fun MyStatusRow(
    myStatus: StatusItem,
    media: StatusMediaItem,
    onClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val context = LocalContext.current
    val view = LocalView.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onClick()
            }
            .padding(horizontal = dimensions.wdsSpacingDouble, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(media.imageUrl)
                .crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = "Status",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF78909C))
        )

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${media.viewCount} views",
                    style = typography.body1Emphasized,
                    color = colors.colorContentDefault
                )
                if (media.hasLikedView) {
                    Text(
                        text = "· 💚",
                        style = typography.body1,
                        color = colors.colorContentDefault
                    )
                }
            }
            Text(
                text = media.timeAgo,
                style = typography.body2,
                color = colors.colorContentDeemphasized
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More options",
                tint = colors.colorContentDeemphasized
            )
        }
    }
}

@Composable
private fun EncryptionNotice() {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.wdsSpacingTriple),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Outlined.Lock,
            contentDescription = null,
            tint = colors.colorContentDeemphasized,
            modifier = Modifier.size(14.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = buildAnnotatedString {
                append("Your status updates are ")
                withStyle(SpanStyle(color = colors.colorAccent)) {
                    append("end-to-end encrypted")
                }
                append(". They will disappear after 24 hours.")
            },
            style = typography.body3,
            color = colors.colorContentDeemphasized
        )
    }
}

@Composable
private fun MyStatusFabs() {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingDouble)
    ) {
        Surface(
            onClick = { },
            color = colors.colorSurfaceEmphasized,
            shape = WdsTheme.shapes.singlePlus,
            shadowElevation = 6.dp,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit status",
                    tint = colors.colorContentDefault,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                )
            }
        }

        FloatingActionButton(
            onClick = { },
            containerColor = colors.colorAccent,
            contentColor = colors.colorContentOnAccent,
            shape = WdsTheme.shapes.double
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_a_photo),
                contentDescription = "Add photo status",
                modifier = Modifier.size(dimensions.wdsIconSizeMedium)
            )
        }
    }
}
