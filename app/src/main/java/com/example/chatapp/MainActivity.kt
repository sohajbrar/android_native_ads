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
import com.example.chatapp.data.local.entity.ConversationEntity
import com.example.chatapp.features.main.MainViewModel
import com.example.chatapp.features.chat.ChatScreen
import com.example.chatapp.features.chatlist.ChatListScreen
import com.example.chatapp.features.broadcast.BroadcastChatScreen
import com.example.chatapp.features.broadcast.BroadcastDraftScreen
import com.example.chatapp.features.broadcast.BroadcastHomeScreen
import com.example.chatapp.features.broadcast.BroadcastInfoScreen
import com.example.chatapp.features.broadcast.BroadcastReviewScreen
import com.example.chatapp.features.broadcast.NewBusinessBroadcastScreen
import com.example.chatapp.features.broadcast.SelectRecipientsScreen
import com.example.chatapp.features.newchat.NewChatScreen
import com.example.chatapp.features.tools.ToolsScreen
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

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WdsTheme.colors.colorSurfaceDefault)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "chat_list",
                        enterTransition = {
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
                        },
                        exitTransition = {
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
                        },
                        popEnterTransition = {
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
                        },
                        popExitTransition = {
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
                            onCallsClick = { /* Calls not implemented yet */ },
                            onUpdatesClick = { /* Updates not implemented yet */ },
                            onToolsClick = { /* Already on Tools */ },
                            onBroadcastClick = {
                                navController.navigate(Screen.BroadcastHome.route)
                            }
                        )
                    }
                    
                    composable(
                        route = Screen.BroadcastHome.route
                    ) {
                        BroadcastHomeScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onNewBroadcastClick = {
                                navController.navigate(Screen.NewBusinessBroadcast.route)
                            }
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
