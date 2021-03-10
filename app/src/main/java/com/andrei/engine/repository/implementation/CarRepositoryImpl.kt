package com.andrei.engine.repository.implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.andrei.carrental.entities.Car
import com.andrei.carrental.entities.BookingDate
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.toBookingDate
import com.andrei.engine.State
import com.andrei.engine.repositoryInterfaces.CarAPI
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
        private val callRunner:CallRunner,
        private val carAPI:CarAPI,
) {


    val searchSuggestions by lazy {
        MutableLiveData<State<List<Car>>>()
    }

    val currentCarID: MutableLiveData<Long> = MutableLiveData()

    val currentSelectedCar: LiveData<State<Car>> =
        Transformations.switchMap(currentCarID) { carID ->
            return@switchMap fetchCarById(carID).asLiveData()
        }


    val unavailableDates: LiveData<State<List<BookingDate>>> =
        Transformations.switchMap(currentCarID) { carId ->
            return@switchMap fetchUnavailableDates(carId).asLiveData()
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

    private fun fetchCarById(id:Long) =
        callRunner.makeApiCall{carAPI.getCarByID(id)}


   private  fun fetchUnavailableDates(carID:Long)  =
        callRunner.makeApiCall{carAPI.getUnavailableDates(carID)}.transform{
            when(it){
                is State.Success-> emit(State.Success(it.data?.map { date -> date.toBookingDate() }))
                is State.Loading -> emit(State.Loading)
                is State.Error -> emit(State.Error(it.error))
        }
    }
}