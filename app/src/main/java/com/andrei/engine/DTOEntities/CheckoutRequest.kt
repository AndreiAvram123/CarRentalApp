package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName

data class CheckoutRequest(
        @SerializedName("paymentInformation")
         val paymentRequest: PaymentRequest,
        @SerializedName("rentInformation")
        val rentInformation: RentInformation)

