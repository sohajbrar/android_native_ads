package com.example.chatapp.features.broadcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.local.entity.ConversationEntity
import com.example.chatapp.data.local.entity.UserEntity
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BroadcastInfoViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BroadcastInfoUiState())
    val uiState: StateFlow<BroadcastInfoUiState> = _uiState.asStateFlow()

    fun loadBroadcastInfo(conversationId: String) {
        viewModelScope.launch {
            repository.getConversation(conversationId).collect { conversation ->
                conversation?.let {
                    _uiState.update { state ->
                        state.copy(
                            conversation = it,
                            isListBased = it.broadcastLinkedListCount > 0
                        )
                    }
                    loadRecipients(conversationId)
                }
            }
        }
    }

    private suspend fun loadRecipients(conversationId: String) {
        repository.getParticipantsForConversation(conversationId).collect { participants ->
            val users = participants
                .filter { it.userId != "user_1" }
                .mapNotNull { participant ->
                    repository.getUser(participant.userId).firstOrNull()
                }

            _uiState.update { state ->
                state.copy(recipients = users)
            }
        }
    }

    fun deleteBroadcast() {
        viewModelScope.launch {
            _uiState.value.conversation?.let { conversation ->
                repository.deleteConversation(conversation.conversationId)
            }
        }
    }
}

data class BroadcastInfoUiState(
    val conversation: ConversationEntity? = null,
    val isListBased: Boolean = false,
    val recipients: List<UserEntity> = emptyList()
)
