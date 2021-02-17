package com.andrei.carrental.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey
    @ColumnInfo(name = "messageID")
    val id:Long,
    @ColumnInfo(name = "content")
    val content:String,
    @ColumnInfo(name = "date")
    val date:Long,
    @ColumnInfo(name = "userID")
    val userID:Long,
    @ColumnInfo(name = "chatID")
    val chatID:Long,
    @ColumnInfo(name = "type")
    val messageType:MessageType
)

enum class MessageType(val id: Int) {
   MESSAGE_RECEIVED_TEXT(1), MESSAGE_SENT_TEXT(2), MESSAGE_SENT_IMAGE(3), MESSAGE_RECEIVED_IMAGE(4),
}

