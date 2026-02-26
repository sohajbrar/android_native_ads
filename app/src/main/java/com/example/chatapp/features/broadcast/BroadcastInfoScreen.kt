package com.example.chatapp.features.broadcast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.data.local.entity.UserEntity
import com.example.chatapp.wds.components.WdsComingSoonDialog
import com.example.chatapp.wds.components.WdsDialog
import com.example.chatapp.wds.components.WdsDialogButton
import com.example.chatapp.wds.theme.WdsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastInfoScreen(
    conversationId: String,
    title: String,
    recipientCount: Int,
    linkedListCount: Int,
    onBackClick: () -> Unit,
    onDeleteBroadcast: () -> Unit = {},
    viewModel: BroadcastInfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(conversationId) {
        viewModel.loadBroadcastInfo(conversationId)
    }

    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showComingSoonDialog by remember { mutableStateOf(false) }

    if (showComingSoonDialog) {
        WdsComingSoonDialog(onDismissRequest = { showComingSoonDialog = false })
    }

    if (showDeleteDialog) {
        WdsDialog(
            title = "Delete this broadcast?",
            negativeButton = WdsDialogButton(
                text = "Cancel",
                onClick = { showDeleteDialog = false }
            ),
            positiveButton = WdsDialogButton(
                text = "Delete",
                onClick = {
                    showDeleteDialog = false
                    viewModel.deleteBroadcast()
                    onDeleteBroadcast()
                }
            ),
            onDismissRequest = { showDeleteDialog = false }
        )
    }

    val isListBased = linkedListCount > 0
    val createdAtFormatted = remember(uiState.conversation?.createdAt) {
        val timestamp = uiState.conversation?.createdAt ?: System.currentTimeMillis()
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        "Created today at ${sdf.format(Date(timestamp))}"
    }

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.colorContentDefault
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showComingSoonDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More options",
                            tint = colors.colorContentDefault
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.colorSurfaceDefault
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header: Avatar + Title + Subtitle + Created
            item(key = "header") {
                BroadcastInfoHeader(
                    title = title,
                    recipientCount = recipientCount,
                    createdAt = createdAtFormatted
                )
            }

            // Section divider before info rows
            item(key = "divider_0") {
                BroadcastSectionDivider()
            }

            // Automatic updates section (only for list-based)
            if (isListBased) {
                item(key = "auto_updates") {
                    AutomaticUpdatesRow(onClick = { showComingSoonDialog = true })
                }
            }

            // Encryption section
            item(key = "encryption") {
                EncryptionRow(onClick = { showComingSoonDialog = true })
            }

            // Section divider
            item(key = "divider_1") {
                BroadcastSectionDivider()
            }

            // Recipients header
            item(key = "recipients_header") {
                val headerText = if (isListBased) {
                    val listLabel = if (linkedListCount == 1) "1 list linked" else "$linkedListCount lists linked"
                    "$listLabel · $recipientCount recipients"
                } else {
                    "$recipientCount recipients"
                }

                Text(
                    text = headerText,
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    modifier = Modifier.padding(
                        start = dimensions.wdsSpacingDouble,
                        end = dimensions.wdsSpacingTriple,
                        top = dimensions.wdsSpacingDouble,
                        bottom = dimensions.wdsSpacingSingle
                    )
                )
            }

            // Edit recipients row
            item(key = "edit_recipients") {
                EditRecipientsRow(onClick = { showComingSoonDialog = true })
            }

            if (isListBased) {
                // List-based: show linked lists
                item(key = "linked_list_new_order") {
                    LinkedListRow(
                        name = title,
                        recipientCount = recipientCount,
                        dotColor = Color(0xFFFFD429),
                        onClick = { showComingSoonDialog = true }
                    )
                }
            } else {
                // Audience-based: show individual recipients
                items(
                    items = uiState.recipients,
                    key = { it.userId }
                ) { user ->
                    RecipientRow(user = user, onClick = { showComingSoonDialog = true })
                }
            }

            // Section divider before delete
            item(key = "divider_2") {
                BroadcastSectionDivider()
            }

            // Delete broadcast
            item(key = "delete") {
                DeleteBroadcastRow(onClick = { showDeleteDialog = true })
            }

            // Bottom spacing
            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuad))
            }
        }
    }
}

