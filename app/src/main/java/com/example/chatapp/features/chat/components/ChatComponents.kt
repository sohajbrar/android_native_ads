package com.example.chatapp.features.chat.components

import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ripple
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.data.local.entity.MessageStatus
import com.example.chatapp.data.local.entity.MessageType
import androidx.compose.material3.MaterialTheme
import com.example.chatapp.wds.theme.BaseColors
import com.example.chatapp.wds.theme.WdsTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Get a consistent color for a participant based on their name.
 * Uses 500-level colors from BaseColors for consistency.
 */
private fun getParticipantColor(name: String): Color {
    val colors = listOf(
        BaseColors.wdsPurple500,
        BaseColors.wdsOrange500,
        BaseColors.wdsPink500,
        BaseColors.wdsCobalt500,
        BaseColors.wdsTeal500,
        BaseColors.wdsBrown500,
        BaseColors.wdsEmerald500,
        BaseColors.wdsSkyBlue500,
        BaseColors.wdsRed500,
        BaseColors.wdsYellow500
    )

    // Generate consistent index based on name hash
    val index = name.hashCode().let { if (it < 0) -it else it } % colors.size
    return colors[index]
}

/**
 * Get a light background color for participant avatar based on their name.
 * Uses lighter tints of the participant color.
 */
private fun getParticipantAvatarBackground(name: String): Color {
    val baseColor = getParticipantColor(name)
    // Create a lighter tint by mixing with white (90% white, 10% base color)
    return baseColor.copy(alpha = 0.15f)
}

