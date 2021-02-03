package com.andrei.engine.DTOEntities

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class GeoPoint(
        @SerializedName("latitude")
        val latitude:Double,
        @SerializedName("longitude")
        val longitude:Double
)
fun GeoPoint.toLatLng():LatLng {
        return LatLng(this.latitude,this.longitude)
}
