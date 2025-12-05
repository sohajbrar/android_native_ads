package com.example.chatapp.data.local.dao

import androidx.room.*
import com.example.chatapp.data.local.entity.ConversationEntity
import com.example.chatapp.data.local.entity.ConversationParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)
    
    @Update
    suspend fun updateConversation(conversation: ConversationEntity)
    
    @Delete
    suspend fun deleteConversation(conversation: ConversationEntity)
    
    @Query("SELECT * FROM conversations WHERE conversationId = :conversationId")
    suspend fun getConversationById(conversationId: String): ConversationEntity?
    
    @Query("SELECT * FROM conversations WHERE conversationId = :conversationId")
    fun getConversationByIdFlow(conversationId: String): Flow<ConversationEntity?>
    
    @Query("SELECT * FROM conversations ORDER BY lastMessageTimestamp DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>
    
    @Query("SELECT * FROM conversations WHERE isPinned = 1 ORDER BY lastMessageTimestamp DESC")
    fun getPinnedConversations(): Flow<List<ConversationEntity>>
    
    @Query("SELECT * FROM conversations WHERE unreadCount > 0 ORDER BY lastMessageTimestamp DESC")
    fun getUnreadConversations(): Flow<List<ConversationEntity>>
    
    @Query("""
        UPDATE conversations 
        SET lastMessageId = :messageId, 
            lastMessageText = :messageText, 
            lastMessageTimestamp = :timestamp,
            updatedAt = :timestamp
        WHERE conversationId = :conversationId
    """)
    suspend fun updateLastMessage(
        conversationId: String,
        messageId: String,
        messageText: String,
        timestamp: Long
    )
    
    @Query("UPDATE conversations SET unreadCount = :count WHERE conversationId = :conversationId")
    suspend fun updateUnreadCount(conversationId: String, count: Int)
    
    @Query("UPDATE conversations SET isPinned = :isPinned WHERE conversationId = :conversationId")
    suspend fun updatePinnedStatus(conversationId: String, isPinned: Boolean)
    
    @Query("UPDATE conversations SET isMuted = :isMuted WHERE conversationId = :conversationId")
    suspend fun updateMutedStatus(conversationId: String, isMuted: Boolean)
    
    @Query("UPDATE conversations SET lastViewedAt = :timestamp WHERE conversationId = :conversationId")
    suspend fun updateLastViewedAt(conversationId: String, timestamp: Long)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participant: ConversationParticipantEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<ConversationParticipantEntity>)
    
    @Query("SELECT * FROM conversation_participants WHERE conversationId = :conversationId")
    fun getConversationParticipants(conversationId: String): Flow<List<ConversationParticipantEntity>>
    
    @Query("SELECT * FROM conversation_participants WHERE userId = :userId")
    fun getUserConversations(userId: String): Flow<List<ConversationParticipantEntity>>
    
    @Query("DELETE FROM conversations")
    suspend fun deleteAllConversations()
    
    @Query("DELETE FROM conversation_participants")
    suspend fun deleteAllParticipants()
}