@Composable
private fun BroadcastInfoHeader(
    title: String,
    recipientCount: Int,
    createdAt: String
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensions.wdsSpacingDouble),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = colors.colorBroadcastAvatar,
            modifier = Modifier.size(dimensions.wdsAvatarXXL)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_business_broadcast),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = colors.colorContentOnAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingDouble))

        Text(
            text = title,
            style = typography.headline1,
            color = colors.colorContentDefault,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = dimensions.wdsSpacingQuad)
        )

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingHalf))

        Text(
            text = "Audience · $recipientCount recipients",
            style = typography.body1,
            color = colors.colorContentDeemphasized,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimensions.wdsSpacingSingle))

        Surface(
            shape = WdsTheme.shapes.circle,
            color = colors.colorSurfaceHighlight
        ) {
            Text(
                text = createdAt,
                style = typography.body3,
                color = colors.colorContentDeemphasized,
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingSinglePlus,
                    vertical = dimensions.wdsSpacingHalf
                )
            )
        }
    }
}

@Composable
private fun AutomaticUpdatesRow(onClick: () -> Unit = {}) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val autoUpdateIcon = remember {
        ImageVector.Builder(
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 4f)
                verticalLineTo(1f)
                lineTo(8f, 5f)
                lineTo(12f, 9f)
                verticalLineTo(6f)
                curveTo(15.31f, 6f, 18f, 8.69f, 18f, 12f)
                curveTo(18f, 13.01f, 17.75f, 13.97f, 17.3f, 14.8f)
                lineTo(18.76f, 16.26f)
                curveTo(19.54f, 15.03f, 20f, 13.57f, 20f, 12f)
                curveTo(20f, 7.58f, 16.42f, 4f, 12f, 4f)
                close()
                moveTo(12f, 18f)
                curveTo(8.69f, 18f, 6f, 15.31f, 6f, 12f)
                curveTo(6f, 10.99f, 6.25f, 10.03f, 6.7f, 9.2f)
                lineTo(5.24f, 7.74f)
                curveTo(4.46f, 8.97f, 4f, 10.43f, 4f, 12f)
                curveTo(4f, 16.42f, 7.58f, 20f, 12f, 20f)
                verticalLineTo(23f)
                lineTo(16f, 19f)
                lineTo(12f, 15f)
                verticalLineTo(18f)
                close()
            }
        }.build()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = dimensions.wdsSpacingTriple,
                vertical = dimensions.wdsSpacingDouble
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = autoUpdateIcon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = colors.colorContentDeemphasized
        )

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingTriple))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Automatic updates",
                style = typography.body1,
                color = colors.colorContentDefault
            )
            Text(
                text = buildAnnotatedString {
                    append("Audiences update when you change lists linked to them. ")
                    withStyle(
                        typography.body2InlineLink.toSpanStyle().copy(color = colors.colorPositive)
                    ) {
                        append("Learn more")
                    }
                },
                style = typography.body2,
                color = colors.colorContentDeemphasized
            )
        }
    }
}

