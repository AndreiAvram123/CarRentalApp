package com.andrei.utils

import android.app.Activity
import android.content.IntentSender
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.Exception

class LocationSettingsHandler {

    companion object{
        const  val REQUEST_CHECK_SETTINGS: Int = 3
    }


    val currentLocationNeedsSatisfied:MutableStateFlow<Boolean> = MutableStateFlow(false)



      suspend fun startLocationRequest(activity: Activity, locationRequest: LocationRequest) {
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

            val client = LocationServices.getSettingsClient(activity)
            val task = client.checkLocationSettings(builder.build())

            try {
                task.await()
                currentLocationNeedsSatisfied.emit(true)
            } catch (e: Exception) {
                if (e is ResolvableApiException) {
                    e.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                } else {
                    currentLocationNeedsSatisfied.emit(false)
                }
            }
        }
}