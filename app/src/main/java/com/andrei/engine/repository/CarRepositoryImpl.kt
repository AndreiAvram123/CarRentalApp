package com.andrei.engine.repository

import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.entities.Car
import com.andrei.carrental.entities.CarSearchEntity
import com.andrei.engine.CallRunner
import com.andrei.engine.CarToRent
import com.andrei.engine.State
import com.andrei.engine.configuration.AuthInterceptor
import com.andrei.engine.repositoryInterfaces.CarRepoInterface
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class CarRepositoryImpl {


     private val callRunner = CallRunner()
     private val retrofit = Retrofit.Builder().baseUrl("https://car-rental-api-kotlin.herokuapp.com").addConverterFactory(
         GsonConverterFactory.create()).client(OkHttpClient.Builder().addInterceptor(AuthInterceptor("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0VXNlcm5hbWUiLCJleHAiOjE2MTMwNzkwMTksInVzZXJJRCI6NCwidXNlcm5hbWUiOiJ0ZXN0VXNlcm5hbWUifQ.aeHTZ4WAGjD1h-zCTXZFSM-aN6cD-81f0UWA05EQ8xvYnO7TgKu2jPvaM2jbhLswM2HexhgNxi3BV1yinWFZJQ")).build()).build()
     private val  repo = retrofit.create(CarRepoInterface::class.java)



    val nearbyCars :MutableLiveData<List<CarToRent>> by lazy {
        MutableLiveData<List<CarToRent>>()
    }
     val searchSuggestions by lazy {
         MutableLiveData<State<List<CarSearchEntity>>>()
     }


    suspend fun fetchNearbyCars (position:LatLng){
         callRunner.makeCall(repo.getNearbyCars(latitude = position.latitude, longitude = position.longitude)){
             if (it is State.Success){
                 nearbyCars.postValue(it.data)
             }
         }
     }

    suspend fun fetchSuggestions(query:String, position: LatLng){
        callRunner.makeCall(repo.search(latitude = position.latitude, longitude = position.longitude,query = query)){
            searchSuggestions.postValue(it)
        }
    }

    fun fetchCarById(id:Long) = flow {
        callRunner.makeCall(repo.fetchCarByID(id)){
            emit(it)
        }
    }
}