@Composable
private fun EncryptionRow(onClick: () -> Unit = {}) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    val encryptionIcon = remember {
        ImageVector.Builder(
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(6f, 22f)
                curveTo(5.45f, 22f, 4.97917f, 21.8042f, 4.5875f, 21.4125f)
                curveTo(4.19583f, 21.0208f, 4f, 20.55f, 4f, 20f)
                verticalLineTo(10f)
                curveTo(4f, 9.45f, 4.19583f, 8.97917f, 4.5875f, 8.5875f)
                curveTo(4.97917f, 8.19583f, 5.45f, 8f, 6f, 8f)
                horizontalLineTo(7f)
                verticalLineTo(6f)
                curveTo(7f, 4.61667f, 7.4875f, 3.4375f, 8.4625f, 2.4625f)
                curveTo(9.4375f, 1.4875f, 10.6167f, 1f, 12f, 1f)
                curveTo(13.3833f, 1f, 14.5625f, 1.4875f, 15.5375f, 2.4625f)
                curveTo(16.5125f, 3.4375f, 17f, 4.61667f, 17f, 6f)
                verticalLineTo(8f)
                horizontalLineTo(18f)
                curveTo(18.55f, 8f, 19.0208f, 8.19583f, 19.4125f, 8.5875f)
                curveTo(19.8042f, 8.97917f, 20f, 9.45f, 20f, 10f)
                verticalLineTo(20f)
                curveTo(20f, 20.55f, 19.8042f, 21.0208f, 19.4125f, 21.4125f)
                curveTo(19.0208f, 21.8042f, 18.55f, 22f, 18f, 22f)
                horizontalLineTo(6f)
                close()
                moveTo(6f, 20f)
                horizontalLineTo(18f)
                verticalLineTo(10f)
                horizontalLineTo(6f)
                verticalLineTo(20f)
                close()
                moveTo(12f, 17f)
                curveTo(12.55f, 17f, 13.0208f, 16.8042f, 13.4125f, 16.4125f)
                curveTo(13.8042f, 16.0208f, 14f, 15.55f, 14f, 15f)
                curveTo(14f, 14.45f, 13.8042f, 13.9792f, 13.4125f, 13.5875f)
                curveTo(13.0208f, 13.1958f, 12.55f, 13f, 12f, 13f)
                curveTo(11.45f, 13f, 10.9792f, 13.1958f, 10.5875f, 13.5875f)
                curveTo(10.1958f, 13.9792f, 10f, 14.45f, 10f, 15f)
                curveTo(10f, 15.55f, 10.1958f, 16.0208f, 10.5875f, 16.4125f)
                curveTo(10.9792f, 16.8042f, 11.45f, 17f, 12f, 17f)
                close()
                moveTo(9f, 8f)
                horizontalLineTo(15f)
                verticalLineTo(6f)
                curveTo(15f, 5.16667f, 14.7083f, 4.45833f, 14.125f, 3.875f)
                curveTo(13.5417f, 3.29167f, 12.8333f, 3f, 12f, 3f)
                curveTo(11.1667f, 3f, 10.4583f, 3.29167f, 9.875f, 3.875f)
                curveTo(9.29167f, 4.45833f, 9f, 5.16667f, 9f, 6f)
                verticalLineTo(8f)
                close()
            }
        }.build()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = dimensions.wdsSpacingTriple,
                vertical = dimensions.wdsSpacingDouble
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = encryptionIcon,
            contentDescription = "Encryption",
            modifier = Modifier.size(24.dp),
            tint = colors.colorContentDeemphasized
        )

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingTriple))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Encryption",
                style = typography.body1,
                color = colors.colorContentDefault
            )
            Text(
                text = buildAnnotatedString {
                    append("Messages and calls are end-to-end encrypted. ")
                    withStyle(
                        typography.body2InlineLink.toSpanStyle().copy(color = colors.colorPositive)
                    ) {
                        append("Learn more")
                    }
                },
                style = typography.body2,
                color = colors.colorContentDeemphasized
            )
        }
    }
}

@Composable
private fun BroadcastSectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .background(WdsTheme.colors.colorSurfaceEmphasized)
    )
}

@Composable
private fun EditRecipientsRow(onClick: () -> Unit) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                start = dimensions.wdsSpacingDouble,
                end = dimensions.wdsSpacingTriple,
                top = dimensions.wdsSpacingSinglePlus,
                bottom = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = colors.colorAccent,
            modifier = Modifier.size(dimensions.wdsAvatarMedium)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.PersonAdd,
                    contentDescription = null,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium),
                    tint = colors.colorContentOnAccent
                )
            }
        }

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Text(
            text = "Edit recipients",
            style = typography.body1,
            color = colors.colorContentDefault
        )
    }
}

