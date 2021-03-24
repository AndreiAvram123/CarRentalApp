package com.andrei.engine.requestModels

import com.google.gson.annotations.SerializedName

data class CreateChatRequest(
        @SerializedName("user1ID")
        val user1ID:Long,
        @SerializedName("user2ID")
        val user2ID:Long
)
