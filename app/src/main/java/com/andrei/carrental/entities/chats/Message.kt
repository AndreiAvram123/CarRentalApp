package com.andreia.carrental.entities

import com.andrei.carrental.entities.User
import com.google.gson.annotations.SerializedName

data class Message(
   @SerializedName("id")
   val id :Long,
   @SerializedName("content")
   val content:String,
   @SerializedName("date")
   val date:Long,
   @SerializedName("sender")
   val sender: User




)
