package com.example.chatapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["conversationId"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["conversationId"]),
        Index(value = ["senderId"]),
        Index(value = ["timestamp"])
    ]
)
data class MessageEntity(
    @PrimaryKey
    val messageId: String,
    val conversationId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val messageType: MessageType = MessageType.TEXT,
    val mediaUrl: String? = null, // For images, videos, files
    val thumbnailUrl: String? = null, // For video/image previews
    val fileName: String? = null, // For file attachments
    val fileSize: Long? = null, // File size in bytes
    val duration: Int? = null, // For audio/video messages in seconds
    val linkUrl: String? = null, // For link previews
    val linkTitle: String? = null,
    val linkDescription: String? = null,
    val linkImageUrl: String? = null,
    val isRead: Boolean = false,
    val isDelivered: Boolean = true,
    val isEdited: Boolean = false,
    val editedAt: Long? = null,
    val replyToMessageId: String? = null,
    val isDeleted: Boolean = false,
    val reactions: String? = null, // JSON string of reactions
    val status: MessageStatus = MessageStatus.SENT
) {
    // Derived status from legacy flags for compatibility
    val derivedStatus: MessageStatus
        get() = when {
            !isDelivered -> MessageStatus.FAILED
            isRead -> MessageStatus.READ
            isDelivered -> MessageStatus.DELIVERED
            else -> MessageStatus.SENT
        }
}

enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    FILE,
    LINK,
    LOCATION,
    STICKER,
    GIF,
    VOICE_NOTE,
    SYSTEM // For system messages like "User joined", "User left", etc.
}

enum class MessageStatus {
    SENT,
    DELIVERED,
    READ,
    FAILED
}
