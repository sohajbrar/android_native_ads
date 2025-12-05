package com.example.chatapp.features.chatinfo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.data.local.entity.UserEntity
import com.example.chatapp.data.local.entity.ConversationParticipantEntity
import com.example.chatapp.data.local.entity.ParticipantRole
import com.example.chatapp.wds.theme.WdsTheme

@Composable
fun GroupParticipantsSection(
    participants: List<UserEntity>,
    participantRoles: Map<String, ConversationParticipantEntity>,
    currentUserId: String,
    onAddParticipant: () -> Unit,
    onInviteViaLink: () -> Unit,
    onAddToCommunity: () -> Unit,
    onParticipantClick: (UserEntity) -> Unit,
    onMakeAdmin: (String) -> Unit,
    onRemoveAdmin: (String) -> Unit,
    onRemoveParticipant: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingSingle, vertical = WdsTheme.dimensions.wdsSpacingHalf),
        colors = CardDefaults.cardColors(containerColor = WdsTheme.colors.colorSurfaceDefault),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Add group to a community
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAddToCommunity() }
                    .padding(start = WdsTheme.dimensions.wdsSpacingSingle, end = WdsTheme.dimensions.wdsSpacingDouble, top = WdsTheme.dimensions.wdsSpacingSinglePlus, bottom = WdsTheme.dimensions.wdsSpacingSinglePlus),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon container with rounded square shape
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(WdsTheme.shapes.single)
                        .background(WdsTheme.colors.colorAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = communityIcon,
                        contentDescription = "Community",
                        modifier = Modifier.size(24.dp),
                        tint = WdsTheme.colors.colorSurfaceDefault
                    )
                }
                Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingSingle))

                // Text content
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingQuarter)
                ) {
                    Text(
                        text = "Add group to a community",
                        style = WdsTheme.typography.body1,
                        color = WdsTheme.colors.colorContentDefault
                    )
                    Text(
                        text = "Bring members together in topic-based groups",
                        style = WdsTheme.typography.body2,
                        color = WdsTheme.colors.colorContentDeemphasized
                    )
                }
            }

            // Section divider
            SectionDivider()

            // Participants header
            Text(
                text = "${participants.size} members",
                style = WdsTheme.typography.body2Emphasized,
                color = WdsTheme.colors.colorContentDeemphasized,
                modifier = Modifier.padding(start = WdsTheme.dimensions.wdsSpacingSingle, end = WdsTheme.dimensions.wdsSpacingDouble, top = WdsTheme.dimensions.wdsSpacingSinglePlus, bottom = WdsTheme.dimensions.wdsSpacingSinglePlus)
            )

            // Add members
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAddParticipant() }
                    .padding(start = WdsTheme.dimensions.wdsSpacingSingle, end = WdsTheme.dimensions.wdsSpacingDouble, top = WdsTheme.dimensions.wdsSpacingDouble, bottom = WdsTheme.dimensions.wdsSpacingDouble),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(WdsTheme.colors.colorAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Add member",
                        modifier = Modifier.size(20.dp),
                        tint = WdsTheme.colors.colorSurfaceDefault
                    )
                }
                Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingSingle))
                Text(
                    text = "Add members",
                    style = WdsTheme.typography.body1,
                    color = WdsTheme.colors.colorContentDefault
                )
            }

            // Invite via link
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onInviteViaLink() }
                    .padding(start = WdsTheme.dimensions.wdsSpacingSingle, end = WdsTheme.dimensions.wdsSpacingDouble, top = WdsTheme.dimensions.wdsSpacingDouble, bottom = WdsTheme.dimensions.wdsSpacingDouble),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(WdsTheme.colors.colorAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = "Invite link",
                        modifier = Modifier.size(20.dp),
                        tint = WdsTheme.colors.colorSurfaceDefault
                    )
                }
                Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingSingle))
                Text(
                    text = "Invite via link",
                    style = WdsTheme.typography.body1,
                    color = WdsTheme.colors.colorContentDefault
                )
            }

            // Participants list
            participants.forEach { participant ->
                ParticipantItem(
                    user = participant,
                    role = participantRoles[participant.userId]?.role,
                    isCurrentUser = participant.userId == currentUserId,
                    onClick = { onParticipantClick(participant) },
                    onMakeAdmin = { onMakeAdmin(participant.userId) },
                    onRemoveAdmin = { onRemoveAdmin(participant.userId) },
                    onRemove = { onRemoveParticipant(participant.userId) }
                )
            }
        }
    }
}

