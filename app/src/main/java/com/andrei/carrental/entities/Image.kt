package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName

data class Image(
        @SerializedName("imagePath")
        val imagePath :String
)
