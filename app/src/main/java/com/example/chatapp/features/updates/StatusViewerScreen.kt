package com.example.chatapp.features.updates

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.chatapp.R
import com.example.chatapp.wds.theme.WdsTheme
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private const val STATUS_DURATION_MS = 5000
private const val BOOST_DURATION_MS = 5000
private const val DISMISS_THRESHOLD_DP = 150f
private const val SWIPE_USER_THRESHOLD_DP = 80f
private val boostDarkBg = Color(0xFF0B1014)

@Composable
fun StatusViewerScreen(
    initialUserIndex: Int = 0,
    initialMediaIndex: Int = 0,
    includeMyStatus: Boolean = false,
    onBack: () -> Unit = {},
    onBoostClick: (creativeUrl: String) -> Unit = {}
) {
    val userChains = remember(includeMyStatus) {
        if (includeMyStatus) StatusData.allViewableStatuses else StatusData.viewableStatuses
    }
    var userIndex by rememberSaveable { mutableIntStateOf(initialUserIndex.coerceIn(0, userChains.lastIndex)) }
    var mediaIndex by rememberSaveable { mutableIntStateOf(initialMediaIndex.coerceIn(0, userChains.getOrNull(initialUserIndex)?.media?.lastIndex ?: 0)) }

    val firstOtherIndex = if (includeMyStatus) 1 else 0
    val boostTriggerIndex = firstOtherIndex + StatusData.BOOST_AFTER_USERS
    val canShowBoost = boostTriggerIndex <= userChains.lastIndex
    var showingBoost by rememberSaveable { mutableStateOf(false) }
    var hasSeenBoost by rememberSaveable { mutableStateOf(false) }

    val currentUser = userChains[userIndex]
    val progress = remember(userIndex, mediaIndex) { Animatable(0f) }
    val boostProgress = remember { Animatable(0f) }
    val boostData = remember { BoostStatusData.suggestedBoost }

    val dismissOffsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val dismissThresholdPx = with(density) { DISMISS_THRESHOLD_DP.dp.toPx() }
    val swipeUserThresholdPx = with(density) { SWIPE_USER_THRESHOLD_DP.dp.toPx() }

    val dismissProgress = (dismissOffsetY.value / dismissThresholdPx).coerceIn(0f, 1f)
    val contentScale = 1f - (dismissProgress * 0.15f)
    val contentAlpha = 1f - (dismissProgress * 0.4f)
    val cornerRadius = dismissProgress * 24f

    fun advancePastBoost() {
        showingBoost = false
        userIndex = boostTriggerIndex.coerceAtMost(userChains.lastIndex)
        mediaIndex = 0
    }

    fun goBackFromBoost() {
        showingBoost = false
    }

    fun advanceToNextUser() {
        if (canShowBoost && userIndex == boostTriggerIndex - 1) {
            hasSeenBoost = true
            showingBoost = true
        } else if (userIndex < userChains.lastIndex) {
            userIndex = userIndex + 1
            mediaIndex = 0
        } else {
            onBack()
        }
    }

    // Status progress animation
    LaunchedEffect(userIndex, mediaIndex, showingBoost) {
        if (showingBoost) return@LaunchedEffect
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = STATUS_DURATION_MS, easing = LinearEasing)
        )
        if (mediaIndex < currentUser.media.lastIndex) {
            mediaIndex++
        } else {
            advanceToNextUser()
        }
    }

    // Boost progress animation
    LaunchedEffect(showingBoost) {
        if (!showingBoost) return@LaunchedEffect
        boostProgress.snapTo(0f)
        boostProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = BOOST_DURATION_MS, easing = LinearEasing)
        )
        advancePastBoost()
    }

    fun goForward() {
        if (mediaIndex < currentUser.media.lastIndex) {
            mediaIndex++
        } else {
            advanceToNextUser()
        }
    }

    fun goBackMedia() {
        if (mediaIndex > 0) {
            mediaIndex--
        } else if (canShowBoost && hasSeenBoost && userIndex == boostTriggerIndex) {
            showingBoost = true
            userIndex = boostTriggerIndex - 1
            mediaIndex = userChains[boostTriggerIndex - 1].media.lastIndex
        } else if (userIndex > 0) {
            userIndex--
            mediaIndex = userChains[userIndex].media.lastIndex
        } else {
            onBack()
        }
    }

    fun goNextUser() {
        advanceToNextUser()
    }

    fun goPrevUser() {
        if (canShowBoost && hasSeenBoost && userIndex == boostTriggerIndex) {
            showingBoost = true
            userIndex = boostTriggerIndex - 1
            mediaIndex = userChains[boostTriggerIndex - 1].media.lastIndex
        } else if (userIndex > 0) {
            userIndex--
            mediaIndex = 0
        } else {
            onBack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, dismissOffsetY.value.roundToInt()) }
                .scale(contentScale)
                .graphicsLayer {
                    alpha = contentAlpha
                    shape = RoundedCornerShape(cornerRadius.dp)
                    clip = true
                }
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            val viewKey = if (showingBoost) boostTriggerIndex * 2 - 1 else userIndex * 2
            val boostViewKey = boostTriggerIndex * 2 - 1

            // Gesture overlay BEHIND content so interactive elements (buttons)
            // receive events first; unconsumed taps/swipes fall through here.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(showingBoost, userIndex, mediaIndex, userChains.size) {
                        val touchSlop = viewConfiguration.touchSlop

                        awaitEachGesture {
                            val down = awaitFirstDown()
                            val startPos = down.position
                            var isDragging = false
                            var isVerticalDrag = false
                            var totalX = 0f
                            var totalY = 0f

                            while (true) {
                                val event = awaitPointerEvent()
                                val change =
                                    event.changes.firstOrNull { it.id == down.id }
                                        ?: break

                                if (!change.pressed) {
                                    if (!isDragging) {
                                        if (showingBoost) {
                                            if (startPos.x < size.width / 2f) {
                                                goBackFromBoost()
                                            } else {
                                                advancePastBoost()
                                            }
                                        } else {
                                            if (startPos.x < size.width / 2f) {
                                                goBackMedia()
                                            } else {
                                                goForward()
                                            }
                                        }
                                    } else if (isVerticalDrag) {
                                        if (dismissOffsetY.value > dismissThresholdPx) {
                                            scope.launch {
                                                dismissOffsetY.animateTo(
                                                    targetValue = 2000f,
                                                    animationSpec = tween(250)
                                                )
                                                onBack()
                                            }
                                        } else {
                                            scope.launch {
                                                dismissOffsetY.animateTo(
                                                    targetValue = 0f,
                                                    animationSpec = spring(
                                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                                        stiffness = Spring.StiffnessMedium
                                                    )
                                                )
                                            }
                                        }
                                    } else {
                                        if (showingBoost) {
                                            if (totalX > swipeUserThresholdPx) {
                                                goBackFromBoost()
                                            } else if (totalX < -swipeUserThresholdPx) {
                                                advancePastBoost()
                                            }
                                        } else {
                                            if (totalX > swipeUserThresholdPx) {
                                                goPrevUser()
                                            } else if (totalX < -swipeUserThresholdPx) {
                                                goNextUser()
                                            }
                                        }
                                    }
                                    break
                                }

                                totalX = change.position.x - startPos.x
                                totalY = change.position.y - startPos.y

                                if (!isDragging && (abs(totalX) > touchSlop || abs(totalY) > touchSlop)) {
                                    isDragging = true
                                    isVerticalDrag = abs(totalY) > abs(totalX)
                                }

                                if (isDragging) {
                                    change.consume()
                                    if (isVerticalDrag) {
                                        scope.launch {
                                            dismissOffsetY.snapTo(
                                                totalY.coerceAtLeast(0f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
            )

            AnimatedContent(
                targetState = viewKey,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        ) + fadeIn(animationSpec = tween(300)))
                            .togetherWith(
                                slideOutHorizontally(
                                    targetOffsetX = { -it },
                                    animationSpec = tween(300)
                                ) + fadeOut(animationSpec = tween(300))
                            )
                    } else {
                        (slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        ) + fadeIn(animationSpec = tween(300)))
                            .togetherWith(
                                slideOutHorizontally(
                                    targetOffsetX = { it },
                                    animationSpec = tween(300)
                                ) + fadeOut(animationSpec = tween(300))
                            )
                    }.using(SizeTransform(clip = false))
                },
                label = "viewer_transition"
            ) { animatedKey ->
                val isBoostView = animatedKey == boostViewKey && canShowBoost

                if (isBoostView) {
                    BoostInterstitialContent(
                        boostData = boostData,
                        progress = boostProgress.value,
                        onBackClick = onBack,
                        onBoostClick = onBoostClick
                    )
                } else {
                    val animatedUserIndex = animatedKey / 2
                    val user = userChains[animatedUserIndex]
                    val isCurrent = animatedUserIndex == userIndex && !showingBoost
                    val activeMediaIndex = if (isCurrent) mediaIndex else user.media.lastIndex
                    val media = user.media.getOrElse(activeMediaIndex) { user.media.first() }
                    val activeProgress = if (isCurrent) progress.value else 1f

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {
                        StatusMedia(
                            imageUrl = media.imageUrl,
                            contentDescription = "${user.name}'s status"
                        )

                        StatusHeader(
                            user = user,
                            currentMediaIndex = activeMediaIndex,
                            mediaProgress = activeProgress,
                            onBackClick = onBack,
                            onMoreClick = {}
                        )

                        if (user.isMyStatus) {
                            MyStatusBottomBar(
                                viewCount = media.viewCount,
                                onBoostClick = { onBoostClick(media.imageUrl) },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                            )
                        } else {
                            StatusBottomBar(
                                isLiked = LikedStatuses.isLiked(media.imageUrl),
                                onLikeToggle = { LikedStatuses.toggle(media.imageUrl) },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                            )
                        }
                    }
                }
            }
        }
    }
}

