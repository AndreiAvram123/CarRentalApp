package com.andrei.engine.requestModels

import com.andrei.engine.DTOEntities.PaymentRequest
import com.google.gson.annotations.SerializedName

data class NewBookingRequestModel(
        @SerializedName("paymentRequest")
        val paymentRequest:PaymentRequest,
        @SerializedName("startDate")
        val startDate:Long,
        @SerializedName("endDate")
        val endDate:Long,
        @SerializedName("carID")
        val carID:Long,
        @SerializedName("userID")
        val userID:Int
)
