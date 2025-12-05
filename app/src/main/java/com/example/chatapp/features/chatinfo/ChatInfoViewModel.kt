package com.example.chatapp.features.chatinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.local.entity.ConversationEntity
import com.example.chatapp.data.local.entity.UserEntity
import com.example.chatapp.data.local.entity.ConversationParticipantEntity
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.data.local.entity.MessageType
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatInfoViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    // Current user ID - in a real app, this would come from auth service
    private val currentUserId = "user_1"

    private val _uiState = MutableStateFlow(ChatInfoUiState())
    val uiState: StateFlow<ChatInfoUiState> = _uiState.asStateFlow()

    fun loadChatInfo(conversationId: String) {
        viewModelScope.launch {
            // Load conversation details
            repository.getConversation(conversationId).collect { conversation ->
                conversation?.let {
                    _uiState.update { state ->
                        state.copy(
                            conversation = it,
                            isGroupChat = it.isGroup
                        )
                    }

                    if (it.isGroup) {
                        loadGroupParticipants(conversationId)
                    } else {
                        loadDirectChatUser(conversationId)
                    }

                    loadMediaCount(conversationId)
                }
            }
        }
    }

    private suspend fun loadGroupParticipants(conversationId: String) {
        repository.getParticipantsForConversation(conversationId).collect { participants ->
            val users = participants.mapNotNull { participant ->
                repository.getUser(participant.userId).firstOrNull()
            }

            _uiState.update { state ->
                state.copy(
                    participants = users,
                    participantRoles = participants.associateBy { it.userId },
                    onlineParticipantCount = users.count { it.isOnline }
                )
            }
        }
    }

    private suspend fun loadDirectChatUser(conversationId: String) {
        // Get the other user in a direct chat
        val participants = repository.getParticipantsForConversation(conversationId).first()
        val otherUserId = participants.firstOrNull { it.userId != currentUserId }?.userId

        otherUserId?.let { userId ->
            repository.getUser(userId).collect { user ->
                user?.let {
                    _uiState.update { state ->
                        state.copy(
                            directChatUser = it,
                            isOnline = it.isOnline,
                            lastSeen = it.lastSeen
                        )
                    }
                }
            }
        }
    }

    private suspend fun loadMediaCount(conversationId: String) {
        repository.getMessagesForConversation(conversationId).collect { messages ->
            val mediaMessages = messages.filter {
                it.messageType in listOf(
                    MessageType.IMAGE,
                    MessageType.VIDEO,
                    MessageType.AUDIO,
                    MessageType.FILE
                )
            }

            val photoCount = messages.count { it.messageType == MessageType.IMAGE }
            val videoCount = messages.count { it.messageType == MessageType.VIDEO }
            val docCount = messages.count { it.messageType == MessageType.FILE }

            _uiState.update { state ->
                state.copy(
                    mediaMessages = mediaMessages.take(20), // Show recent 20 media items
                    photoCount = photoCount,
                    videoCount = videoCount,
                    docCount = docCount,
                    totalMediaCount = photoCount + videoCount + docCount
                )
            }
        }
    }

    fun toggleMute() {
        viewModelScope.launch {
            _uiState.value.conversation?.let { conversation ->
                val updatedConversation = conversation.copy(
                    isMuted = !conversation.isMuted
                )
                repository.updateConversation(updatedConversation)
                _uiState.update { it.copy(isMuted = !it.isMuted) }
            }
        }
    }

    fun toggleNotifications() {
        _uiState.update { it.copy(notificationsEnabled = !it.notificationsEnabled) }
    }

    fun toggleDisappearingMessages() {
        _uiState.update { it.copy(disappearingMessages = !it.disappearingMessages) }
    }

    fun toggleChatLock() {
        _uiState.update { it.copy(chatLocked = !it.chatLocked) }
    }

    fun blockUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isBlocked = true) }
            // TODO: Implement actual block functionality
        }
    }

    fun unblockUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isBlocked = false) }
            // TODO: Implement actual unblock functionality
        }
    }

    fun reportUser() {
        // TODO: Implement report functionality
    }

    fun reportGroup() {
        // TODO: Implement report functionality
    }

    fun exitGroup() {
        viewModelScope.launch {
            // TODO: Implement exit group functionality
        }
    }

    fun addParticipants(userIds: List<String>) {
        viewModelScope.launch {
            // TODO: Implement add participants functionality
        }
    }

    fun removeParticipant(userId: String) {
        viewModelScope.launch {
            // TODO: Implement remove participant functionality
        }
    }

    fun makeAdmin(userId: String) {
        viewModelScope.launch {
            // TODO: Implement make admin functionality
        }
    }

    fun removeAdmin(userId: String) {
        viewModelScope.launch {
            // TODO: Implement remove admin functionality
        }
    }
}

data class ChatInfoUiState(
    val conversation: ConversationEntity? = null,
    val isGroupChat: Boolean = false,

    // 1:1 Chat specific
    val directChatUser: UserEntity? = null,
    val isOnline: Boolean = false,
    val lastSeen: Long? = null,
    val isBlocked: Boolean = false,

    // Group Chat specific
    val participants: List<UserEntity> = emptyList(),
    val participantRoles: Map<String, ConversationParticipantEntity> = emptyMap(),
    val onlineParticipantCount: Int = 0,
    val groupDescription: String? = null,
    val createdBy: String? = null,
    val createdAt: Long? = null,

    // Media
    val mediaMessages: List<MessageEntity> = emptyList(),
    val photoCount: Int = 0,
    val videoCount: Int = 0,
    val docCount: Int = 0,
    val totalMediaCount: Int = 0,

    // Settings
    val isMuted: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val mediaVisibility: Boolean = true,
    val disappearingMessages: Boolean = false,
    val chatLocked: Boolean = false,

    // Loading states
    val isLoading: Boolean = false,
    val error: String? = null
)
