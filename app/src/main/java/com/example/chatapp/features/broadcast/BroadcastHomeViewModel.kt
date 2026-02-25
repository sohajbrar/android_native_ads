package com.example.chatapp.features.broadcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.local.entity.ConversationEntity
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BroadcastHomeViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BroadcastHomeUiState())
    val uiState: StateFlow<BroadcastHomeUiState> = _uiState.asStateFlow()

    init {
        loadBroadcasts()
    }

    private fun loadBroadcasts() {
        viewModelScope.launch {
            combine(
                chatRepository.getBroadcastConversations(),
                chatRepository.getBroadcastMessages()
            ) { conversations, messages ->
                val conversationMap = conversations.associateBy { it.conversationId }
                val sentMessages = messages.mapNotNull { message ->
                    conversationMap[message.conversationId]?.let { conversation ->
                        BroadcastSentMessage(
                            message = message,
                            conversationTitle = conversation.title,
                            recipientCount = conversation.broadcastRecipientCount
                        )
                    }
                }
                Triple(conversations, sentMessages, messages.size)
            }.collect { (conversations, sentMessages, _) ->
                _uiState.update { state ->
                    state.copy(
                        broadcastConversations = conversations,
                        sentBroadcastMessages = sentMessages,
                        isLoading = false
                    )
                }
            }
        }
    }
}

data class BroadcastSentMessage(
    val message: MessageEntity,
    val conversationTitle: String?,
    val recipientCount: Int
)

data class BroadcastHomeUiState(
    val broadcastConversations: List<ConversationEntity> = emptyList(),
    val sentBroadcastMessages: List<BroadcastSentMessage> = emptyList(),
    val isLoading: Boolean = true
)
