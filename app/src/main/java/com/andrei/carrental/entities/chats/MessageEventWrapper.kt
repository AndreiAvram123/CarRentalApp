package com.andrei.carrental.entities.chats

import com.google.gson.annotations.SerializedName

data class MessageEventWrapper(
        @SerializedName("event")
        val eventName:String,

        @SerializedName("channelName")
        val channelName:String

)
