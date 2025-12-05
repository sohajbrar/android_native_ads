package com.example.chatapp.features.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.data.local.entity.MessageType
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var conversationId: String = savedStateHandle.get<String>("conversationId") ?: ""

    // Current user ID - in a real app, this would come from auth service
    val currentUserId = "user_1"

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages.asStateFlow()

    private var conversationLastViewedAt: Long = 0L

    fun loadConversation(newConversationId: String) {
        // Update the conversation ID
        this.conversationId = newConversationId

        // Load conversation details to get lastViewedAt
        viewModelScope.launch {
            val conversation = chatRepository.getConversationById(newConversationId)
            conversationLastViewedAt = conversation?.lastViewedAt ?: 0L
        }

        // Start collecting messages for this conversation
        viewModelScope.launch {
            chatRepository.getConversationMessages(newConversationId)
                .collect { messageList ->
                    _messages.value = messageList

                    // Sort messages by timestamp to find the chronologically first unread
                    val sortedMessages = messageList.sortedBy { it.timestamp }

                    // Calculate unread messages - count incoming messages that arrived after lastViewedAt
                    val unreadIncomingMessages = sortedMessages.filter { message ->
                        message.senderId != currentUserId &&
                        message.timestamp > conversationLastViewedAt
                    }

                    if (unreadIncomingMessages.isNotEmpty()) {
                        val firstUnread = unreadIncomingMessages.first()
                        _uiState.update { it.copy(
                            unreadCount = unreadIncomingMessages.size,
                            firstUnreadMessageId = firstUnread.messageId
                        )}
                    } else {
                        _uiState.update { it.copy(
                            unreadCount = 0,
                            firstUnreadMessageId = null
                        )}
                    }
                }
        }

        // Load conversation details for header
        viewModelScope.launch {
            chatRepository.getConversationById(newConversationId)?.let { conversation ->
                if (conversation.isGroup) {
                    // For group chats, use the conversation title and avatar
                    _uiState.update { it.copy(
                        conversationTitle = conversation.title ?: "",
                        conversationAvatar = conversation.avatarUrl,
                        isGroupChat = true,
                        isBusinessChat = conversation.isBusinessChat
                    )}

                    // Load participant info for group chat
                    chatRepository.getConversationParticipants(newConversationId)
                        .collect { participants ->
                            val participantMap = mutableMapOf<String, ParticipantInfo>()
                            participants.forEach { participant ->
                                val user = chatRepository.getUserById(participant.userId)
                                if (user != null) {
                                    participantMap[participant.userId] = ParticipantInfo(
                                        userId = participant.userId,
                                        displayName = user.displayName,
                                        avatarUrl = user.avatarUrl
                                    )
                                }
                            }
                            _uiState.update { it.copy(participantInfo = participantMap) }
                        }
                } else {
                    // For 1:1 chats, get the other user's info
                    val participants = chatRepository.getConversationParticipants(newConversationId).first()
                    val otherUser = participants.firstOrNull { it.userId != currentUserId }

                    if (otherUser != null) {
                        val user = chatRepository.getUserById(otherUser.userId)
                        if (user != null) {
                            // Initially show with isOnline = false (will show last seen)
                            _uiState.update { it.copy(
                                conversationTitle = user.displayName,
                                conversationAvatar = user.avatarUrl,
                                isGroupChat = false,
                                isBusinessChat = conversation.isBusinessChat,
                                isOnline = false,
                                lastSeen = formatLastSeen(user.lastSeen)
                            )}

                            // After 3 seconds, randomly show online status for 50% of chats
                            if (user.isOnline && Random.nextBoolean()) {
                                viewModelScope.launch {
                                    delay(3000)
                                    _uiState.update { it.copy(isOnline = true) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun sendMessage() {
        val text = _uiState.value.composerText.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            val message = MessageEntity(
                messageId = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = currentUserId,
                content = text,
                timestamp = System.currentTimeMillis(),
                messageType = MessageType.TEXT,
                status = com.example.chatapp.data.local.entity.MessageStatus.SENT,
                isDeleted = false
            )

            chatRepository.sendMessage(message)
            _uiState.update { it.copy(composerText = "") }

            // Update lastViewedAt when user sends a message
            markMessagesAsRead()
        }
    }

    fun updateComposerText(text: String) {
        _uiState.update { it.copy(composerText = text) }
    }

    fun markMessagesAsRead() {
        viewModelScope.launch {
            // Use NonCancellable to ensure database updates complete even when navigating away
            withContext(NonCancellable) {
                println("ChatViewModel: markMessagesAsRead called for conversation $conversationId")
                // Update lastViewedAt to current time
                val currentTime = System.currentTimeMillis()
                println("ChatViewModel: Updating lastViewedAt to $currentTime")
                chatRepository.updateLastViewedAt(conversationId, currentTime)
                conversationLastViewedAt = currentTime

                // Update the conversation's unread count to 0
                println("ChatViewModel: Updating unread count to 0")
                chatRepository.updateConversationUnreadCount(conversationId, 0)
                println("ChatViewModel: Unread count update complete")

                // Reset unread count
                _uiState.update { it.copy(
                    unreadCount = 0,
                    firstUnreadMessageId = null
                )}
            }
        }
    }

    private fun formatLastSeen(lastSeenTimestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - lastSeenTimestamp
        val minutes = diff / (1000 * 60)
        val hours = diff / (1000 * 60 * 60)
        val days = diff / (1000 * 60 * 60 * 24)

        return when {
            minutes < 1 -> "last seen just now"
            minutes < 60 -> "last seen $minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
            hours < 24 -> "last seen $hours ${if (hours == 1L) "hour" else "hours"} ago"
            days < 7 -> "last seen $days ${if (days == 1L) "day" else "days"} ago"
            else -> "last seen recently"
        }
    }
}

data class ChatUiState(
    val conversationTitle: String = "",
    val conversationAvatar: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: String? = null,
    val composerText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGroupChat: Boolean = false,
    val isBusinessChat: Boolean = false,
    val participantInfo: Map<String, ParticipantInfo> = emptyMap(),
    val unreadCount: Int = 0,
    val firstUnreadMessageId: String? = null
)

data class ParticipantInfo(
    val userId: String,
    val displayName: String,
    val avatarUrl: String?
)
