package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName

data class User(
        val id :Long,
        val username:String,
        val profilePicture:Image?
)
