package com.andrei.carrental.entities

import androidx.room.Entity
import com.google.gson.annotations.SerializedName


data class MediaFile(
        @SerializedName("mediaURL")
        val mediaURL :String
)
