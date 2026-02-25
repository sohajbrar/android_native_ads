package com.example.chatapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val conversationId: String,
    val title: String? = null,
    val isGroup: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastMessageId: String? = null,
    val lastMessageText: String? = null,
    val lastMessageTimestamp: Long? = null,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val isBusinessChat: Boolean = false,
    val isBroadcast: Boolean = false,
    val broadcastRecipientCount: Int = 0,
    val broadcastLinkedListCount: Int = 0,
    val avatarUrl: String? = null,
    val lastViewedAt: Long = 0L
)
