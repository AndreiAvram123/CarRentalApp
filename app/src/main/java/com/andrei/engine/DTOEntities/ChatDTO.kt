package com.andrei.engine.DTOEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrei.carrental.entities.User
import com.google.gson.annotations.SerializedName


data class ChatDTO(
        @SerializedName("id")
        val id:Long,
        @SerializedName("lastMessages")
        val lastMessages:List<MessageDTO>,
        @SerializedName("friend")
        val friend:UserDTO
){
    fun toChat():Chat = Chat(id = this.id, friendID = this.friend.userID)
}



@Entity
data class Chat(
        @PrimaryKey
        @ColumnInfo(name = "chatID")
        val id:Long,
        @ColumnInfo(name = "friendID")
        val friendID:Long

)
