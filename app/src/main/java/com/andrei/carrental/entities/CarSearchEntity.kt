package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName

data class CarSearchEntity(
        @SerializedName("id")
        val id :Long,
        @SerializedName("name")
        val name:String,
        @SerializedName("imagePath")
        val imagePath :String?
)