// region Boost Interstitial

@Composable
private fun BoostInterstitialContent(
    boostData: BoostableStatus,
    progress: Float,
    onBackClick: () -> Unit,
    onBoostClick: (creativeUrl: String) -> Unit
) {
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(boostDarkBg)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Progress bar
            Box(
                modifier = Modifier
                    .padding(
                        start = dimensions.wdsSpacingSingle,
                        end = dimensions.wdsSpacingSingle,
                        top = dimensions.wdsSpacingSingle
                    )
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x66FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = progress)
                        .clip(RoundedCornerShape(2.5.dp))
                        .background(Color.White)
                )
            }

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = dimensions.wdsSpacingHalf),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Suggested to boost",
                    style = typography.body1Emphasized,
                    color = Color.White
                )
            }

            // Content area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))

                BoostCardPreview(
                    boostData = boostData,
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onBoostClick(boostData.creativeImageUrl)
                    }
                )

                Spacer(modifier = Modifier.height(dimensions.wdsSpacingTriple))

                Text(
                    text = "Create an ad with this\npopular status",
                    style = typography.headline2,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onBoostClick(boostData.creativeImageUrl)
                    },
                    modifier = Modifier.padding(horizontal = 48.dp),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1B1B1B)
                    )
                ) {
                    Text(
                        text = "Boost this status",
                        style = typography.body2Emphasized,
                        modifier = Modifier.padding(
                            horizontal = dimensions.wdsSpacingSingle,
                            vertical = dimensions.wdsSpacingHalf
                        )
                    )
                }

                Spacer(modifier = Modifier.height(dimensions.wdsSpacingTriple))
            }
        }
    }
}