@Composable
private fun ParticipantItem(
    user: UserEntity,
    role: ParticipantRole?,
    isCurrentUser: Boolean,
    onClick: () -> Unit,
    onMakeAdmin: () -> Unit,
    onRemoveAdmin: () -> Unit,
    onRemove: () -> Unit
) {
    var showOptions by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!isCurrentUser) {
                    showOptions = true
                } else {
                    onClick()
                }
            }
            .padding(start = WdsTheme.dimensions.wdsSpacingSingle, end = WdsTheme.dimensions.wdsSpacingDouble, top = WdsTheme.dimensions.wdsSpacingDouble, bottom = WdsTheme.dimensions.wdsSpacingDouble),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        ) {
            if (user.avatarUrl != null) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WdsTheme.colors.colorSurfaceHighlight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.displayName.firstOrNull()?.toString() ?: "?",
                        style = WdsTheme.typography.body1,
                        color = WdsTheme.colors.colorContentDeemphasized
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingSingle))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = if (isCurrentUser) "You" else user.displayName,
                style = WdsTheme.typography.body1,
                color = WdsTheme.colors.colorContentDefault
            )

            user.statusMessage?.let {
                Text(
                    text = it,
                    style = WdsTheme.typography.body2,
                    color = WdsTheme.colors.colorContentDeemphasized
                )
            }
        }

        if (role == ParticipantRole.ADMIN) {
            Text(
                text = "Group Admin",
                style = WdsTheme.typography.body3,
                color = WdsTheme.colors.colorAccent,
                modifier = Modifier
                    .background(
                        WdsTheme.colors.colorBubbleSurfaceOutgoing,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = WdsTheme.dimensions.wdsSpacingSingle, vertical = WdsTheme.dimensions.wdsSpacingHalf)
            )
        }
    }

    // Options dropdown
    if (showOptions && !isCurrentUser) {
        DropdownMenu(
            expanded = showOptions,
            onDismissRequest = { showOptions = false }
        ) {
            DropdownMenuItem(
                text = { Text("View profile") },
                onClick = {
                    onClick()
                    showOptions = false
                }
            )

            if (role == ParticipantRole.ADMIN) {
                DropdownMenuItem(
                    text = { Text("Remove as admin") },
                    onClick = {
                        onRemoveAdmin()
                        showOptions = false
                    }
                )
            } else {
                DropdownMenuItem(
                    text = { Text("Make group admin") },
                    onClick = {
                        onMakeAdmin()
                        showOptions = false
                    }
                )
            }

            DropdownMenuItem(
                text = {
                    Text(
                        "Remove from group",
                        color = WdsTheme.colors.colorNegative
                    )
                },
                onClick = {
                    onRemove()
                    showOptions = false
                }
            )
        }
    }
}