@Composable
private fun LinkedListRow(
    name: String,
    recipientCount: Int,
    dotColor: Color,
    onClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                start = dimensions.wdsSpacingDouble,
                end = dimensions.wdsSpacingTriple,
                top = dimensions.wdsSpacingSinglePlus,
                bottom = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.wdsAvatarMedium),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = typography.body1,
                color = colors.colorContentDefault,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$recipientCount recipients",
                style = typography.body2,
                color = colors.colorContentDeemphasized
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = colors.colorContentDeemphasized,
            modifier = Modifier.size(dimensions.wdsIconSizeMedium)
        )
    }
}

@Composable
private fun RecipientRow(user: UserEntity, onClick: () -> Unit = {}) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                start = dimensions.wdsSpacingDouble,
                end = dimensions.wdsSpacingTriple,
                top = dimensions.wdsSpacingSinglePlus,
                bottom = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(dimensions.wdsAvatarMedium)
                .clip(CircleShape)
        ) {
            when {
                user.avatarUrl != null && user.avatarUrl.startsWith("drawable://") -> {
                    val drawableResId = when (user.avatarUrl.removePrefix("drawable://")) {
                        "avatar_alice" -> R.drawable.avatar_alice
                        "avatar_anna_soe" -> R.drawable.avatar_anna_soe
                        "avatar_anika_chavan" -> R.drawable.avatar_anika_chavan
                        "avatar_ayesha" -> R.drawable.avatar_ayesha
                        "avatar_jordan" -> R.drawable.avatar_jordan
                        "avatar_faith" -> R.drawable.avatar_faith
                        "avatar_maria" -> R.drawable.avatar_maria
                        "avatar_anika" -> R.drawable.avatar_anika
                        "avatar_gerald" -> R.drawable.avatar_gerald
                        "avatar_yuna" -> R.drawable.avatar_yuna
                        else -> null
                    }

                    if (drawableResId != null) {
                        Image(
                            painter = painterResource(id = drawableResId),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        AvatarPlaceholder(user.displayName)
                    }
                }
                user.avatarUrl != null && user.avatarUrl.isNotEmpty() -> {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    AvatarPlaceholder(user.displayName)
                }
            }
        }

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingDouble))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.displayName,
                style = typography.body1,
                color = colors.colorContentDefault,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            user.statusMessage?.let { status ->
                Text(
                    text = status,
                    style = typography.body2,
                    color = colors.colorContentDeemphasized,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AvatarPlaceholder(displayName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WdsTheme.colors.colorSurfaceHighlight),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayName.firstOrNull()?.uppercase() ?: "?",
            style = WdsTheme.typography.body1,
            color = WdsTheme.colors.colorContentDeemphasized
        )
    }
}

@Composable
private fun DeleteBroadcastRow(onClick: () -> Unit) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = dimensions.wdsSpacingTriple,
                vertical = dimensions.wdsSpacingDouble
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = colors.colorNegative
        )

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingTriple))

        Text(
            text = "Delete broadcast",
            style = typography.body1,
            color = colors.colorNegative
        )
    }
}

@Preview(showBackground = true, name = "Broadcast Info - List Based")
@Composable
private fun BroadcastInfoScreenPreviewList() {
    WdsTheme(darkTheme = false) {
        BroadcastInfoScreen(
            conversationId = "preview",
            title = "New order",
            recipientCount = 303,
            linkedListCount = 1,
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Broadcast Info - Audience Based")
@Composable
private fun BroadcastInfoScreenPreviewAudience() {
    WdsTheme(darkTheme = false) {
        BroadcastInfoScreen(
            conversationId = "preview",
            title = "Manel, Matt, Shelly...",
            recipientCount = 303,
            linkedListCount = 0,
            onBackClick = {}
        )
    }
}
