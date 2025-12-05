package com.example.chatapp.features.chatlist
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.initializer.DatabaseInitializer
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.wds.components.MessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import javax.inject.Inject
import kotlin.math.absoluteValue
import com.example.chatapp.data.local.entity.MessageType as EntityMessageType
import com.example.chatapp.data.generator.ChatDataGenerator

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val databaseInitializer: DatabaseInitializer
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()
    
    init {
        initializeAndLoadConversations()
    }
    
    private fun initializeAndLoadConversations() {
        Log.d("ChatListViewModel", "Starting initializeAndLoadConversations")
        viewModelScope.launch {
            // First ensure database is initialized
            try {
                Log.d("ChatListViewModel", "About to call databaseInitializer.initializeDatabase()")
                databaseInitializer.initializeDatabase()
                Log.d("ChatListViewModel", "Database initialized successfully")
                
                // Add a delay to ensure all database operations complete
                kotlinx.coroutines.delay(1000)
            } catch (e: Exception) {
                Log.e("ChatListViewModel", "Error initializing database", e)
            }
            
            // Then load conversations
            Log.d("ChatListViewModel", "About to call loadConversations()")
            loadConversations()
        }
    }
    
    private fun loadConversations() {
        Log.d("ChatListViewModel", "Starting loadConversations")
        viewModelScope.launch {
            try {
                Log.d("ChatListViewModel", "Inside loadConversations coroutine")
                // Get current user (assuming user with ID "user_1" is the current user)
                val currentUserId = "user_1"
                
                // Load all conversations with participants
                chatRepository.getAllConversations().collect { conversations ->
                    try {
                        Log.d("ChatListViewModel", "Flow emitted: Loaded ${conversations.size} conversations from database")
                        // Log unread counts to verify updates
                        conversations.take(10).forEach { conv ->
                            Log.d("ChatListViewModel", "Conv ${conv.conversationId}: unreadCount=${conv.unreadCount}, lastViewedAt=${conv.lastViewedAt}")
                        }
                        
                        // Load all users to get their avatar URLs
                        val allUsers = chatRepository.getAllUsers().first()
                        val userMap = allUsers.associateBy { it.userId }
                        Log.d("ChatListViewModel", "Loaded ${allUsers.size} users")
                        
                        // Load all participants for all conversations
                        val conversationParticipants = mutableMapOf<String, List<com.example.chatapp.data.local.entity.ConversationParticipantEntity>>()
                        for (conv in conversations) {
                            val participants = chatRepository.getConversationParticipants(conv.conversationId).first()
                            conversationParticipants[conv.conversationId] = participants
                        }
                        
                        // Track used names and avatars to ensure uniqueness
                        val usedNames = mutableSetOf<String>()
                        val nameCounter = mutableMapOf<String, Int>()
                        
                        // Create a list of avatar indices for guaranteed uniqueness (no shuffling)
                        val totalAvatars = 70 // We have 70 individual avatars in ChatDataGenerator (50 original + 20 new)
                        val availableAvatarIndices = (0 until totalAvatars).toMutableList()
                        var avatarIndexCounter = 0
                        
                        val conversationUiModels = conversations.mapNotNull { conversation ->
                            try {
                                Log.d("ChatListViewModel", "Processing conversation: ${conversation.conversationId}, title: ${conversation.title}, lastMessage: ${conversation.lastMessageText}, lastMessageTimestamp: ${conversation.lastMessageTimestamp}")
                                
                                // For now, display conversations even without lastMessage to debug
                                // if (conversation.lastMessageText != null && conversation.lastMessageTimestamp != null) {
                                if (true) { // Temporarily show all conversations
                                    // For direct messages, get the actual participant's name from the database
                                    val displayTitle = if (conversation.title.isNullOrEmpty()) {
                                        // Get the other participant's actual name from the database
                                        val participants = conversationParticipants[conversation.conversationId] ?: emptyList()
                                        val otherParticipant = participants.firstOrNull { it.userId != currentUserId }
                                        
                                        if (otherParticipant != null) {
                                            // Use the actual user's display name from the database
                                            val user = userMap[otherParticipant.userId]
                                            user?.displayName ?: "Unknown User"
                                        } else {
                                            "Unknown User"
                                        }
                                    } else {
                                        conversation.title
                                    }
                                    
                                    // Generate appropriate avatar URL based on conversation type
                                    val avatarUrl = if (conversation.isGroup) {
                                        // For groups, use the group's avatar URL or generate one based on group name
                                        // Extract conversation index from ID for consistent avatar selection
                                        val convIndex = conversation.conversationId.removePrefix("conv_").toIntOrNull() ?: 0
                                        conversation.avatarUrl ?: ChatDataGenerator.generateGroupAvatarUrl(displayTitle, convIndex)
                                    } else {
                                        // For direct messages, try to get the other participant's avatar
                                        val participants = conversationParticipants[conversation.conversationId] ?: emptyList()
                                        val otherParticipant = participants.firstOrNull { it.userId != currentUserId }
                                        
                                        if (otherParticipant != null) {
                                            // Get the user's avatar from the user map
                                            userMap[otherParticipant.userId]?.avatarUrl ?: ChatDataGenerator.generateIndividualAvatarUrl(
                                                conversation.conversationId,
                                                conversation.conversationId
                                            )
                                        } else {
                                            // Fallback to generated avatar
                                            ChatDataGenerator.generateIndividualAvatarUrl(
                                                conversation.conversationId,
                                                conversation.conversationId
                                            )
                                        }
                                    }
                                    
                                    // Determine message type from the lastMessageText content
                                    val lastMessageType = when {
                                        conversation.lastMessageText?.contains("📷 Photo") == true -> MessageType.PHOTO
                                        conversation.lastMessageText?.contains("📹 Video") == true -> MessageType.VIDEO
                                        conversation.lastMessageText?.contains("🎵 Audio") == true -> MessageType.AUDIO
                                        conversation.lastMessageText?.contains("📎") == true -> MessageType.FILE
                                        conversation.lastMessageText?.contains("📍") == true -> MessageType.LOCATION
                                        conversation.lastMessageText?.contains("Sticker") == true -> MessageType.STICKER
                                        conversation.lastMessageText?.contains("GIF") == true -> MessageType.GIF
                                        conversation.lastMessageText?.contains("🎤 Voice message") == true -> MessageType.VOICE_NOTE
                                        conversation.lastMessageText?.contains("🔗") == true -> MessageType.DOCUMENT
                                        conversation.lastMessageText?.contains("Missed Call") == true -> MessageType.VOICE_CALL
                                        conversation.lastMessageText?.contains("Call") == true && !conversation.lastMessageText.contains("Missed") -> MessageType.VOICE_CALL
                                        else -> MessageType.TEXT
                                    }
                                    
                                    ConversationUiModel(
                                        id = conversation.conversationId,
                                        title = displayTitle,
                                        avatarUrl = avatarUrl,
                                        lastMessage = conversation.lastMessageText ?: "Start a conversation",
                                        lastMessageTime = Instant.fromEpochMilliseconds(conversation.lastMessageTimestamp ?: System.currentTimeMillis()),
                                        lastMessageSender = null, // Would need to fetch from participants
                                        lastMessageType = lastMessageType,
                                        unreadCount = conversation.unreadCount,
                                        isGroup = conversation.isGroup,
                                        isSentByUser = false, // Would need to check against current user
                                        isRead = conversation.unreadCount == 0,
                                        hasUnread = conversation.unreadCount > 0,
                                        isMissedCall = conversation.lastMessageText?.contains("Missed Call") == true,
                                        isPinned = conversation.isPinned
                                    )
                                } else null
                            } catch (e: Exception) {
                                Log.e("ChatListViewModel", "Error processing conversation ${conversation.conversationId}", e)
                                null
                            }
                        }
                            .distinctBy { it.id } // Ensure no duplicate conversation IDs
                            .let { conversations ->
                                // Log the raw conversation data before sorting
                                Log.d("ChatListViewModel", "=== BEFORE SORTING ===")
                                conversations.take(10).forEach { conv ->
                                    Log.d("ChatListViewModel", "Conv ${conv.id}: lastMessageTime = ${conv.lastMessageTime}, unread = ${conv.unreadCount}")
                                }
                                
                                // Separate pinned and unpinned conversations
                                val pinned = conversations.filter { it.isPinned }
                                val unpinned = conversations.filter { !it.isPinned }
                                
                                // Sort unpinned conversations by recency (most recent first)
                                val sortedUnpinned = unpinned.sortedByDescending { it.lastMessageTime }
                                
                                // Log the sorted order
                                Log.d("ChatListViewModel", "=== AFTER SORTING ===")
                                sortedUnpinned.take(10).forEach { conv ->
                                    Log.d("ChatListViewModel", "Conv ${conv.id}: lastMessageTime = ${conv.lastMessageTime}, unread = ${conv.unreadCount}")
                                }
                                
                                // Combine: pinned first (maintaining their order), then sorted unpinned
                                pinned + sortedUnpinned
                            }
                        
                        Log.d("ChatListViewModel", "Created ${conversationUiModels.size} conversation UI models after deduplication")
                        
                        // Log unique titles to help debug duplicates
                        val uniqueTitles = conversationUiModels.map { it.title }.distinct()
                        Log.d("ChatListViewModel", "Unique conversation titles: $uniqueTitles")
                        
                        // Calculate counts
                        val unreadCount = conversationUiModels.sumOf { it.unreadCount }
                        val groupsCount = conversationUiModels.count { it.isGroup }
                        val archivedCount = 1 // Hardcoded for demo
                        
                        // Log state before and after update
                        val oldState = _uiState.value
                        Log.d("ChatListViewModel", "Updating UI state...")
                        Log.d("ChatListViewModel", "Old state: ${oldState.conversations.take(3).map { "${it.id}: unread=${it.unreadCount}" }}")
                        Log.d("ChatListViewModel", "New models: ${conversationUiModels.take(3).map { "${it.id}: unread=${it.unreadCount}" }}")
                        
                        _uiState.update { currentState ->
                            val newState = currentState.copy(
                                conversations = filterConversations(conversationUiModels, currentState.selectedFilter),
                                allConversations = conversationUiModels,
                                unreadCount = unreadCount,
                                groupsCount = groupsCount,
                                archivedCount = archivedCount,
                                isLoading = false
                            )
                            Log.d("ChatListViewModel", "State updated. New conversations: ${newState.conversations.take(3).map { "${it.id}: unread=${it.unreadCount}" }}")
                            newState
                        }
                    } catch (e: Exception) {
                        Log.e("ChatListViewModel", "Error processing conversations", e)
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatListViewModel", "Error loading conversations", e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    
    fun selectFilter(filter: ChatFilter) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedFilter = filter,
                conversations = filterConversations(currentState.allConversations, filter)
            )
        }
    }
    
    private fun filterConversations(
        conversations: List<ConversationUiModel>,
        filter: ChatFilter
    ): List<ConversationUiModel> {
        return when (filter) {
            // ALL filter shows only 1-to-1 business-consumer threads (excludes groups)
            ChatFilter.ALL -> conversations.filter { !it.isGroup }
            ChatFilter.UNREAD -> conversations.filter { it.unreadCount > 0 }
            ChatFilter.FAVORITES -> conversations.filter { it.isPinned }
            ChatFilter.GROUPS -> conversations.filter { it.isGroup }
        }
    }
}

data class ChatListUiState(
    val conversations: List<ConversationUiModel> = emptyList(),
    val allConversations: List<ConversationUiModel> = emptyList(),
    val selectedFilter: ChatFilter = ChatFilter.ALL,
    val unreadCount: Int = 0,
    val groupsCount: Int = 0,
    val archivedCount: Int = 0,
    val isLoading: Boolean = true
)
