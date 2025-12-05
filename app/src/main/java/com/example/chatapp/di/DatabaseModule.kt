package com.example.chatapp.di

import android.content.Context
import androidx.room.Room
import com.example.chatapp.data.local.ChatDatabase
import com.example.chatapp.data.local.dao.ConversationDao
import com.example.chatapp.data.local.dao.MessageDao
import com.example.chatapp.data.local.dao.OrderDao
import com.example.chatapp.data.local.dao.UserDao
import com.example.chatapp.data.initializer.DatabaseInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideChatDatabase(
        @ApplicationContext context: Context
    ): ChatDatabase {
        return Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            ChatDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    @Singleton
    fun provideUserDao(database: ChatDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    @Singleton
    fun provideConversationDao(database: ChatDatabase): ConversationDao {
        return database.conversationDao()
    }
    
    @Provides
    @Singleton
    fun provideMessageDao(database: ChatDatabase): MessageDao {
        return database.messageDao()
    }
    
    @Provides
    @Singleton
    fun provideOrderDao(database: ChatDatabase): OrderDao {
        return database.orderDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseInitializer(
        chatRepository: com.example.chatapp.data.repository.ChatRepository
    ): DatabaseInitializer {
        return DatabaseInitializer(chatRepository)
    }
}
