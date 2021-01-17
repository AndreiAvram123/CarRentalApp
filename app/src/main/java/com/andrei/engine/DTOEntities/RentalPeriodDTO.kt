package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.RentalPeriod
import com.andrei.utils.fromUnixToLocalDate
import com.andrei.utils.toUnix
import com.google.gson.annotations.SerializedName

data class RentalPeriodDTO(
        @SerializedName("startDate")
        val startDate:Long,
        @SerializedName("endDate")
        val endDate:Long
)


fun RentalPeriodDTO.toRentalPeriod():RentalPeriod{
    return RentalPeriod(startDate = this.startDate.fromUnixToLocalDate(),endDate = this.endDate.fromUnixToLocalDate())
}
fun RentalPeriod.toRentalPeriodDTO():RentalPeriodDTO{
    return RentalPeriodDTO(startDate = this.startDate.toUnix(),endDate = this.endDate.toUnix())
}