@Composable
fun ChatComposer(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onAttachClick: () -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier,
    isBroadcast: Boolean = false,
    onStickerClick: () -> Unit = {}
) {
    // Cache theme lookups for performance
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val shapes = WdsTheme.shapes
    val view = LocalView.current

    // Track line count for multiline detection
    var lineCount by remember { mutableStateOf(1) }
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val typography = WdsTheme.typography

    // Auto-scroll to bottom when text changes
    LaunchedEffect(value) {
        if (scrollState.maxValue > 0) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    // Determine if multiline (more than 1 line)
    val isMultiline = lineCount > 1

    // Max 6 lines
    val maxLines = 6

    // Calculate max height based on line height (convert TextUnit to Dp)
    val lineHeightDp = remember(density, typography) {
        with(density) {
            val lh = typography.chatBody1.lineHeight
            if (lh.value > 0) lh.toDp() else 21.dp
        }
    }
    val maxHeight = lineHeightDp * maxLines
    val lineHeightPx = remember(density, typography) {
        with(density) {
            val lh = typography.chatBody1.lineHeight
            if (lh.value > 0) lh.toPx() else 21.dp.toPx()
        }
    }

    Surface(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.wdsSpacingHalf),
            verticalAlignment = Alignment.Bottom
        ) {
            val inputEndPadding by animateDpAsState(
                targetValue = if (value.isNotEmpty()) dimensions.wdsSpacingHalf else 0.dp,
                animationSpec = tween(durationMillis = 200),
                label = "inputEndPadding"
            )

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = inputEndPadding),
                shape = shapes.triple,
                color = colors.colorBubbleSurfaceIncoming,
                shadowElevation = dimensions.wdsElevationSubtle
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (isMultiline) {
                                Modifier.padding(
                                    start = 0.dp,
                                    end = dimensions.wdsSpacingHalf,
                                    top = dimensions.wdsSpacingHalf,
                                    bottom = 0.dp
                                )
                            } else {
                                Modifier.padding(
                                    end = dimensions.wdsSpacingHalf
                                )
                            }
                        ),
                    verticalAlignment = if (isMultiline) Alignment.Bottom else Alignment.CenterVertically
                ) {
                    // Left icon container (48dp fixed width, bottom-aligned)
                    Box(
                        modifier = Modifier
                            .size(dimensions.wdsTouchTargetComfortable)
                            .align(Alignment.Bottom)
                            .clickableWithSound(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false, radius = 20.dp),
                                onClick = onStickerClick
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Expression/emoji icon with custom SVG path
                        val expressionIcon = rememberVectorPainter(
                            ImageVector.Builder(
                                defaultWidth = 25.dp,
                                defaultHeight = 25.dp,
                                viewportWidth = 25f,
                                viewportHeight = 25f
                            ).apply {
                                // Left eye
                                path(
                                    fill = SolidColor(colors.colorContentDeemphasized),
                                    pathFillType = PathFillType.NonZero
                                ) {
                                    moveTo(8.99893f, 10.5021f)
                                    curveTo(9.82736f, 10.5021f, 10.4989f, 9.8305f, 10.4989f, 9.00208f)
                                    curveTo(10.4989f, 8.17365f, 9.82736f, 7.50208f, 8.99893f, 7.50208f)
                                    curveTo(8.1705f, 7.50208f, 7.49893f, 8.17365f, 7.49893f, 9.00208f)
                                    curveTo(7.49893f, 9.8305f, 8.1705f, 10.5021f, 8.99893f, 10.5021f)
                                    close()
                                }
                                // Right eye
                                path(
                                    fill = SolidColor(colors.colorContentDeemphasized),
                                    pathFillType = PathFillType.NonZero
                                ) {
                                    moveTo(17.5011f, 9.00208f)
                                    curveTo(17.5011f, 9.8305f, 16.8295f, 10.5021f, 16.0011f, 10.5021f)
                                    curveTo(15.1726f, 10.5021f, 14.5011f, 9.8305f, 14.5011f, 9.00208f)
                                    curveTo(14.5011f, 8.17365f, 15.1726f, 7.50208f, 16.0011f, 7.50208f)
                                    curveTo(16.8295f, 7.50208f, 17.5011f, 8.17365f, 17.5011f, 9.00208f)
                                    close()
                                }
                                // Face outline with speech bubble
                                path(
                                    fill = SolidColor(colors.colorContentDeemphasized),
                                    pathFillType = PathFillType.EvenOdd
                                ) {
                                    moveTo(17.3221f, 20.2299f)
                                    curveTo(16.0379f, 21.5037f, 14.3087f, 22.2281f, 12.5f, 22.25f)
                                    horizontalLineTo(9.77273f)
                                    curveTo(5.75611f, 22.25f, 2.5f, 18.9939f, 2.5f, 14.9773f)
                                    verticalLineTo(9.52273f)
                                    curveTo(2.5f, 5.50611f, 5.75611f, 2.25f, 9.77273f, 2.25f)
                                    horizontalLineTo(15.2273f)
                                    curveTo(19.2439f, 2.25f, 22.5f, 5.50611f, 22.5f, 9.52273f)
                                    verticalLineTo(12.0641f)
                                    curveTo(22.5f, 14.0032f, 21.7256f, 15.862f, 20.3489f, 17.2276f)
                                    lineTo(17.3221f, 20.2299f)
                                    close()
                                    moveTo(15.2273f, 4.25f)
                                    horizontalLineTo(9.77273f)
                                    curveTo(6.86068f, 4.25f, 4.5f, 6.61068f, 4.5f, 9.52273f)
                                    verticalLineTo(14.9773f)
                                    curveTo(4.5f, 17.8893f, 6.86068f, 20.25f, 9.77273f, 20.25f)
                                    horizontalLineTo(11.8331f)
                                    curveTo(12.222f, 20.1471f, 12.5081f, 19.7917f, 12.5058f, 19.3704f)
                                    lineTo(12.4935f, 17.1064f)
                                    curveTo(12.4933f, 17.0701f, 12.4935f, 17.034f, 12.4941f, 16.9979f)
                                    curveTo(11.5454f, 16.9973f, 10.659f, 16.764f, 9.83502f, 16.2979f)
                                    curveTo(9.01002f, 15.8312f, 8.34752f, 15.1979f, 7.84752f, 14.3979f)
                                    curveTo(7.74752f, 14.1979f, 7.75585f, 13.9979f, 7.87252f, 13.7979f)
                                    curveTo(7.98919f, 13.5979f, 8.16419f, 13.4979f, 8.39752f, 13.4979f)
                                    lineTo(14.0939f, 13.4979f)
                                    curveTo(14.9494f, 12.731f, 16.0811f, 12.266f, 17.3216f, 12.2708f)
                                    lineTo(19.5806f, 12.2796f)
                                    curveTo(20.0817f, 12.2815f, 20.4889f, 11.8759f, 20.4889f, 11.3748f)
                                    verticalLineTo(9.32648f)
                                    horizontalLineTo(20.4964f)
                                    curveTo(20.3932f, 6.50535f, 18.0736f, 4.25f, 15.2273f, 4.25f)
                                    close()
                                    moveTo(14.5057f, 19.3595f)
                                    curveTo(14.5066f, 19.5105f, 14.4959f, 19.6589f, 14.4744f, 19.8037f)
                                    curveTo(15.0044f, 19.5624f, 15.4926f, 19.2276f, 15.9136f, 18.8099f)
                                    lineTo(18.9405f, 15.8076f)
                                    curveTo(19.3989f, 15.3529f, 19.7653f, 14.8226f, 20.0274f, 14.246f)
                                    curveTo(19.8793f, 14.2687f, 19.7275f, 14.2801f, 19.5729f, 14.2795f)
                                    lineTo(17.3138f, 14.2708f)
                                    curveTo(15.752f, 14.2647f, 14.485f, 15.5337f, 14.4935f, 17.0955f)
                                    lineTo(14.5057f, 19.3595f)
                                    close()
                                }
                            }.build()
                        )

                        Icon(
                            painter = expressionIcon,
                            contentDescription = "Expression",
                            tint = colors.colorContentDeemphasized,
                            modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                        )
                    }

                    // Text field with scrolling support
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(max = maxHeight)
                            .verticalScroll(scrollState)
                            .padding(vertical = dimensions.wdsSpacingSingle)
                    ) {
                        BasicTextField(
                            value = value,
                            onValueChange = onValueChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    // Calculate line count based on text field height
                                    val textHeight = coordinates.size.height.toFloat()
                                    val newLineCount = if (textHeight > 0 && lineHeightPx > 0) {
                                        maxOf(1, (textHeight / lineHeightPx).toInt())
                                    } else {
                                        1
                                    }
                                    if (newLineCount != lineCount) {
                                        lineCount = newLineCount
                                    }
                                },
                            textStyle = WdsTheme.typography.chatBody1.copy(
                                color = colors.colorContentDefault
                            ),
                            cursorBrush = SolidColor(colors.colorAccent),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (value.isEmpty()) {
                                        Text(
                                            "Message",
                                            style = WdsTheme.typography.chatBody1.copy(
                                                color = colors.colorContentDeemphasized
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }

                    // Right side icons container - fixed size, bottom-aligned
                    Box(
                        modifier = Modifier
                            .size(
                                width = if (value.isEmpty()) dimensions.wdsAvatarExtraLarge else dimensions.wdsTouchTargetStandard,
                                height = dimensions.wdsTouchTargetComfortable
                            )
                            .align(Alignment.Bottom),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            // Attachment icon - custom SVG path
                            val attachmentIcon = rememberVectorPainter(
                                ImageVector.Builder(
                                    defaultWidth = 24.dp,
                                    defaultHeight = 24.dp,
                                    viewportWidth = 24f,
                                    viewportHeight = 24f
                                ).apply {
                                    path(
                                        fill = SolidColor(colors.colorContentDeemphasized),
                                        pathFillType = PathFillType.NonZero
                                    ) {
                                        moveTo(18f, 15.75f)
                                        curveTo(18f, 17.4833f, 17.3917f, 18.9583f, 16.175f, 20.175f)
                                        curveTo(14.9583f, 21.3917f, 13.4833f, 22f, 11.75f, 22f)
                                        curveTo(10.0167f, 22f, 8.54167f, 21.3917f, 7.325f, 20.175f)
                                        curveTo(6.10833f, 18.9583f, 5.5f, 17.4833f, 5.5f, 15.75f)
                                        verticalLineTo(6.5f)
                                        curveTo(5.5f, 5.25f, 5.9375f, 4.1875f, 6.8125f, 3.3125f)
                                        curveTo(7.6875f, 2.4375f, 8.75f, 2f, 10f, 2f)
                                        curveTo(11.25f, 2f, 12.3125f, 2.4375f, 13.1875f, 3.3125f)
                                        curveTo(14.0625f, 4.1875f, 14.5f, 5.25f, 14.5f, 6.5f)
                                        verticalLineTo(15.25f)
                                        curveTo(14.5f, 16.0167f, 14.2333f, 16.6667f, 13.7f, 17.2f)
                                        curveTo(13.1667f, 17.7333f, 12.5167f, 18f, 11.75f, 18f)
                                        curveTo(10.9833f, 18f, 10.3333f, 17.7333f, 9.8f, 17.2f)
                                        curveTo(9.26667f, 16.6667f, 9f, 16.0167f, 9f, 15.25f)
                                        verticalLineTo(7f)
                                        curveTo(9f, 6.44772f, 9.44772f, 6f, 10f, 6f)
                                        curveTo(10.5523f, 6f, 11f, 6.44772f, 11f, 7f)
                                        verticalLineTo(15.25f)
                                        curveTo(11f, 15.4667f, 11.0708f, 15.6458f, 11.2125f, 15.7875f)
                                        curveTo(11.3542f, 15.9292f, 11.5333f, 16f, 11.75f, 16f)
                                        curveTo(11.9667f, 16f, 12.1458f, 15.9292f, 12.2875f, 15.7875f)
                                        curveTo(12.4292f, 15.6458f, 12.5f, 15.4667f, 12.5f, 15.25f)
                                        verticalLineTo(6.5f)
                                        curveTo(12.4833f, 5.8f, 12.2375f, 5.20833f, 11.7625f, 4.725f)
                                        curveTo(11.2875f, 4.24167f, 10.7f, 4f, 10f, 4f)
                                        curveTo(9.3f, 4f, 8.70833f, 4.24167f, 8.225f, 4.725f)
                                        curveTo(7.74167f, 5.20833f, 7.5f, 5.8f, 7.5f, 6.5f)
                                        verticalLineTo(15.75f)
                                        curveTo(7.48333f, 16.9333f, 7.89167f, 17.9375f, 8.725f, 18.7625f)
                                        curveTo(9.55833f, 19.5875f, 10.5667f, 20f, 11.75f, 20f)
                                        curveTo(12.9167f, 20f, 13.9083f, 19.5875f, 14.725f, 18.7625f)
                                        curveTo(15.5417f, 17.9375f, 15.9667f, 16.9333f, 16f, 15.75f)
                                        verticalLineTo(7f)
                                        curveTo(16f, 6.44772f, 16.4477f, 6f, 17f, 6f)
                                        curveTo(17.5523f, 6f, 18f, 6.44772f, 18f, 7f)
                                        verticalLineTo(15.75f)
                                        close()
                                    }
                                }.build()
                            )

                            Box(
                                modifier = Modifier
                                    .size(dimensions.wdsTouchTargetStandard)
                                    .clickableWithSound(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = false, radius = 20.dp),
                                        onClick = onAttachClick
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = attachmentIcon,
                                    contentDescription = "Attach",
                                    tint = colors.colorContentDeemphasized,
                                    modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                                )
                            }

                            // Camera icon - only show when empty
                            if (value.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(dimensions.wdsTouchTargetStandard)
                                        .clickableWithSound(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple(bounded = false, radius = 20.dp),
                                            onClick = onCameraClick
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.PhotoCamera,
                                        contentDescription = "Camera",
                                        tint = colors.colorContentDeemphasized,
                                        modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = value.isNotEmpty(),
                enter = expandHorizontally(animationSpec = tween(200)) + scaleIn(animationSpec = tween(200)) + fadeIn(animationSpec = tween(200)),
                exit = shrinkHorizontally(animationSpec = tween(200)) + scaleOut(animationSpec = tween(200)) + fadeOut(animationSpec = tween(200))
            ) {
                FloatingActionButton(
                    onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onSendClick() },
                    modifier = Modifier.size(dimensions.wdsTouchTargetComfortable),
                    containerColor = colors.colorAccent,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp
                    )
                ) {
                    Icon(
                        if (isBroadcast) Icons.AutoMirrored.Filled.ArrowForward
                        else Icons.AutoMirrored.Default.Send,
                        contentDescription = if (isBroadcast) "Next" else "Send",
                        tint = colors.colorContentOnAccent,
                        modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageItem(
    message: MessageEntity,
    isFromCurrentUser: Boolean,
    isGroupChat: Boolean,
    senderName: String,
    senderAvatar: String?,
    showSenderInfo: Boolean,
    modifier: Modifier = Modifier,
    showTimestamp: Boolean = true,
    actionContent: (@Composable () -> Unit)? = null
) {
    // Cache theme lookups for performance
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val shapes = WdsTheme.shapes
    val timestampEndPadding = with(LocalDensity.current) { 10.toDp() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble, vertical = WdsTheme.dimensions.wdsSpacingQuarter),
        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        // Show avatar for group chats (incoming messages only, first in sequence)
        if (!isFromCurrentUser && isGroupChat && showSenderInfo) {
            // Avatar - check for both null and empty string
            if (senderAvatar != null && senderAvatar.isNotEmpty()) {
                AsyncImage(
                    model = senderAvatar,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback to colored avatar with initial
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(getParticipantAvatarBackground(senderName)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = senderName.firstOrNull()?.toString()?.uppercase() ?: "?",
                        style = WdsTheme.typography.body3Emphasized,
                        color = getParticipantColor(senderName)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(dimensions.wdsSpacingSingle))
        }
        
        // Add left padding for continuation messages in group chat
        if (!isFromCurrentUser && isGroupChat && !showSenderInfo) {
            Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingQuint)) // 28dp avatar + 8dp spacing = ~36dp, closest is 40dp
        }

            Column(modifier = Modifier.widthIn(max = 260.dp)) {
            // Message bubble
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = dimensions.wdsElevationSubtle,
                        spotColor = colors.colorBubbleSurfaceOverlay,
                        ambientColor = colors.colorBubbleSurfaceOverlay,
                        shape = if (actionContent != null) shapes.singlePlusTop else shapes.singlePlus
                    )
                    .clip(if (actionContent != null) shapes.singlePlusTop else shapes.singlePlus)
                    .background(
                        if (isFromCurrentUser) {
                            colors.colorBubbleSurfaceOutgoing
                        } else {
                            colors.colorBubbleSurfaceIncoming
                        }
                    )
            ) {
                // Handle different message types
                when (message.messageType) {
                    MessageType.TEXT -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = WdsTheme.dimensions.wdsSpacingHalfPlus)
                        ) {
                            // Show sender name inside bubble for group chats
                            if (!isFromCurrentUser && isGroupChat && showSenderInfo) {
                                Text(
                                    text = senderName,
                                    style = WdsTheme.typography.body3Emphasized,
                                    color = getParticipantColor(senderName),
                                    modifier = Modifier.padding(
                                        start = WdsTheme.dimensions.wdsSpacingSingle,
                                        end = WdsTheme.dimensions.wdsSpacingSingle,
                                        bottom = WdsTheme.dimensions.wdsSpacingQuarter
                                    )
                                )
                            }
                            
                            // Message content with timestamp
                            if (!showTimestamp || message.content.length >= 20) {
                                Text(
                                    text = message.content,
                                    style = WdsTheme.typography.chatBody1,
                                    color = colors.colorContentDefault,
                                    modifier = Modifier.padding(horizontal = WdsTheme.dimensions.wdsSpacingSingle)
                                )
                                if (showTimestamp) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = WdsTheme.dimensions.wdsSpacingQuarter, end = timestampEndPadding),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = formatTime(message.timestamp),
                                            style = WdsTheme.typography.chatBody3,
                                            color = colors.colorBubbleContentDeemphasized
                                        )
                                        if (isFromCurrentUser) {
                                            Spacer(modifier = Modifier.width(3.dp))
                                            MessageStatusIcon(message.derivedStatus)
                                        }
                                    }
                                }
                            } else {
                                // Short messages - inline timestamp
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = WdsTheme.dimensions.wdsSpacingSingle,
                                            end = timestampEndPadding
                                        ),
                                    verticalAlignment = Alignment.Bottom,
                                ) {
                                    Text(
                                        text = message.content,
                                        style = WdsTheme.typography.chatBody1,
                                        color = colors.colorContentDefault
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = formatTime(message.timestamp),
                                            style = WdsTheme.typography.chatBody3,
                                            color = colors.colorBubbleContentDeemphasized
                                        )
                                        if (isFromCurrentUser) {
                                            Spacer(modifier = Modifier.width(3.dp))
                                            MessageStatusIcon(message.derivedStatus)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    MessageType.GIF -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            // GIF background
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black
                                )
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Surface(
                                        color = Color.Black.copy(alpha = 0.7f),
                                        shape = CircleShape,
                                        modifier = Modifier.size(60.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                "GIF",
                                                color = colors.colorAlwaysWhite,
                                                style = WdsTheme.typography.headline2
                                            )
                                        }
                                    }
                                }
                            }
                            
                            // Show sender name inside media bubble for group chats
                            if (!isFromCurrentUser && isGroupChat && showSenderInfo) {
                                Text(
                                    text = senderName,
                                    style = WdsTheme.typography.body3Emphasized,
                                    color = colors.colorAlwaysWhite,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(start = WdsTheme.dimensions.wdsSpacingSingle, top = WdsTheme.dimensions.wdsSpacingSingle)
                                )
                            }
                            
                            // Timestamp and watermark at the bottom
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(dimensions.wdsSpacingSingle),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "tenor",
                                    style = WdsTheme.typography.body3,
                                    color = colors.colorAlwaysWhite.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.width(dimensions.wdsSpacingSingle))
                                Text(
                                    text = formatTime(message.timestamp),
                                    style = WdsTheme.typography.body3,
                                    color = colors.colorAlwaysWhite.copy(alpha = 0.9f)
                                )
                                if (isFromCurrentUser) {
                                    Spacer(modifier = Modifier.width(3.dp))
                                    MessageStatusIcon(message.derivedStatus, tint = colors.colorAlwaysWhite.copy(alpha = 0.9f))
                                }
                            }
                        }
                    }
                    else -> {
                        // Other message types - same logic as TEXT
                        // Simple threshold that guarantees no cutoff
                        if (message.content.length < 20) {
                            // Short messages - inline timestamp
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = WdsTheme.dimensions.wdsSpacingSingle,
                                        end = timestampEndPadding,
                                        top = WdsTheme.dimensions.wdsSpacingHalfPlus,
                                        bottom = WdsTheme.dimensions.wdsSpacingHalfPlus
                                    ),
                                verticalAlignment = Alignment.Bottom,
                            ) {
                                Text(
                                    text = message.content,
                                    style = WdsTheme.typography.chatBody1,
                                    color = colors.colorContentDefault
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = formatTime(message.timestamp),
                                        style = WdsTheme.typography.chatBody3,
                                        color = colors.colorBubbleContentDeemphasized
                                    )
                                    if (isFromCurrentUser) {
                                        Spacer(modifier = Modifier.width(3.dp))
                                        MessageStatusIcon(message.derivedStatus)
                                    }
                                }
                            }
                        } else {
                            // Longer messages - timestamp below
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = WdsTheme.dimensions.wdsSpacingHalfPlus)
                            ) {
                                Text(
                                    text = message.content,
                                    style = WdsTheme.typography.chatBody1,
                                    color = colors.colorContentDefault,
                                    modifier = Modifier.padding(horizontal = WdsTheme.dimensions.wdsSpacingSingle)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = WdsTheme.dimensions.wdsSpacingQuarter, end = timestampEndPadding),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = formatTime(message.timestamp),
                                        style = WdsTheme.typography.chatBody3,
                                        color = colors.colorBubbleContentDeemphasized
                                    )
                                    if (isFromCurrentUser) {
                                        Spacer(modifier = Modifier.width(3.dp))
                                        MessageStatusIcon(message.derivedStatus)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (actionContent != null) {
                actionContent()
            }
            }
        }
    }


