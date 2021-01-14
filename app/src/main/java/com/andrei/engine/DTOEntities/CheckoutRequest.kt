package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName

data class CheckoutRequest(
    @SerializedName("nonce")
    val nonce:String?,
    @SerializedName("deviceData")
    val deviceData :String?,
    @SerializedName("amount")
    val amount :Long,
)
