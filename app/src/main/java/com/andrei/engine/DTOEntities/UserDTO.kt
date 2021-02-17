package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.Image
import com.andrei.carrental.entities.User
import com.google.gson.annotations.SerializedName

data class UserDTO(
        @SerializedName("id")
        val id :Long,
        @SerializedName("username")
        val username:String,
        @SerializedName("profilePicture")
        val profilePicture:Image?
)

fun UserDTO.toUser():User{
    return User(id = this.id, username =this.username, _profilePicture = this.profilePicture)
}

