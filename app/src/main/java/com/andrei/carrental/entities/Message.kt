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
)
