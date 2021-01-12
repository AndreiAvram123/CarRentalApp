package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName

data class Car(
       @SerializedName("name")
       val name:String
)