@Composable
private fun BoostCardPreview(boostData: BoostableStatus, onClick: () -> Unit = {}) {
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val context = LocalContext.current

    Surface(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .fillMaxWidth()
            .aspectRatio(9f / 16f),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        color = Color(0xFF1A2024)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(boostData.creativeImageUrl)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = "Status preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x55000000),
                                Color(0x22000000),
                                Color.Transparent
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensions.wdsSpacingSinglePlus),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(boostData.businessAvatarUrl)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = boostData.businessName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4DB6AC))
                )
                Spacer(modifier = Modifier.width(dimensions.wdsSpacingSingle))
                Text(
                    text = boostData.businessName,
                    style = typography.body2Emphasized,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(dimensions.wdsSpacingSinglePlus),
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = dimensions.wdsSpacingSinglePlus,
                            vertical = dimensions.wdsSpacingSingle
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_whatsapp_logo),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF25D366)
                        )
                        Text(
                            text = "CHAT ON WHATSAPP",
                            style = typography.body3Emphasized,
                            color = Color(0xFF1B1B1B)
                        )
                    }
                }
            }
        }
    }
}

// endregion

// region Media

@Composable
private fun StatusMedia(imageUrl: String?, contentDescription: String) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// endregion

// region Header

@Composable
private fun StatusHeader(
    user: StatusItem,
    currentMediaIndex: Int,
    mediaProgress: Float,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val context = LocalContext.current
    val currentMedia = user.media.getOrNull(currentMediaIndex)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x99000000),
                        Color(0x66000000),
                        Color(0x33000000),
                        Color.Transparent
                    )
                )
            )
            .padding(top = dimensions.wdsSpacingSingle)
    ) {
        SegmentedProgressBar(
            totalSegments = user.media.size,
            currentSegment = currentMediaIndex,
            currentProgress = mediaProgress,
            modifier = Modifier.padding(horizontal = dimensions.wdsSpacingSingle)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = dimensions.wdsSpacingHalf),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(user.avatarUrl)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = user.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF546E7A))
                )
                if (user.isMyStatus) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .offset(x = 2.dp, y = 2.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add status",
                            tint = Color(0xFF3B4A54),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(dimensions.wdsSpacingSingle))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (user.isMyStatus) "My status" else user.name,
                    style = typography.chatHeaderTitle,
                    color = Color.White
                )
                val timeText = currentMedia?.timeAgo.orEmpty()
                if (timeText.isNotEmpty()) {
                    Text(
                        text = timeText,
                        style = typography.body3,
                        color = Color.White
                    )
                }
            }

            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "More options",
                    tint = Color.White
                )
            }
        }
    }
}

