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
import kotlinx.coroutines.launch

class LocationSettingsHandler {


   companion object{
       const  val REQUEST_CHECK_SETTINGS: Int = 3
   }


     val currentLocationRequest:MutableLiveData<LocationRequest> by lazy {
        MutableLiveData<LocationRequest>()
    }

    val currentLocationNeedsSatisfied:MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    fun buildLocationRequest (accuracy :Int,intervalValue:Long = 10000,fastestIntervalValue :Long? =null  ){
      GlobalScope.launch(Dispatchers.Main){
          currentLocationRequest.value = LocationRequest.create()?.apply {
              interval = intervalValue
              priority =  accuracy
              if(fastestIntervalValue != null){
                  fastestInterval = fastestIntervalValue
              }
          }
      }

    }

    fun requestChangeLocationForCurrentRequest(activity: Activity ){
       val currentLocationRequestTemp = currentLocationRequest.value
        if(currentLocationRequestTemp!= null){
          val builder = LocationSettingsRequest.Builder().addLocationRequest(currentLocationRequestTemp)

           val client = LocationServices.getSettingsClient(activity)
           val task = client.checkLocationSettings(builder.build())

           task.addOnSuccessListener {
               currentLocationNeedsSatisfied.postValue(true)
           }


            task.addOnFailureListener{
                if(it is ResolvableApiException){
                    try{
                        it.startResolutionForResult(activity,  REQUEST_CHECK_SETTINGS)
                    }catch (sendEx: IntentSender.SendIntentException){
                       currentLocationNeedsSatisfied.postValue(false)
                    }
                }else{
                    currentLocationNeedsSatisfied.postValue(false)
                }
            }
        }
    }
}