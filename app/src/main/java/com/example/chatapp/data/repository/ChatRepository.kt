package com.example.chatapp.data.repository

import com.example.chatapp.data.local.dao.ConversationDao
import com.example.chatapp.data.local.dao.MessageDao
import com.example.chatapp.data.local.dao.OrderDao
import com.example.chatapp.data.local.dao.UserDao
import com.example.chatapp.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val userDao: UserDao,
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao,
    private val orderDao: OrderDao
) {
    // User operations
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    suspend fun insertUsers(users: List<UserEntity>) = userDao.insertUsers(users)
    suspend fun getUserById(userId: String) = userDao.getUserById(userId)
    fun getUserByIdFlow(userId: String) = userDao.getUserByIdFlow(userId)
    fun getAllUsers() = userDao.getAllUsers()
    fun getOnlineUsers() = userDao.getOnlineUsers()
    suspend fun updateUserOnlineStatus(userId: String, isOnline: Boolean, lastSeen: Long) =
        userDao.updateUserOnlineStatus(userId, isOnline, lastSeen)
    fun getUser(userId: String): Flow<UserEntity?> = userDao.getUserByIdFlow(userId)
    
    // Conversation operations
    suspend fun insertConversation(conversation: ConversationEntity) =
        conversationDao.insertConversation(conversation)
        suspend fun insertConversations(conversations: List<ConversationEntity>) =
        conversationDao.insertConversations(conversations)

    suspend fun updateConversations(conversations: List<ConversationEntity>) {
        conversations.forEach { conversation ->
            conversationDao.updateConversation(conversation)
        }
    }
    suspend fun getConversationById(conversationId: String) =
        conversationDao.getConversationById(conversationId)
    fun getConversationByIdFlow(conversationId: String) =
        conversationDao.getConversationByIdFlow(conversationId)
    fun getAllConversations() = conversationDao.getAllConversations()
    fun getPinnedConversations() = conversationDao.getPinnedConversations()
    fun getUnreadConversations() = conversationDao.getUnreadConversations()
    fun getBroadcastConversations() = conversationDao.getBroadcastConversations()
    suspend fun updateLastMessage(
        conversationId: String,
        messageId: String,
        messageText: String,
        timestamp: Long
    ) = conversationDao.updateLastMessage(conversationId, messageId, messageText, timestamp)
    suspend fun updateUnreadCount(conversationId: String, count: Int) =
        conversationDao.updateUnreadCount(conversationId, count)
    suspend fun updatePinnedStatus(conversationId: String, isPinned: Boolean) =
        conversationDao.updatePinnedStatus(conversationId, isPinned)
    suspend fun updateMutedStatus(conversationId: String, isMuted: Boolean) =
        conversationDao.updateMutedStatus(conversationId, isMuted)
    fun getConversation(conversationId: String): Flow<ConversationEntity?> =
        conversationDao.getConversationByIdFlow(conversationId)
    suspend fun updateConversation(conversation: ConversationEntity) =
        conversationDao.updateConversation(conversation)
    suspend fun deleteConversation(conversationId: String) {
        val conversation = conversationDao.getConversationById(conversationId)
        conversation?.let { conversationDao.deleteConversation(it) }
    }
    
    // Participant operations
    suspend fun insertParticipant(participant: ConversationParticipantEntity) =
        conversationDao.insertParticipant(participant)
    suspend fun insertParticipants(participants: List<ConversationParticipantEntity>) =
        conversationDao.insertParticipants(participants)
    fun getConversationParticipants(conversationId: String) =
        conversationDao.getConversationParticipants(conversationId)
    fun getUserConversations(userId: String) =
        conversationDao.getUserConversations(userId)
    fun getParticipantsForConversation(conversationId: String): Flow<List<ConversationParticipantEntity>> =
        conversationDao.getConversationParticipants(conversationId)
    
    // Message operations
    suspend fun insertMessage(message: MessageEntity) = messageDao.insertMessage(message)
    suspend fun insertMessages(messages: List<MessageEntity>) = messageDao.insertMessages(messages)
    suspend fun getMessageById(messageId: String) = messageDao.getMessageById(messageId)
    fun getConversationMessages(conversationId: String) =
        messageDao.getConversationMessages(conversationId)
    fun getConversationMessagesWithLimit(conversationId: String, limit: Int) =
        messageDao.getConversationMessagesWithLimit(conversationId, limit)
    suspend fun getMessagesBeforeTimestamp(
        conversationId: String,
        beforeTimestamp: Long,
        limit: Int
    ) = messageDao.getMessagesBeforeTimestamp(conversationId, beforeTimestamp, limit)
    fun getUnreadMessages(conversationId: String) = messageDao.getUnreadMessages(conversationId)
    suspend fun getUnreadMessageCount(conversationId: String) =
        messageDao.getUnreadMessageCount(conversationId)
    suspend fun markAllMessagesAsRead(conversationId: String) =
        messageDao.markAllMessagesAsRead(conversationId)
    suspend fun markMessageAsRead(messageId: String) = messageDao.markMessageAsRead(messageId)
    suspend fun markMessageAsDelivered(messageId: String) =
        messageDao.markMessageAsDelivered(messageId)
    suspend fun editMessage(messageId: String, newContent: String, editedAt: Long) =
        messageDao.editMessage(messageId, newContent, editedAt)
    suspend fun softDeleteMessage(messageId: String) = messageDao.softDeleteMessage(messageId)
    fun getMessagesByType(conversationId: String, types: List<MessageType>) =
        messageDao.getMessagesByType(conversationId, types)
    fun searchMessages(conversationId: String, query: String) =
        messageDao.searchMessages(conversationId, query)
    fun getRepliesForMessage(messageId: String) = messageDao.getRepliesForMessage(messageId)
    fun getMessagesForConversation(conversationId: String): Flow<List<MessageEntity>> =
        messageDao.getConversationMessages(conversationId)
    fun getBroadcastMessages(): Flow<List<MessageEntity>> =
        messageDao.getBroadcastMessages()
    
    // Clear data operations
    suspend fun clearAllData() {
        messageDao.deleteAllMessages()
        conversationDao.deleteAllParticipants()
        conversationDao.deleteAllConversations()
        userDao.deleteAllUsers()
    }
    
    suspend fun sendMessage(message: MessageEntity) {
        // Insert the message
        messageDao.insertMessage(message)
        
        // Update conversation's last message
        conversationDao.updateLastMessage(
            conversationId = message.conversationId,
            messageId = message.messageId,
            messageText = when (message.messageType) {
                MessageType.IMAGE -> "📷 Photo"
                MessageType.VIDEO -> "📹 Video"
                MessageType.AUDIO -> "🎵 Audio"
                MessageType.FILE -> "📎 File"
                MessageType.LOCATION -> "📍 Location"
                MessageType.STICKER -> "Sticker"
                MessageType.GIF -> "GIF"
                MessageType.VOICE_NOTE -> "🎤 Voice message"
                MessageType.LINK -> "🔗 Link"
                MessageType.SYSTEM -> message.content
                MessageType.TEXT -> message.content
            },
            timestamp = message.timestamp
        )
    }
    
    suspend fun updateConversationUnreadCount(conversationId: String, count: Int) {
        println("ChatRepository: Updating unread count for $conversationId to $count")
        conversationDao.updateUnreadCount(conversationId, count)
        println("ChatRepository: Unread count updated successfully")
    }
    
    suspend fun updateLastViewedAt(conversationId: String, timestamp: Long) {
        println("ChatRepository: Updating lastViewedAt for $conversationId to $timestamp")
        conversationDao.updateLastViewedAt(conversationId, timestamp)
        println("ChatRepository: LastViewedAt updated successfully")
    }

    // Drop and recreate the entire database to avoid foreign key issues
    suspend fun dropAndRecreateDatabase() {
        try {
            println("ChatRepository: Attempting to clear database...")
            
            // Try to clear all data - if this fails, we'll know the exact issue
            clearAllData()
            println("ChatRepository: Database cleared successfully")
            
        } catch (e: Exception) {
            println("ChatRepository: Error during database clearing: ${e.message}")
            println("ChatRepository: This suggests foreign key constraints are preventing data clearing")
            throw e
        }
    }
    
    // Order operations
    suspend fun insertOrder(order: OrderEntity) = orderDao.insertOrder(order)
    
    suspend fun insertOrders(orders: List<OrderEntity>) = orderDao.insertOrders(orders)
    
    suspend fun updateOrder(order: OrderEntity) = orderDao.updateOrder(order)
    
    suspend fun getOrderById(orderId: String) = orderDao.getOrderById(orderId)
    
    fun getOrderByIdFlow(orderId: String) = orderDao.getOrderByIdFlow(orderId)
    
    fun getOrdersByCustomer(customerId: String) = orderDao.getOrdersByCustomer(customerId)
    
    suspend fun getOrdersByCustomerList(customerId: String): List<OrderEntity> {
        // Helper to get orders as a list for analysis
        return getOrdersByCustomer(customerId).first()
    }
    
    fun getOrdersByConversation(conversationId: String) = orderDao.getOrdersByConversation(conversationId)
    
    fun getAllOrders() = orderDao.getAllOrders()
    
    suspend fun getOrderCountForCustomer(customerId: String) = orderDao.getOrderCountForCustomer(customerId)
    
    suspend fun getTotalOrderValueForCustomer(customerId: String) = orderDao.getTotalOrderValueForCustomer(customerId)
    
    suspend fun getAverageOrderValueForCustomer(customerId: String) = orderDao.getAverageOrderValueForCustomer(customerId)
    
    suspend fun getLastOrderDateForCustomer(customerId: String) = orderDao.getLastOrderDateForCustomer(customerId)
    
    suspend fun getLastOrderForCustomer(customerId: String) = orderDao.getLastOrderForCustomer(customerId)
}
