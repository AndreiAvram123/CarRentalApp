package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName

data class User(
        val id :Long,
        val username:String,
        private val _profilePicture:Image?
){
        val profilePicture:Image
                get() {
                        if(_profilePicture == null){
                                return Image("https://robohash.org/139.162.116.133.png")

                        }
                        return _profilePicture
                }
}
