package com.example.chatapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "conversation_participants",
    primaryKeys = ["conversationId", "userId"],
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
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["conversationId"]),
        Index(value = ["userId"])
    ]
)
data class ConversationParticipantEntity(
    val conversationId: String,
    val userId: String,
    val joinedAt: Long = System.currentTimeMillis(),
    val role: ParticipantRole = ParticipantRole.MEMBER,
    val lastReadMessageId: String? = null,
    val lastReadTimestamp: Long? = null
)

enum class ParticipantRole {
    ADMIN,
    MODERATOR,
    MEMBER
}
