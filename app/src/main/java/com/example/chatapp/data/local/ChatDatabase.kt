package com.example.chatapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chatapp.data.local.converter.Converters
import com.example.chatapp.data.local.dao.ConversationDao
import com.example.chatapp.data.local.dao.MessageDao
import com.example.chatapp.data.local.dao.OrderDao
import com.example.chatapp.data.local.dao.UserDao
import com.example.chatapp.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        ConversationParticipantEntity::class,
        OrderEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun orderDao(): OrderDao

    companion object {
        const val DATABASE_NAME = "chat_database"
    }
}
