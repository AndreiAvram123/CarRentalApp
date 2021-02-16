package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName


data class ChatDTO(
    @SerializedName("id")
    val id:Long,
    @SerializedName("lastMessages")
    val lastMessages:List<MessageDTO>
)
