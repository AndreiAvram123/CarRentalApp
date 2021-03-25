package com.andrei.carrental.entities

import com.stfalcon.chatkit.commons.models.IUser

data class User(
        val userID :Long,
        val username:String,
        val profilePicture:MediaFile,
        val email:String
):IUser{


        override fun getId(): String = userID.toString()

        override fun getName(): String = username


        override fun getAvatar(): String  = profilePicture.mediaURL
}
