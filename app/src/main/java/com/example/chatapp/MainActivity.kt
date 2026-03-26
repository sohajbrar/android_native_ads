package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.chatapp.data.local.entity.ConversationEntity
import com.example.chatapp.features.main.MainViewModel
import com.example.chatapp.features.chat.ChatScreen
import com.example.chatapp.features.chatlist.ChatListScreen
import com.example.chatapp.features.advertise.AdCreationViewModel
import com.example.chatapp.features.advertise.AudienceScreen
import com.example.chatapp.features.advertise.ChooseCatalogScreen
import com.example.chatapp.features.advertise.ChooseStatusScreen
import com.example.chatapp.features.advertise.CreateNewAudienceScreen
import com.example.chatapp.features.advertise.BudgetScreen
import com.example.chatapp.features.advertise.DesignAdScreen
import com.example.chatapp.features.advertise.EditInterestsScreen
import com.example.chatapp.features.advertise.EditLocationScreen
import com.example.chatapp.features.advertise.AdDetailsScreen
import com.example.chatapp.features.advertise.AdPerformanceScreen
import com.example.chatapp.features.advertise.ManageAdsScreen
import com.example.chatapp.features.advertise.ManageAdsViewModel
import com.example.chatapp.features.advertise.MediaSelectionScreen
import com.example.chatapp.features.advertise.PreviewAdScreen
import com.example.chatapp.features.advertise.ReviewAdScreen
import com.example.chatapp.features.broadcast.BroadcastChatScreen
import com.example.chatapp.features.broadcast.BroadcastDraftScreen
import com.example.chatapp.features.broadcast.BroadcastHomeScreen
import com.example.chatapp.features.broadcast.BroadcastInfoScreen
import com.example.chatapp.features.broadcast.BroadcastReviewScreen
import com.example.chatapp.features.broadcast.MessageDetailsScreen
import com.example.chatapp.features.broadcast.NewBusinessBroadcastScreen
import com.example.chatapp.features.broadcast.SelectRecipientsScreen
import com.example.chatapp.features.newchat.NewChatScreen
import com.example.chatapp.features.tools.ToolsScreen
import com.example.chatapp.features.updates.UpdatesScreen
import com.example.chatapp.features.updates.StatusViewerScreen

