package com.andrei.carrental.entities

import com.stfalcon.chatkit.commons.models.IUser

data class User(
        val userID :Long,
        val username:String,
        private val _profilePicture:MediaFile?
):IUser{

        val profilePicture:MediaFile
                get() {
                        if(_profilePicture == null){
                                return MediaFile("https://robohash.org/139.162.116.133.png")

                        }
                        return _profilePicture
                }

        override fun getId(): String = userID.toString()

        override fun getName(): String = username


        override fun getAvatar(): String  = profilePicture.mediaURL
}
