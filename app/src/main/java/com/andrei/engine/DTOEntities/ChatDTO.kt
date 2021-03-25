package com.andrei.engine.DTOEntities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrei.carrental.entities.MediaFile
import com.andrei.carrental.entities.User
import com.google.gson.annotations.SerializedName

data class ChatDTO(
        @SerializedName("id")
        val id:Long,
        @SerializedName("lastMessages")
        val lastMessages:List<MessageDTO>,
        @SerializedName("user1")
        val user1:UserDTO,
        @SerializedName("user2")
        val user2:UserDTO
){
    fun toChat(currentUserID:Long):Chat {
            return when(user1.userID){
                    currentUserID -> Chat(id = this.id, image = user2.profilePicture,name = user2.username)
                    else -> Chat(id = this.id, image = user1.profilePicture, name = user1.username)
            }
    }
}



@Entity
data class Chat(
        @PrimaryKey
        @ColumnInfo(name = "chatID")
        val id:Long,
        @Embedded
        val image:MediaFile,
        @SerializedName("name")
        val name:String

)
