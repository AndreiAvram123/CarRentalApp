package com.andrei.engine.repository.implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.andrei.carrental.entities.Car
import com.andrei.carrental.entities.BookingDate
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.toBookingDate
import com.andrei.engine.State
import com.andrei.engine.repositoryInterfaces.CarAPI
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
        private val callRunner:CallRunner,
        private val carAPI:CarAPI,
) {


    private val searchSuggestions by lazy {
        MutableLiveData<State<List<Car>>>()
    }



    fun fetchNearbyCars(position: LatLng) =
        callRunner.makeApiCall {
            carAPI.getNearbyCars(latitude = position.latitude, longitude = position.longitude)
        }

    suspend fun fetchSuggestions(query: String, position: LatLng) {
        callRunner.makeApiCall {
            carAPI.search(
                latitude = position.latitude,
                longitude = position.longitude,
                query = query
            )
        }.collect {
            searchSuggestions.postValue(it)
        }
        }

      fun fetchCarById(id:Long):Flow<State<Car>> =
        callRunner.makeApiCall{carAPI.getCarByID(id)}


    fun fetchUnavailableDates(carID:Long)  =
        callRunner.makeApiCall{carAPI.getUnavailableDates(carID)}.transform{
            when(it){
                is State.Success-> emit(State.Success(it.data.map { date -> date.toBookingDate() }))
                is State.Loading -> emit(State.Loading)
                is State.Error -> emit(State.Error(it.error))
        }
    }
}