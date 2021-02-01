package com.andrei.carrental.entities

import com.andrei.carrental.entities.Image
import com.google.gson.annotations.SerializedName


data class BasicUser(
        @SerializedName("id")
        val id :Int,
        @SerializedName("email")
        val email:String,
        @SerializedName("username")
        val username:String,
        val profileImage: Image?
)
