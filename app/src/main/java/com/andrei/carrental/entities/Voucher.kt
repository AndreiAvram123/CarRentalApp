package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName

data class Voucher(
    @SerializedName("id")
    val id:Long,
    @SerializedName("description")
    val description:String,
    @SerializedName("value")
    val value:Int

)
