package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.MediaFile
import com.andrei.carrental.entities.User
import com.google.gson.annotations.SerializedName

data class UserDTO(
        @SerializedName("id")
        val userID :Long,
        @SerializedName("username")
        val username:String,
        @SerializedName("email")
        val email:String,
        @SerializedName("profilePicture")
        val profilePicture:MediaFile?
)

fun UserDTO.toUser():User{
    return User(userID = this.userID, username =this.username, _profilePicture = this.profilePicture)
}

