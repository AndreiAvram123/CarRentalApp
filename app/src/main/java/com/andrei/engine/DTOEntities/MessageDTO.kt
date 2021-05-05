package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.MessageType
import com.google.gson.annotations.SerializedName

data class MessageDTO(
        @SerializedName("id")
        val id :Long,
        @SerializedName("content")
        val textContent:String?,
        @SerializedName("date")
        val date:Long,
        @SerializedName("sender")
        val sender: UserDTO,
        @SerializedName("chatID")
        val chatID:Long,
        @SerializedName("messageType")
        val messageType: MessageType,
        @SerializedName("mediaURL")
        val mediaURl:String?
){
        fun toMessage(): Message = Message(
                messageID = this.id,
                textContent = this.textContent,
                date = this.date ,
                sender =  this.sender.toUser(),
                chatID = this.chatID,
                messageType = this.messageType,
                mediaURL = this.mediaURl
        )

}



