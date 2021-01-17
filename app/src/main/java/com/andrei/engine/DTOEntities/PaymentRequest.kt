package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName

data class PaymentRequest(
        @SerializedName("nonce")
        val nonce:String?,
        @SerializedName("deviceData")
        val deviceData :String?,
        @SerializedName("amount")
        val amount :Double,
)
