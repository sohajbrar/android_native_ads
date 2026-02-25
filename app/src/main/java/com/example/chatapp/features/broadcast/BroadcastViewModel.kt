package com.example.chatapp.features.broadcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.data.local.entity.MessageStatus
import com.example.chatapp.data.local.entity.MessageType
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BroadcastViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _sentMessages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val sentMessages: StateFlow<List<MessageEntity>> = _sentMessages.asStateFlow()

    fun addSentMessage(messageText: String, conversationId: String = "") {
        if (messageText.isBlank()) return
        val now = System.currentTimeMillis()
        val messageId = "broadcast_${UUID.randomUUID()}"
        val message = MessageEntity(
            messageId = messageId,
            conversationId = conversationId.ifBlank { "broadcast" },
            senderId = "user_1",
            content = messageText,
            timestamp = now,
            messageType = MessageType.TEXT,
            isDelivered = true,
            status = MessageStatus.DELIVERED
        )
        _sentMessages.value = _sentMessages.value + message

        if (conversationId.isNotBlank()) {
            viewModelScope.launch {
                chatRepository.insertMessage(message)
                chatRepository.updateLastMessage(
                    conversationId = conversationId,
                    messageId = messageId,
                    messageText = messageText,
                    timestamp = now
                )
            }
        }
    }
}
