package com.example.chatapp.data.initializer

import com.example.chatapp.data.generator.ChatDataGenerator
import com.example.chatapp.data.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class DatabaseInitializer @Inject constructor(
    private val chatRepository: ChatRepository
) {
    
    suspend fun initializeDatabase() = withContext(Dispatchers.IO) {
        try {
            Log.d("DatabaseInitializer", "Starting database initialization...")
            
            // ALWAYS clear and repopulate database on app start
            // This ensures fresh Lucky Shrub data on every launch
            try {
                Log.d("DatabaseInitializer", "Clearing existing database...")
                chatRepository.clearAllData()
                Log.d("DatabaseInitializer", "Database cleared successfully")
            } catch (e: Exception) {
                // If clear fails, database might be empty - that's OK
                Log.d("DatabaseInitializer", "Clear failed (database might be empty): ${e.message}")
            }
            
            // Populate with fresh Lucky Shrub business data
            Log.d("DatabaseInitializer", "Populating database with Lucky Shrub data...")
            populateDatabase()
            Log.d("DatabaseInitializer", "Database initialization completed successfully")
            
        } catch (e: Exception) {
            Log.e("DatabaseInitializer", "Error during initialization: ${e.message}", e)
            throw e
        }
    }
    
    private suspend fun populateDatabase() {
        try {
            Log.d("DatabaseInitializer", "Starting database population...")
            
            // Database will be created automatically by Room when we first access it
            Log.d("DatabaseInitializer", "Database will be created automatically by Room")
            
            // Generate users
            Log.d("DatabaseInitializer", "Generating users...")
            val users = ChatDataGenerator.generateUsers(100)
            Log.d("DatabaseInitializer", "Generated ${users.size} users")
            chatRepository.insertUsers(users)
            Log.d("DatabaseInitializer", "Users inserted successfully")
            
            // Use the first user as the current user
            val currentUserId = "user_1"
            
            // Generate conversations
            Log.d("DatabaseInitializer", "Generating conversations...")
            val conversations = ChatDataGenerator.generateConversations(users, currentUserId)
            Log.d("DatabaseInitializer", "Generated ${conversations.size} conversations")
            
            // Insert conversations FIRST (before participants)
            Log.d("DatabaseInitializer", "Inserting conversations...")
            chatRepository.insertConversations(conversations)
            Log.d("DatabaseInitializer", "Conversations inserted successfully")
            
            // Generate participants
            Log.d("DatabaseInitializer", "Generating participants...")
            val participants = ChatDataGenerator.generateConversationParticipants(
                conversations, users, currentUserId
            )
            Log.d("DatabaseInitializer", "Generated ${participants.size} participants")
            chatRepository.insertParticipants(participants)
            Log.d("DatabaseInitializer", "Participants inserted successfully")
            
            // Generate messages
            Log.d("DatabaseInitializer", "Generating messages...")
            val messages = ChatDataGenerator.generateMessages(
                conversations, participants, messagesPerConversation = 50
            )
            Log.d("DatabaseInitializer", "Generated ${messages.size} messages")
            chatRepository.insertMessages(messages)
            Log.d("DatabaseInitializer", "Messages inserted successfully")
            
            // Generate orders for business customers (correlated with message activity)
            Log.d("DatabaseInitializer", "Generating orders...")
            val orders = ChatDataGenerator.generateOrders(users, conversations, currentUserId, participants, messages)
            Log.d("DatabaseInitializer", "Generated ${orders.size} orders")
            chatRepository.insertOrders(orders)
            Log.d("DatabaseInitializer", "Orders inserted successfully")
            
            // Update conversations with last message info and correct unread counts
            Log.d("DatabaseInitializer", "Updating conversations with last message info...")
            val updatedConversations = ChatDataGenerator.updateConversationsWithLastMessage(
                conversations, messages, currentUserId
            )
            Log.d("DatabaseInitializer", "Updated ${updatedConversations.size} conversations")
            
            // Update the existing conversations with last message info
            Log.d("DatabaseInitializer", "Updating conversations in database...")
            chatRepository.updateConversations(updatedConversations)
            Log.d("DatabaseInitializer", "Conversations updated successfully")

            Log.d("DatabaseInitializer", "Database initialized with ${users.size} users, ${updatedConversations.size} conversations, ${messages.size} messages, and ${orders.size} orders")
        } catch (e: Exception) {
            Log.e("DatabaseInitializer", "Error during population: ${e.message}", e)
            throw e
        }
    }
    
    suspend fun resetAndRepopulateDatabase() = withContext(Dispatchers.IO) {
        try {
            println("DatabaseInitializer: Starting database reset...")
            chatRepository.clearAllData()
            println("DatabaseInitializer: All data cleared successfully")
            populateDatabase()
            println("DatabaseInitializer: Database reset and repopulation completed")
        } catch (e: Exception) {
            println("DatabaseInitializer: Error during reset: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}