@Composable
private fun MessageStatusIcon(status: MessageStatus, tint: Color = Color.Unspecified) {
    val colors = WdsTheme.colors
    val statusColor = if (tint != Color.Unspecified) {
        tint
    } else when (status) {
        MessageStatus.READ -> colors.colorContentRead // Using color token #007BFC
        MessageStatus.FAILED -> Color.Red
        else -> colors.colorContentDeemphasized // Gray for sent/delivered
    }
    
    when (status) {
        MessageStatus.SENT -> {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Sent",
                tint = statusColor,
                modifier = Modifier.size(16.dp)
            )
        }
        MessageStatus.DELIVERED -> {
            // Double checkmark for delivered (gray)
            DoubleCheckIcon(
                tint = statusColor,
                modifier = Modifier.size(width = 16.dp, height = 12.dp)
            )
        }
        MessageStatus.READ -> {
            // Double checkmark for read (blue)
            DoubleCheckIcon(
                tint = statusColor,
                modifier = Modifier.size(width = 16.dp, height = 12.dp)
            )
        }
        MessageStatus.FAILED -> {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Failed",
                tint = statusColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun DoubleCheckIcon(
    tint: Color,
    modifier: Modifier = Modifier
) {
    val doubleCheckIcon = ImageVector.Builder(
        defaultWidth = 16.dp,
        defaultHeight = 12.dp,
        viewportWidth = 16f,
        viewportHeight = 12f
    ).path(
        fill = SolidColor(tint),
        fillAlpha = 1f,
        stroke = null,
        strokeAlpha = 1f,
        strokeLineWidth = 0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 4f,
        pathFillType = PathFillType.NonZero
    ) {
            // First check mark
            moveTo(10.5714f, 1.40088f)
            lineTo(10.9078f, 1.67383f)
            curveTo(11.0348f, 1.76693f, 11.0982f, 1.89176f, 11.0982f, 2.04834f)
            curveTo(11.0982f, 2.1499f, 11.0623f, 2.24935f, 10.9903f, 2.34668f)
            lineTo(4.34434f, 10.7637f)
            curveTo(4.28086f, 10.8483f, 4.20046f, 10.9139f, 4.10313f, 10.9604f)
            curveTo(4.0058f, 11.007f, 3.90424f, 11.0303f, 3.79844f, 11.0303f)
            curveTo(3.61224f, 11.0303f, 3.44509f, 10.9626f, 3.29698f, 10.8271f)
            lineTo(0.300883f, 7.83105f)
            curveTo(0.207784f, 7.73796f, 0.161235f, 7.6237f, 0.161235f, 7.48828f)
            curveTo(0.161235f, 7.35286f, 0.207784f, 7.24072f, 0.300883f, 7.15186f)
            lineTo(0.611918f, 6.84082f)
            curveTo(0.709249f, 6.74349f, 0.823507f, 6.69482f, 0.954692f, 6.69482f)
            curveTo(1.01817f, 6.69482f, 1.07953f, 6.70752f, 1.13877f, 6.73291f)
            curveTo(1.19802f, 6.7583f, 1.2488f, 6.79427f, 1.29112f, 6.84082f)
            lineTo(3.69688f, 9.11328f)
            lineTo(9.88584f, 1.47705f)
            curveTo(9.99164f, 1.35856f, 10.1186f, 1.29932f, 10.2667f, 1.29932f)
            curveTo(10.3894f, 1.29932f, 10.491f, 1.33317f, 10.5714f, 1.40088f)
            close()
            
            // Second check mark
            moveTo(6.6168f, 9.75439f)
            curveTo(6.5237f, 9.65706f, 6.47715f, 9.54281f, 6.47715f, 9.41162f)
            curveTo(6.47715f, 9.29313f, 6.51947f, 9.18522f, 6.60411f, 9.08789f)
            lineTo(6.8961f, 8.75781f)
            curveTo(6.9892f, 8.65202f, 7.1098f, 8.59912f, 7.25791f, 8.59912f)
            curveTo(7.3891f, 8.59912f, 7.49489f, 8.63932f, 7.5753f, 8.71973f)
            curveTo(7.7488f, 8.83822f, 7.93077f, 8.95671f, 8.1212f, 9.0752f)
            lineTo(14.2657f, 1.50244f)
            curveTo(14.3715f, 1.38395f, 14.4964f, 1.32471f, 14.6402f, 1.32471f)
            curveTo(14.7587f, 1.32471f, 14.8582f, 1.35856f, 14.9386f, 1.42627f)
            lineTo(15.294f, 1.69922f)
            curveTo(15.4125f, 1.80501f, 15.4718f, 1.92985f, 15.4718f, 2.07373f)
            curveTo(15.4718f, 2.18799f, 15.4358f, 2.28743f, 15.3639f, 2.37207f)
            lineTo(8.74327f, 10.7637f)
            curveTo(8.67556f, 10.8483f, 8.59304f, 10.9139f, 8.49571f, 10.9604f)
            curveTo(8.39838f, 11.007f, 8.29681f, 11.0303f, 8.19102f, 11.0303f)
            curveTo(7.99636f, 11.0303f, 7.83132f, 10.9626f, 7.6959f, 10.8271f)
            lineTo(6.6168f, 9.75439f)
            close()
    }.build()
    
    Icon(
        imageVector = doubleCheckIcon,
        contentDescription = "Read",
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun UnreadMessagesPill(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Cache theme lookups for performance
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val shapes = WdsTheme.shapes
    val isDarkTheme = isSystemInDarkTheme()

    // Create custom icon from SVG
    val unreadIcon = rememberVectorPainter(
        ImageVector.Builder(
            defaultWidth = 18.dp,
            defaultHeight = 18.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).apply {
            // Star/sparkle shape
            path(
                fill = SolidColor(colors.colorContentDefault),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14.3762f, 0.703953f)
                curveTo(14.5266f, 0.353891f, 15.0241f, 0.353891f, 15.1746f, 0.703953f)
                lineTo(15.7493f, 2.04141f)
                curveTo(15.7933f, 2.14368f, 15.8749f, 2.22512f, 15.9775f, 2.26896f)
                lineTo(17.3185f, 2.84217f)
                curveTo(17.6695f, 2.99218f, 17.6695f, 3.48842f, 17.3185f, 3.63842f)
                lineTo(15.9775f, 4.21164f)
                curveTo(15.8749f, 4.25548f, 15.7933f, 4.33691f, 15.7493f, 4.43918f)
                lineTo(15.1629f, 5.80389f)
                curveTo(15.0142f, 6.14978f, 14.5244f, 6.1551f, 14.3683f, 5.81251f)
                lineTo(13.7384f, 4.43052f)
                curveTo(13.6942f, 4.33353f, 13.6157f, 4.25617f, 13.5179f, 4.21329f)
                lineTo(12.2161f, 3.64202f)
                curveTo(11.8684f, 3.48946f, 11.8708f, 2.99664f, 12.22f, 2.84737f)
                lineTo(13.5733f, 2.26896f)
                curveTo(13.6758f, 2.22512f, 13.7575f, 2.14368f, 13.8014f, 2.04141f)
                lineTo(14.3762f, 0.703953f)
                close()
            }
            // First line
            path(
                fill = SolidColor(colors.colorContentDefault),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(2.73944f, 4.99674f)
                curveTo(2.73944f, 4.58252f, 3.07523f, 4.24674f, 3.48944f, 4.24674f)
                horizontalLineTo(10.9894f)
                curveTo(11.4037f, 4.24674f, 11.7394f, 4.58252f, 11.7394f, 4.99674f)
                curveTo(11.7394f, 5.41095f, 11.4037f, 5.74674f, 10.9894f, 5.74674f)
                horizontalLineTo(3.48944f)
                curveTo(3.07523f, 5.74674f, 2.73944f, 5.41095f, 2.73944f, 4.99674f)
                close()
            }
            // Second line
            path(
                fill = SolidColor(colors.colorContentDefault),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(2.73944f, 9.56082f)
                curveTo(2.73944f, 9.14661f, 3.07523f, 8.81082f, 3.48944f, 8.81082f)
                horizontalLineTo(13.9894f)
                curveTo(14.4037f, 8.81082f, 14.7394f, 9.14661f, 14.7394f, 9.56082f)
                curveTo(14.7394f, 9.97504f, 14.4037f, 10.3108f, 13.9894f, 10.3108f)
                horizontalLineTo(3.48944f)
                curveTo(3.07523f, 10.3108f, 2.73944f, 9.97504f, 2.73944f, 9.56082f)
                close()
            }
            // Third line
            path(
                fill = SolidColor(colors.colorContentDefault),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(2.73944f, 14.1251f)
                curveTo(2.73944f, 13.7109f, 3.07523f, 13.3751f, 3.48944f, 13.3751f)
                horizontalLineTo(13.9894f)
                curveTo(14.4037f, 13.3751f, 14.7394f, 13.7109f, 14.7394f, 14.1251f)
                curveTo(14.7394f, 14.5393f, 14.4037f, 14.8751f, 13.9894f, 14.8751f)
                horizontalLineTo(3.48944f)
                curveTo(3.07523f, 14.8751f, 2.73944f, 14.5393f, 2.73944f, 14.1251f)
                close()
            }
        }.build()
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.colorAlwaysWhite.copy(alpha = if (isDarkTheme) 0.05f else 0.3f))
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingDouble, vertical = WdsTheme.dimensions.wdsSpacingSingle),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(WdsTheme.shapes.double)
                .background(colors.colorSurfaceDefault)
                .clickableWithSound { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = dimensions.wdsSpacingSinglePlus, vertical = dimensions.wdsSpacingHalfPlus),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Custom icon with star and lines
                Icon(
                    painter = unreadIcon,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = colors.colorContentDefault
                )
                Spacer(modifier = Modifier.width(WdsTheme.dimensions.wdsSpacingHalfPlus))
                Text(
                    text = "$count unread message${if (count > 1) "s" else ""}",
                    style = WdsTheme.typography.body2Emphasized,
                    color = colors.colorContentDefault
                )
            }
        }
    }
}

