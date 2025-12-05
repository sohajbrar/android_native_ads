package com.example.chatapp.data.local.dao

import androidx.room.*
import com.example.chatapp.data.local.entity.MessageEntity
import com.example.chatapp.data.local.entity.MessageType
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)
    
    @Update
    suspend fun updateMessage(message: MessageEntity)
    
    @Delete
    suspend fun deleteMessage(message: MessageEntity)
    
    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    suspend fun getMessageById(messageId: String): MessageEntity?
    
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getConversationMessages(conversationId: String): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp DESC LIMIT :limit")
    fun getConversationMessagesWithLimit(conversationId: String, limit: Int): Flow<List<MessageEntity>>
    
    @Query("""
        SELECT * FROM messages 
        WHERE conversationId = :conversationId AND timestamp < :beforeTimestamp 
        ORDER BY timestamp DESC 
        LIMIT :limit
    """)
    suspend fun getMessagesBeforeTimestamp(
        conversationId: String,
        beforeTimestamp: Long,
        limit: Int
    ): List<MessageEntity>
    
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId AND isRead = 0")
    fun getUnreadMessages(conversationId: String): Flow<List<MessageEntity>>
    
    @Query("SELECT COUNT(*) FROM messages WHERE conversationId = :conversationId AND isRead = 0")
    suspend fun getUnreadMessageCount(conversationId: String): Int
    
    @Query("UPDATE messages SET isRead = 1 WHERE conversationId = :conversationId")
    suspend fun markAllMessagesAsRead(conversationId: String)
    
    @Query("UPDATE messages SET isRead = 1 WHERE messageId = :messageId")
    suspend fun markMessageAsRead(messageId: String)
    
    @Query("UPDATE messages SET isDelivered = 1 WHERE messageId = :messageId")
    suspend fun markMessageAsDelivered(messageId: String)
    
    @Query("""
        UPDATE messages 
        SET content = :newContent, isEdited = 1, editedAt = :editedAt 
        WHERE messageId = :messageId
    """)
    suspend fun editMessage(messageId: String, newContent: String, editedAt: Long)
    
    @Query("UPDATE messages SET isDeleted = 1 WHERE messageId = :messageId")
    suspend fun softDeleteMessage(messageId: String)
    
    @Query("SELECT * FROM messages WHERE messageType IN (:types) AND conversationId = :conversationId")
    fun getMessagesByType(conversationId: String, types: List<MessageType>): Flow<List<MessageEntity>>
    
    @Query("""
        SELECT * FROM messages 
        WHERE conversationId = :conversationId 
        AND (content LIKE '%' || :query || '%')
        ORDER BY timestamp DESC
    """)
    fun searchMessages(conversationId: String, query: String): Flow<List<MessageEntity>>
    
    @Query("DELETE FROM messages WHERE conversationId = :conversationId")
    suspend fun deleteConversationMessages(conversationId: String)
    
    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
    
    @Query("SELECT * FROM messages WHERE replyToMessageId = :messageId")
    fun getRepliesForMessage(messageId: String): Flow<List<MessageEntity>>
}
