package com.andrei.engine.repository

import androidx.lifecycle.MutableLiveData
import com.andrei.engine.CallRunner
import com.andrei.engine.CarToRent
import com.andrei.engine.State
import com.andrei.engine.repositoryInterfaces.CarRepoInterface
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class CarRepositoryImpl(
        private val coroutineContext:CoroutineContext = GlobalScope.coroutineContext) {


     private val callRunner = CallRunner()
     private val retrofit = Retrofit.Builder().baseUrl("https://car-rental-api-kotlin.herokuapp.com").addConverterFactory(
         GsonConverterFactory.create()).build()
     private val  repo = retrofit.create(CarRepoInterface::class.java)

    val nearbyCars :MutableLiveData<List<CarToRent>> by lazy {
        MutableLiveData<List<CarToRent>>()
    }


    suspend fun fetchNearbyCars (position:LatLng){
         callRunner.makeCall(repo.getNearbyCars(latitude = position.latitude, longitude = position.longitude)){
             if (it is State.Success){
                 nearbyCars.postValue(it.data)
             }
         }
     }
}