// Community icon definition
private val communityIcon = ImageVector.Builder(
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFFFFFFFF)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(4.7595f, 13.3957f)
        curveTo(4.65644f, 13.3913f, 4.55139f, 13.389f, 4.44446f, 13.389f)
        curveTo(3.65152f, 13.389f, 2.96238f, 13.5157f, 2.41872f, 13.6688f)
        curveTo(1.95159f, 13.8003f, 1.42115f, 14.016f, 0.975466f, 14.3596f)
        curveTo(0.521192f, 14.7098f, 0.107939f, 15.2339f, 0.0263682f, 15.9559f)
        curveTo(-0.0117388f, 16.2932f, 2.95136e-05f, 17.0153f, 0.00922726f, 17.4244f)
        curveTo(0.0256195f, 18.1535f, 0.623061f, 19f, 1.33916f, 19f)
        horizontalLineTo(4.31303f)
        curveTo(4.13562f, 18.6252f, 4.03074f, 17.9298f, 4.01771f, 17.4856f)
        curveTo(4.00494f, 17.0504f, 3.97075f, 15.8763f, 4.05525f, 15.253f)
        curveTo(4.09928f, 14.9282f, 4.17628f, 14.6239f, 4.27738f, 14.342f)
        curveTo(4.40496f, 13.9862f, 4.57082f, 13.6722f, 4.7595f, 13.3957f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFFFFFFFF)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(19.6869f, 19f)
        horizontalLineTo(22.6608f)
        curveTo(23.3769f, 19f, 23.9744f, 18.1535f, 23.9908f, 17.4244f)
        curveTo(24f, 17.0153f, 24.0117f, 16.2932f, 23.9736f, 15.9559f)
        curveTo(23.8921f, 15.2339f, 23.4788f, 14.7098f, 23.0245f, 14.3596f)
        curveTo(22.5788f, 14.016f, 22.0484f, 13.8003f, 21.5813f, 13.6688f)
        curveTo(21.0376f, 13.5157f, 20.3485f, 13.389f, 19.5555f, 13.389f)
        curveTo(19.4486f, 13.389f, 19.3435f, 13.3913f, 19.2405f, 13.3957f)
        curveTo(19.4291f, 13.6722f, 19.595f, 13.9862f, 19.7226f, 14.342f)
        curveTo(19.8237f, 14.6239f, 19.9007f, 14.9282f, 19.9447f, 15.253f)
        curveTo(20.0292f, 15.8763f, 19.995f, 17.0504f, 19.9822f, 17.4856f)
        curveTo(19.9692f, 17.9298f, 19.8643f, 18.6252f, 19.6869f, 19f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFFFFFFFF)),
        pathFillType = PathFillType.EvenOdd
    ) {
        moveTo(8.52831f, 12.9865f)
        curveTo(9.36687f, 12.7437f, 10.5649f, 12.5001f, 12f, 12.5001f)
        curveTo(13.4351f, 12.5001f, 14.6331f, 12.7437f, 15.4716f, 12.9865f)
        curveTo(15.9039f, 13.1117f, 16.4206f, 13.2888f, 16.8826f, 13.5731f)
        curveTo(17.352f, 13.8619f, 17.8167f, 14.294f, 18.0492f, 14.9421f)
        curveTo(18.1097f, 15.111f, 18.1563f, 15.2944f, 18.183f, 15.4918f)
        curveTo(18.243f, 15.9341f, 18.22f, 16.9291f, 18.2052f, 17.4335f)
        curveTo(18.184f, 18.1587f, 17.5884f, 19f, 16.8756f, 19f)
        horizontalLineTo(7.12434f)
        curveTo(6.41155f, 19f, 5.81599f, 18.1587f, 5.79472f, 17.4335f)
        curveTo(5.77992f, 16.9291f, 5.75694f, 15.9341f, 5.81691f, 15.4918f)
        curveTo(5.84367f, 15.2944f, 5.89021f, 15.111f, 5.95079f, 14.9421f)
        curveTo(6.18324f, 14.294f, 6.64793f, 13.8619f, 7.11736f, 13.5731f)
        curveTo(7.57933f, 13.2888f, 8.09602f, 13.1117f, 8.52831f, 12.9865f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFFFFFFFF)),
        pathFillType = PathFillType.EvenOdd
    ) {
        moveTo(16.8889f, 9.33f)
        curveTo(16.8889f, 7.85724f, 18.0828f, 6.67f, 19.5555f, 6.67f)
        curveTo(21.0283f, 6.67f, 22.2222f, 7.85724f, 22.2222f, 9.33f)
        curveTo(22.2222f, 10.8028f, 21.0283f, 12f, 19.5555f, 12f)
        curveTo(18.0828f, 12f, 16.8889f, 10.8028f, 16.8889f, 9.33f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFFFFFFFF)),
        pathFillType = PathFillType.EvenOdd
    ) {
        moveTo(8.44443f, 7.56f)
        curveTo(8.44443f, 5.59633f, 10.0363f, 4f, 12f, 4f)
        curveTo(13.9636f, 4f, 15.5555f, 5.59633f, 15.5555f, 7.56f)
        curveTo(15.5555f, 9.52367f, 13.9636f, 11.11f, 12f, 11.11f)
        curveTo(10.0363f, 11.11f, 8.44443f, 9.52367f, 8.44443f, 7.56f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFFFFFFFF)),
        pathFillType = PathFillType.EvenOdd
    ) {
        moveTo(1.77777f, 9.33f)
        curveTo(1.77777f, 7.85724f, 2.97168f, 6.67f, 4.44444f, 6.67f)
        curveTo(5.91719f, 6.67f, 7.1111f, 7.85724f, 7.1111f, 9.33f)
        curveTo(7.1111f, 10.8028f, 5.91719f, 12f, 4.44444f, 12f)
        curveTo(2.97168f, 12f, 1.77777f, 10.8028f, 1.77777f, 9.33f)
        close()
    }
}.build()