@Composable
fun DateHeader(
    timestamp: Long,
    modifier: Modifier = Modifier
) {
    // Cache theme lookups for performance
    val colors = WdsTheme.colors
    val shapes = WdsTheme.shapes

    Box(
        modifier = modifier
            .background(
                color = colors.colorBubbleSurfaceIncoming.copy(alpha = 0.9f),
                shape = shapes.single
            )
            .padding(horizontal = WdsTheme.dimensions.wdsSpacingSinglePlus, vertical = WdsTheme.dimensions.wdsSpacingHalfPlus)
    ) {
        Text(
            text = formatDateForHeader(timestamp),
            style = WdsTheme.typography.body3Emphasized,
            color = colors.colorContentDeemphasized
        )
    }
}

private fun formatTime(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(timestamp))
}

private fun formatDate(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(timestamp))
}

private fun formatDateForHeader(timestamp: Long): String {
    val messageDate = Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val daysAgo = ChronoUnit.DAYS.between(messageDate, today)
    
    return when {
        messageDate == today -> "Today"
        messageDate == yesterday -> "Yesterday"
        daysAgo < 7 -> messageDate.format(DateTimeFormatter.ofPattern("EEEE")) // Day name
        messageDate.year == today.year -> messageDate.format(DateTimeFormatter.ofPattern("MMMM d"))
        else -> messageDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
    }
}

