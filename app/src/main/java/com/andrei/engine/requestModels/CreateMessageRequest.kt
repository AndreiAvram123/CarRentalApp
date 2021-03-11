package com.andreia.carrental.requestModels

import com.andrei.carrental.entities.MessageType
import com.google.gson.annotations.SerializedName

data class CreateMessageRequest(
        @SerializedName("senderID")
        val senderID:Long,
        @SerializedName("chatID")
        val chatID:Long,
        @SerializedName("messageType")
        val messageType: MessageType,
        @SerializedName("textContent")
        val textContent:String? = null,
        @SerializedName("mediaData")
        val mediaData:String? = null
)