import com.example.chatapp.navigation.Screen
import com.example.chatapp.ui.screens.DesignSystemLibraryScreen
import com.example.chatapp.ui.screens.ColorsScreen
import com.example.chatapp.ui.screens.TypeScreen
import com.example.chatapp.ui.screens.ComponentsScreen
import com.example.chatapp.ui.screens.IconsScreen
import dagger.hilt.android.AndroidEntryPoint
import com.example.chatapp.wds.theme.WdsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WdsTheme {
                val navController = rememberNavController()

                val tabRoutes = remember { setOf("chat_list", Screen.Tools.route, Screen.Updates.route) }
                var dismissDown by remember { mutableStateOf(false) }
                var pendingBoostMediaUri by remember { mutableStateOf<String?>(null) }
                var enteredAdFlowFromBoost by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WdsTheme.colors.colorSurfaceDefault)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "chat_list",
                        enterTransition = {
                            val betweenTabs = initialState.destination.route in tabRoutes &&
                                    targetState.destination.route in tabRoutes
                            if (betweenTabs) {
                                EnterTransition.None
                            } else {
                                slideInHorizontally(
                                    initialOffsetX = { it },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(
                                        durationMillis = 150,
                                        delayMillis = 150
                                    )
                                )
                            }
                        },
                        exitTransition = {
                            val betweenTabs = initialState.destination.route in tabRoutes &&
                                    targetState.destination.route in tabRoutes
                            if (betweenTabs) {
                                ExitTransition.None
                            } else {
                                slideOutHorizontally(
                                    targetOffsetX = { -it / 3 },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(
                                        durationMillis = 150
                                    )
                                )
                            }
                        },
                        popEnterTransition = {
                            val betweenTabs = initialState.destination.route in tabRoutes &&
                                    targetState.destination.route in tabRoutes
                            if (betweenTabs || dismissDown) {
                                EnterTransition.None
                            } else {
                                slideInHorizontally(
                                    initialOffsetX = { -it / 3 },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(
                                        durationMillis = 150,
                                        delayMillis = 150
                                    )
                                )
                            }
                        },
                        popExitTransition = {
                            val betweenTabs = initialState.destination.route in tabRoutes &&
                                    targetState.destination.route in tabRoutes
                            if (betweenTabs) {
                                ExitTransition.None
                            } else if (dismissDown) {
                                dismissDown = false
                                slideOutVertically(
                                    targetOffsetY = { it },
                                    animationSpec = tween(
                                        durationMillis = 350,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(
                                        durationMillis = 200
                                    )
                                )
                            } else {
                                slideOutHorizontally(
                                    targetOffsetX = { it },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(
                                        durationMillis = 150
                                    )
                                )
                            }
                        }
                    ) {

                    composable(
                        route = "chat_list"
                    ) {
                        ChatListScreen(
                            onChatClick = { conversationId ->
                                navController.navigate("chat/$conversationId")
                            },
                            onBroadcastChatClick = { conversationId, title, recipientCount, linkedListCount ->
                                val encodedConversationId = java.net.URLEncoder.encode(conversationId, "UTF-8")
                                val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
                                navController.navigate(
                                    "${Screen.BroadcastChat.route}/$encodedConversationId/$encodedTitle/$recipientCount/$linkedListCount"
                                )
                            },
                            onNewChatClick = {
                                navController.navigate(Screen.NewChat.route)
                            },
                            onDesignLibraryClick = {
                                navController.navigate(Screen.DesignSystemLibrary.route)
                            },
                            onToolsClick = {
                                navController.navigate(Screen.Tools.route) {
                                    popUpTo("chat_list") {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onUpdatesClick = {
                                navController.navigate(Screen.Updates.route) {
                                    popUpTo("chat_list") {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                    
                    composable(
                        route = Screen.NewChat.route
                    ) {
                        NewChatScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onContactClick = { userId ->
                                navController.popBackStack()
                            },
                            onNewBroadcastClick = {
                                navController.navigate(Screen.NewBusinessBroadcast.route)
                            }
                        )
                    }

                    composable(
                        route = Screen.NewBusinessBroadcast.route
                    ) {
                        NewBusinessBroadcastScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onNewAudienceClick = {
                                navController.navigate(Screen.SelectRecipients.route)
                            }
                        )
                    }

                    composable(
                        route = Screen.SelectRecipients.route
                    ) {
                        SelectRecipientsScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onNextClick = { conversationId, title, recipientCount, linkedListCount ->
                                val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
                                val encodedConversationId = java.net.URLEncoder.encode(conversationId, "UTF-8")
                                navController.navigate(
                                    "${Screen.BroadcastChat.route}/$encodedConversationId/$encodedTitle/$recipientCount/$linkedListCount"
                                ) {
                                    popUpTo("chat_list") { inclusive = false }
                                }
                            }
                        )
                    }

                    composable(
                        route = "${Screen.BroadcastChat.route}/{conversationId}/{title}/{recipientCount}/{linkedListCount}?sentMessage={sentMessage}",
                        arguments = listOf(
                            navArgument("conversationId") { type = NavType.StringType },
                            navArgument("title") { type = NavType.StringType },
                            navArgument("recipientCount") { type = NavType.IntType },
                            navArgument("linkedListCount") { type = NavType.IntType },
                            navArgument("sentMessage") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            }
                        )
                    ) { backStackEntry ->
                        val conversationId = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("conversationId") ?: "",
                            "UTF-8"
                        )
                        val title = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("title") ?: "",
                            "UTF-8"
                        )
                        val recipientCount = backStackEntry.arguments?.getInt("recipientCount") ?: 0
                        val linkedListCount = backStackEntry.arguments?.getInt("linkedListCount") ?: 0

                        val newSentMessage by backStackEntry.savedStateHandle
                            .getStateFlow<String?>("newSentMessage", null)
                            .collectAsState()

                        BroadcastChatScreen(
                            conversationId = conversationId,
                            title = title,
                            recipientCount = recipientCount,
                            linkedListCount = linkedListCount,
                            sentMessage = newSentMessage,
                            onBackClick = { navController.popBackStack() },
                            onHeaderClick = {
                                val encodedConversationId = java.net.URLEncoder.encode(conversationId, "UTF-8")
                                val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
                                navController.navigate(
                                    "${Screen.BroadcastInfo.route}/$encodedConversationId/$encodedTitle/$recipientCount/$linkedListCount"
                                )
                            },
                            onNextClick = { messageText ->
                                val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
                                val encodedConversationId = java.net.URLEncoder.encode(conversationId, "UTF-8")
                                val encodedMessage = java.net.URLEncoder.encode(messageText, "UTF-8")
                                navController.navigate(
                                    "${Screen.BroadcastDraft.route}/$encodedConversationId/$encodedTitle/$encodedMessage/$recipientCount/$linkedListCount"
                                )
                            },
                            onMessageProcessed = {
                                backStackEntry.savedStateHandle.remove<String>("newSentMessage")
                            }
                        )
                    }

                    composable(
                        route = "${Screen.BroadcastInfo.route}/{conversationId}/{title}/{recipientCount}/{linkedListCount}",
                        arguments = listOf(
                            navArgument("conversationId") { type = NavType.StringType },
                            navArgument("title") { type = NavType.StringType },
                            navArgument("recipientCount") { type = NavType.IntType },
                            navArgument("linkedListCount") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val infoConversationId = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("conversationId") ?: "",
                            "UTF-8"
                        )
                        val infoTitle = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("title") ?: "",
                            "UTF-8"
                        )
                        val infoRecipientCount = backStackEntry.arguments?.getInt("recipientCount") ?: 0
                        val infoLinkedListCount = backStackEntry.arguments?.getInt("linkedListCount") ?: 0
                        BroadcastInfoScreen(
                            conversationId = infoConversationId,
                            title = infoTitle,
                            recipientCount = infoRecipientCount,
                            linkedListCount = infoLinkedListCount,
                            onBackClick = { navController.popBackStack() },
                            onDeleteBroadcast = {
                                navController.popBackStack("chat_list", inclusive = false)
                            }
                        )
                    }

                    composable(
                        route = "${Screen.BroadcastDraft.route}/{conversationId}/{title}/{messageText}/{recipientCount}/{linkedListCount}",
                        arguments = listOf(
                            navArgument("conversationId") { type = NavType.StringType },
                            navArgument("title") { type = NavType.StringType },
                            navArgument("messageText") { type = NavType.StringType },
                            navArgument("recipientCount") { type = NavType.IntType },
                            navArgument("linkedListCount") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val conversationId = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("conversationId") ?: "",
                            "UTF-8"
                        )
                        val draftTitle = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("title") ?: "",
                            "UTF-8"
                        )
                        val messageText = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("messageText") ?: "",
                            "UTF-8"
                        )
                        val recipientCount = backStackEntry.arguments?.getInt("recipientCount") ?: 0
                        val linkedListCount = backStackEntry.arguments?.getInt("linkedListCount") ?: 0
                        BroadcastDraftScreen(
                            title = draftTitle,
                            messageText = messageText,
                            onBackClick = { navController.popBackStack() },
                            onNextClick = {
                                val encodedConversationId = java.net.URLEncoder.encode(conversationId, "UTF-8")
                                val encodedTitle = java.net.URLEncoder.encode(draftTitle, "UTF-8")
                                val encodedMessage = java.net.URLEncoder.encode(messageText, "UTF-8")
                                navController.navigate(
                                    "${Screen.BroadcastReview.route}/$encodedConversationId/$encodedTitle/$encodedMessage/$recipientCount/$linkedListCount"
                                )
                            }
                        )
                    }

                    composable(
                        route = "${Screen.BroadcastReview.route}/{conversationId}/{title}/{messageText}/{recipientCount}/{linkedListCount}",
                        arguments = listOf(
                            navArgument("conversationId") { type = NavType.StringType },
                            navArgument("title") { type = NavType.StringType },
                            navArgument("messageText") { type = NavType.StringType },
                            navArgument("recipientCount") { type = NavType.IntType },
                            navArgument("linkedListCount") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val conversationId = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("conversationId") ?: "",
                            "UTF-8"
                        )
                        val title = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("title") ?: "",
                            "UTF-8"
                        )
                        val messageText = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("messageText") ?: "",
                            "UTF-8"
                        )
                        val recipientCount = backStackEntry.arguments?.getInt("recipientCount") ?: 0
                        val linkedListCount = backStackEntry.arguments?.getInt("linkedListCount") ?: 0
                        BroadcastReviewScreen(
                            messageText = messageText,
                            recipientCount = recipientCount,
                            onBackClick = { navController.popBackStack() },
                            onEditAudienceClick = { },
                            onScheduleClick = { },
                            onSendNowClick = {
                                val chatRoute = "${Screen.BroadcastChat.route}/{conversationId}/{title}/{recipientCount}/{linkedListCount}?sentMessage={sentMessage}"
                                try {
                                    navController.getBackStackEntry(chatRoute)
                                        .savedStateHandle["newSentMessage"] = messageText
                                } catch (_: Exception) { }
                                navController.popBackStack(chatRoute, inclusive = false)
                            }
                        )
                    }

                    // Updates Screen
                    composable(
                        route = Screen.Updates.route
                    ) {
                        UpdatesScreen(
                            onNavigateToChats = {
                                navController.navigate("chat_list") {
                                    popUpTo("chat_list") {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onStatusClick = { statusIndex ->
                                navController.navigate(Screen.StatusViewer.createRoute(statusIndex))
                            },
                            onMyStatusClick = {
                                navController.navigate(Screen.MyStatusDetail.route)
                            },
                            onCallsClick = { },
                            onToolsClick = {
                                navController.navigate(Screen.Tools.route) {
                                    popUpTo("chat_list") {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }

                    // My Status Detail screen
                    composable(
                        route = Screen.MyStatusDetail.route
                    ) {
                        com.example.chatapp.features.updates.MyStatusScreen(
                            onBack = { navController.popBackStack() },
                            onStatusClick = { mediaIndex ->
                                navController.navigate(
                                    Screen.StatusViewer.createRoute(
                                        statusIndex = 0,
                                        includeMyStatus = true
                                    ) + "?mediaIndex=$mediaIndex"
                                )
                            }
                        )
                    }

                    // Status Viewer (includes boost interstitial inline)
                    composable(
                        route = Screen.StatusViewer.route + "?mediaIndex={mediaIndex}",
                        arguments = listOf(
                            navArgument("statusIndex") { type = NavType.IntType },
                            navArgument("includeMyStatus") { type = NavType.BoolType },
                            navArgument("mediaIndex") {
                                type = NavType.IntType
                                defaultValue = 0
                            }
                        )
                    ) { backStackEntry ->
                        val statusIndex = backStackEntry.arguments?.getInt("statusIndex") ?: 0
                        val includeMyStatus = backStackEntry.arguments?.getBoolean("includeMyStatus") ?: false
                        val mediaIndex = backStackEntry.arguments?.getInt("mediaIndex") ?: 0
                        StatusViewerScreen(
                            initialUserIndex = statusIndex,
                            initialMediaIndex = mediaIndex,
                            includeMyStatus = includeMyStatus,
                            onBack = {
                                dismissDown = true
                                navController.popBackStack()
                            },
                            onBoostClick = { creativeUrl ->
                                pendingBoostMediaUri = creativeUrl
                                enteredAdFlowFromBoost = true
                                navController.navigate("advertise_flow")
                            }
                        )
                    }

                    // Tools Screen
                    composable(
                        route = Screen.Tools.route
                    ) {
                        ToolsScreen(
                            selectedTab = 3,
                            onChatsClick = {
                                navController.navigate("chat_list") {
                                    popUpTo("chat_list") {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onCallsClick = { },
                            onUpdatesClick = {
                                navController.navigate(Screen.Updates.route) {
                                    popUpTo("chat_list") {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onToolsClick = { },
                            onBroadcastClick = {
                                navController.navigate(Screen.BroadcastHome.route)
                            },
                            onAdvertiseClick = {
                                navController.navigate("advertise_flow")
                            },
                            onManageAdsClick = {
                                navController.navigate(Screen.ManageAds.route)
                            }
                        )
                    }

                    composable(
                        route = Screen.ManageAds.route
                    ) {
                        val manageVm: ManageAdsViewModel = hiltViewModel()
                        ManageAdsScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onCreateAdClick = {
                                navController.navigate("advertise_flow")
                            },
                            onAdClick = { ad ->
                                manageVm.store.selectedAd = ad
                                navController.navigate(Screen.AdDetails.route)
                            },
                            createdAds = manageVm.store.ads,
                            showSnackbar = manageVm.store.pendingSnackbar,
                            onSnackbarShown = { manageVm.store.onSnackbarShown() }
                        )
                    }

                    composable(
                        route = Screen.AdDetails.route
                    ) {
                        val manageVm: ManageAdsViewModel = hiltViewModel()
                        val ad = manageVm.store.selectedAd
                        if (ad != null) {
                            AdDetailsScreen(
                                ad = ad,
                                onNavigateBack = { navController.popBackStack() },
                                onSeePerformanceDetails = {
                                    navController.navigate(Screen.AdPerformance.route)
                                },
                                onRecreateAd = {
                                    navController.navigate("advertise_flow")
                                },
                                onAdPreviewClick = {
                                    navController.navigate(Screen.AdPreviewFromDetails.route)
                                }
                            )
                        }
                    }

                    composable(
                        route = Screen.AdPreviewFromDetails.route
                    ) {
                        val manageVm: ManageAdsViewModel = hiltViewModel()
                        val ad = manageVm.store.selectedAd
                        PreviewAdScreen(
                            onNavigateBack = { navController.popBackStack() },
                            selectedMediaUri = ad?.imageUrl,
                            showCloseButton = true
                        )
                    }

                    composable(
                        route = Screen.AdPerformance.route
                    ) {
                        val manageVm: ManageAdsViewModel = hiltViewModel()
                        val ad = manageVm.store.selectedAd
                        if (ad != null) {
                            AdPerformanceScreen(
                                ad = ad,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                    
                    composable(
                        route = Screen.BroadcastHome.route
                    ) {
                        BroadcastHomeScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onNewBroadcastClick = {
                                navController.navigate(Screen.SelectRecipients.route)
                            },
                            onBroadcastClick = { messageContent, sentTimestamp, recipientCount ->
                                val encodedContent = java.net.URLEncoder.encode(messageContent, "UTF-8")
                                navController.navigate(
                                    "${Screen.MessageDetails.route}/$encodedContent/$sentTimestamp/$recipientCount"
                                )
                            },
                            onAudienceClick = { conversationId, title, recipientCount, linkedListCount ->
                                val encodedConversationId = java.net.URLEncoder.encode(conversationId, "UTF-8")
                                val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
                                navController.navigate(
                                    "${Screen.BroadcastChat.route}/$encodedConversationId/$encodedTitle/$recipientCount/$linkedListCount"
                                )
                            }
                        )
                    }

                    composable(
                        route = "${Screen.MessageDetails.route}/{messageContent}/{sentTimestamp}/{recipientCount}",
                        arguments = listOf(
                            navArgument("messageContent") { type = NavType.StringType },
                            navArgument("sentTimestamp") { type = NavType.LongType },
                            navArgument("recipientCount") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val messageContent = java.net.URLDecoder.decode(
                            backStackEntry.arguments?.getString("messageContent") ?: "",
                            "UTF-8"
                        )
                        val sentTimestamp = backStackEntry.arguments?.getLong("sentTimestamp") ?: 0L
                        val recipientCount = backStackEntry.arguments?.getInt("recipientCount") ?: 0

                        MessageDetailsScreen(
                            messageContent = messageContent,
                            sentTimestamp = sentTimestamp,
                            recipientCount = recipientCount,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "chat/{conversationId}",
                        arguments = listOf(
                            navArgument("conversationId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
                        ChatScreen(
                            conversationId = conversationId,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onChatInfoClick = {
                                navController.navigate("chat_info/$conversationId")
                            }
                        )
                    }

                    composable(
                        route = "chat_info/{conversationId}",
                        arguments = listOf(
                            navArgument("conversationId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
                        com.example.chatapp.features.chatinfo.ChatInfoScreen(
                            conversationId = conversationId,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onNavigate = { route ->
                                navController.navigate(route)
                            }
                        )
                    }
                    
                    // Advertise Flow (nested nav graph to share AdCreationViewModel)
                    navigation(
                        startDestination = Screen.AdvertiseMediaSelection.route,
                        route = "advertise_flow"
                    ) {
                        composable(
                            route = Screen.AdvertiseMediaSelection.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)

                            val boostUri = pendingBoostMediaUri
                            if (boostUri != null) {
                                LaunchedEffect(Unit) {
                                    pendingBoostMediaUri = null
                                    adVm.selectedMediaUri = boostUri
                                    navController.navigate(Screen.AdvertiseDesignAd.route) {
                                        popUpTo(Screen.AdvertiseMediaSelection.route) { inclusive = true }
                                    }
                                }
                            }

                            MediaSelectionScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNextClick = { mediaUri ->
                                    if (mediaUri != null) {
                                        adVm.selectedMediaUri = mediaUri
                                    }
                                    navController.navigate(Screen.AdvertiseDesignAd.route)
                                },
                                onChooseStatus = {
                                    navController.navigate(Screen.ChooseStatus.route)
                                },
                                onChooseCatalog = {
                                    navController.navigate(Screen.ChooseCatalog.route)
                                }
                            )
                        }

                        composable(
                            route = Screen.ChooseStatus.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            ChooseStatusScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onMediaConfirmed = { uri ->
                                    adVm.selectedMediaUri = uri.toString()
                                    navController.navigate(Screen.AdvertiseDesignAd.route)
                                }
                            )
                        }

                        composable(
                            route = Screen.ChooseCatalog.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            ChooseCatalogScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onMediaConfirmed = { uri ->
                                    adVm.selectedMediaUri = uri.toString()
                                    navController.navigate(Screen.AdvertiseDesignAd.route)
                                }
                            )
                        }

                        composable(
                            route = Screen.AdvertiseDesignAd.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            DesignAdScreen(
                                onNavigateBack = {
                                    if (enteredAdFlowFromBoost) {
                                        enteredAdFlowFromBoost = false
                                        navController.popBackStack("advertise_flow", inclusive = true)
                                    } else {
                                        navController.popBackStack()
                                    }
                                },
                                onNextClick = {
                                    navController.navigate(Screen.AdvertiseAudience.route)
                                },
                                onPreviewClick = {
                                    navController.navigate(Screen.AdvertisePreview.route)
                                },
                                selectedMediaUri = adVm.selectedMediaUri,
                                adViewModel = adVm
                            )
                        }

                        composable(
                            route = Screen.AdvertiseAudience.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            AudienceScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNextClick = {
                                    navController.navigate(Screen.AdvertiseBudget.route)
                                },
                                onCreateNewAudience = {
                                    adVm.editingAudienceId = null
                                    navController.navigate(Screen.CreateNewAudience.route)
                                },
                                onEditAudience = { audienceId ->
                                    adVm.editingAudienceId = audienceId
                                    navController.navigate(Screen.CreateNewAudience.route)
                                },
                                adViewModel = adVm
                            )
                        }

                        composable(
                            route = Screen.CreateNewAudience.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            CreateNewAudienceScreen(
                                onNavigateBack = {
                                    adVm.editingAudienceId = null
                                    navController.popBackStack()
                                },
                                onSave = { navController.popBackStack() },
                                onEditLocation = { currentLocation ->
                                    adVm.editingLocations.clear()
                                    adVm.editingLocations.addAll(
                                        currentLocation.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                    )
                                    navController.navigate(Screen.EditLocation.route)
                                },
                                onEditInterests = { currentInterests ->
                                    adVm.editingInterests.clear()
                                    adVm.editingInterests.addAll(
                                        currentInterests.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                    )
                                    navController.navigate(Screen.EditInterests.route)
                                },
                                adViewModel = adVm
                            )
                        }

                        composable(
                            route = Screen.EditInterests.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            EditInterestsScreen(
                                onNavigateBack = {
                                    adVm.editingInterests.clear()
                                    navController.popBackStack()
                                },
                                onSave = { interests ->
                                    adVm.editingInterests.clear()
                                    adVm.editingInterests.addAll(interests)
                                    navController.popBackStack()
                                },
                                initialInterests = adVm.editingInterests.toList()
                            )
                        }

                        composable(
                            route = Screen.EditLocation.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            EditLocationScreen(
                                onNavigateBack = {
                                    adVm.editingLocations.clear()
                                    navController.popBackStack()
                                },
                                onSave = { locations ->
                                    adVm.editingLocations.clear()
                                    adVm.editingLocations.addAll(locations)
                                    navController.popBackStack()
                                },
                                initialLocations = adVm.editingLocations.toList()
                            )
                        }

                        composable(
                            route = Screen.AdvertiseBudget.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            BudgetScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNextClick = {
                                    navController.navigate(Screen.AdvertiseReviewAd.route)
                                },
                                adViewModel = adVm
                            )
                        }

                        composable(
                            route = Screen.AdvertiseReviewAd.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            ReviewAdScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onCreateAdClick = {
                                    adVm.createAd()
                                    enteredAdFlowFromBoost = false
                                    dismissDown = true
                                    navController.popBackStack("advertise_flow", inclusive = true)
                                    val currentRoute = navController.currentBackStackEntry?.destination?.route
                                    if (currentRoute != Screen.ManageAds.route) {
                                        navController.navigate(Screen.ManageAds.route)
                                    }
                                },
                                onEditAdPreview = {
                                    navController.navigate(Screen.ReviewDesignAd.route)
                                },
                                onEditAudience = {
                                    navController.navigate(Screen.ReviewAudience.route)
                                },
                                onEditBudget = {
                                    navController.navigate(Screen.ReviewBudget.route)
                                },
                                adViewModel = adVm
                            )
                        }

                        composable(
                            route = Screen.AdvertisePreview.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            PreviewAdScreen(
                                onNavigateBack = { navController.popBackStack() },
                                selectedMediaUri = adVm.selectedMediaUri
                            )
                        }

                        // Review-mode screens (opened from ReviewAdScreen, dismiss returns to review)
                        composable(
                            route = Screen.ReviewDesignAd.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            DesignAdScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNextClick = { },
                                reviewMode = true,
                                selectedMediaUri = adVm.selectedMediaUri,
                                adViewModel = adVm
                            )
                        }

                        composable(
                            route = Screen.ReviewAudience.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            AudienceScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNextClick = { },
                                onCreateNewAudience = {
                                    adVm.editingAudienceId = null
                                    navController.navigate(Screen.CreateNewAudience.route)
                                },
                                onEditAudience = { audienceId ->
                                    adVm.editingAudienceId = audienceId
                                    navController.navigate(Screen.CreateNewAudience.route)
                                },
                                reviewMode = true,
                                adViewModel = adVm
                            )
                        }

                        composable(
                            route = Screen.ReviewBudget.route
                        ) { entry ->
                            val parentEntry = remember(entry) {
                                navController.getBackStackEntry("advertise_flow")
                            }
                            val adVm: AdCreationViewModel = hiltViewModel(parentEntry)
                            BudgetScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNextClick = { },
                                reviewMode = true,
                                adViewModel = adVm
                            )
                        }
                    }

                    // Design System Library Routes
                    composable(
                        route = Screen.DesignSystemLibrary.route
                    ) {
                        DesignSystemLibraryScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToColors = { navController.navigate(Screen.Colors.route) },
                            onNavigateToType = { navController.navigate(Screen.Type.route) },
                            onNavigateToComponents = { navController.navigate(Screen.Components.route) },
                            onNavigateToIcons = { navController.navigate(Screen.Icons.route) }
                        )
                    }
                    
                    composable(
                        route = Screen.Colors.route
                    ) {
                        ColorsScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    
                    composable(
                        route = Screen.Type.route
                    ) {
                        TypeScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    
                    composable(
                        route = Screen.Components.route
                    ) {
                        ComponentsScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    
                    composable(
                        route = Screen.Icons.route
                    ) {
                        IconsScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDatabaseTestScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val isDatabaseInitialized by viewModel.isDatabaseInitialized.collectAsState()
    val conversations by viewModel.conversations.collectAsState(initial = emptyList())
    val users by viewModel.users.collectAsState(initial = emptyList())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat Database Test") },
                actions = {
                    TextButton(
                        onClick = { viewModel.resetDatabase() },
                        enabled = !isLoading
                    ) {
                        Text("Reset DB")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                isDatabaseInitialized -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(WdsTheme.dimensions.wdsSpacingDouble),
                        verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
                    ) {
                        item {
                            DatabaseSummaryCard(
                                userCount = users.size,
                                conversationCount = conversations.size
                            )
                        }
                        
                        item {
                            Text(
                                text = "Recent Conversations",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = WdsTheme.dimensions.wdsSpacingSingle)
                            )
                        }
                        
                        items(conversations.take(10)) { conversation ->
                            ConversationCard(conversation)
                        }
                    }
                }
                else -> {
                    Text(
                        text = "Database not initialized",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun DatabaseSummaryCard(
    userCount: Int,
    conversationCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = WdsTheme.colors.colorAccentDeemphasized
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WdsTheme.dimensions.wdsSpacingDouble)
        ) {
            Text(
                text = "Database Summary",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingSingle))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = userCount.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Users",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = conversationCount.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Conversations",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationCard(conversation: ConversationEntity) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WdsTheme.dimensions.wdsSpacingSinglePlus)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = conversation.title ?: "Direct Message",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (conversation.unreadCount > 0) {
                    Badge {
                        Text(conversation.unreadCount.toString())
                    }
                }
            }
            
            conversation.lastMessageText?.let { lastMessage ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = WdsTheme.colors.colorContentDeemphasized
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)) {
                    if (conversation.isPinned) {
                        AssistChip(
                            onClick = { },
                            label = { Text("📌 Pinned") },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                    if (conversation.isMuted) {
                        AssistChip(
                            onClick = { },
                            label = { Text("🔇 Muted") },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                    if (conversation.isGroup) {
                        AssistChip(
                            onClick = { },
                            label = { Text("👥 Group") },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
            }
        }
    }
}
}
