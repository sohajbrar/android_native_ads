package com.example.chatapp.features.broadcast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.data.local.entity.MessageStatus
import com.example.chatapp.data.local.entity.MessageType
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BroadcastViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val conversationId: String =
        savedStateHandle.get<String>("conversationId") ?: ""

    val sentMessages: StateFlow<List<MessageEntity>> =
        chatRepository.getMessagesForConversation(conversationId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addSentMessage(messageText: String, conversationId: String = "") {
        if (messageText.isBlank()) return
        val effectiveConversationId = conversationId.ifBlank { this.conversationId }
        if (effectiveConversationId.isBlank()) return

        val now = System.currentTimeMillis()
        val messageId = "broadcast_${UUID.randomUUID()}"
        val message = MessageEntity(
            messageId = messageId,
            conversationId = effectiveConversationId,
            senderId = "user_1",
            content = messageText,
            timestamp = now,
            messageType = MessageType.TEXT,
            isDelivered = true,
            status = MessageStatus.DELIVERED
        )

        viewModelScope.launch {
            chatRepository.insertMessage(message)
            chatRepository.updateLastMessage(
                conversationId = effectiveConversationId,
                messageId = messageId,
                messageText = messageText,
                timestamp = now
            )
        }
    }
}