// endregion

// region Progress Bar

@Composable
private fun SegmentedProgressBar(
    totalSegments: Int,
    currentSegment: Int,
    currentProgress: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(totalSegments) { index ->
            val segmentFraction = when {
                index < currentSegment -> 1f
                index == currentSegment -> currentProgress
                else -> 0f
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x66FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = segmentFraction)
                        .clip(RoundedCornerShape(2.5.dp))
                        .background(Color.White)
                )
            }
        }
    }
}

// endregion

// region Bottom Bar

@Composable
private fun StatusBottomBar(
    isLiked: Boolean,
    onLikeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensions.wdsSpacingSingle),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(Color(0xFF20272B)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Reply",
                style = typography.body1,
                color = Color(0xFFE9EDEF),
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
            )
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF20272B))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onLikeToggle
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLiked) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Unlike",
                    tint = colors.colorActivityIndicator,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = Color.White,
                    modifier = Modifier.size(dimensions.wdsIconSizeMedium)
                )
            }
        }
    }
}

// endregion

// region My Status Bottom Bar

@Composable
private fun MyStatusBottomBar(
    viewCount: Int,
    onBoostClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.wdsSpacingSingle, vertical = dimensions.wdsSpacingSingle),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(Color(0xFF2A3137)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$viewCount views",
                style = typography.body1,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .height(48.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(Color(0xFF2A3137))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBoostClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Campaign,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Boost",
                    style = typography.body1,
                    color = Color.White
                )
            }
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF2A3137))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_forward),
                contentDescription = "Forward",
                tint = Color.White,
                modifier = Modifier.size(dimensions.wdsIconSizeMedium)
            )
        }
    }
}

// endregion