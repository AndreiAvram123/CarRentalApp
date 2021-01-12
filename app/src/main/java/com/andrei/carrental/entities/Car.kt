package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName

data class Car(
       @SerializedName("name")
       val name:String,
       @SerializedName("images")
       val images:List<Image>,
       @SerializedName("user")
       val user:User
)
