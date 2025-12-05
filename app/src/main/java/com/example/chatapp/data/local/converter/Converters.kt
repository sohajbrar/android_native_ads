package com.example.chatapp.data.local.converter

import androidx.room.TypeConverter
import com.example.chatapp.data.local.entity.MessageType
import com.example.chatapp.data.local.entity.ParticipantRole

class Converters {
    @TypeConverter
    fun fromMessageType(type: MessageType): String {
        return type.name
    }

    @TypeConverter
    fun toMessageType(type: String): MessageType {
        return MessageType.valueOf(type)
    }

    @TypeConverter
    fun fromParticipantRole(role: ParticipantRole): String {
        return role.name
    }

    @TypeConverter
    fun toParticipantRole(role: String): ParticipantRole {
        return ParticipantRole.valueOf(role)
    }
}
