package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName

data class User(
        val id :Long,
        val username:String,
){
        var profilePicture:Image? = null
        get() {
                if(field == null){
                   return Image("https://robohash.org/139.162.116.133.png")
                }
                return field
        }
}
