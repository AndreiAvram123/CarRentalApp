package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.MessageType
import com.andrei.carrental.entities.User
import com.google.gson.annotations.SerializedName

data class MessageDTO(
   @SerializedName("id")
   val id :Long,
   @SerializedName("content")
   val content:String,
   @SerializedName("date")
   val date:Long,
   @SerializedName("sender")
   val sender: User,
   @SerializedName("chatID")
   val chatID:Long,
   @SerializedName("isImageMessage")
   val isImageMessage:Boolean = false
)

fun MessageDTO.toMessage(messageType: MessageType): Message {
   return Message(id = this.id,
           content = this.content,
           date = this.date ,
           userID =  this.sender.id ,
           chatID = this.chatID,
           messageType = messageType)
}
