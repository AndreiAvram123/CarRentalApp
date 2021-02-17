package com.andreia.carrental.requestModels

import com.google.gson.annotations.SerializedName

data class CreateMessageRequest(
    @SerializedName("content")
    val content:String,
    @SerializedName("senderID")
    val senderID:Long,
    @SerializedName("chatID")
    val chatID:Long
)
