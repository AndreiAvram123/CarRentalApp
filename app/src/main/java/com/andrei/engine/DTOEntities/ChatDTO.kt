package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.User
import com.google.gson.annotations.SerializedName


data class ChatDTO(
    @SerializedName("id")
    val id:Long,
    @SerializedName("lastMessages")
    val lastMessages:List<MessageDTO>,
    @SerializedName("friend")
    val friend:UserDTO
)
