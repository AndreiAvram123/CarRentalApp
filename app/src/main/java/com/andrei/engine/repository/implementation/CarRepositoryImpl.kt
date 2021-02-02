package com.andrei.engine.repository.implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.andrei.carrental.entities.CarToRent
import com.andrei.carrental.entities.BookingDate
import com.andrei.engine.CallRunner
import com.andrei.carrental.entities.CarSearchEntity
import com.andrei.engine.DTOEntities.toBooking
import com.andrei.engine.DTOEntities.toBookingDTO
import com.andrei.engine.State
import com.andrei.engine.repositoryInterfaces.CarRepoInterface
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
        private val callRunner:CallRunner,
        private val carRepo:CarRepoInterface,
){



     val searchSuggestions by lazy {
         MutableLiveData<State<List<CarSearchEntity>>>()
     }

    val currentCarID : MutableLiveData<Long> = MutableLiveData()

    val currentSelectedCar : LiveData<State<CarToRent>> = Transformations.switchMap(currentCarID) { carID ->
        return@switchMap fetchCarById(carID).asLiveData()
    }


    val unavailableDates : LiveData<State<List<BookingDate>>>  = Transformations.switchMap(currentCarID) { carId ->
        return@switchMap fetchUnavailableDates(carId).asLiveData()
    }


     fun fetchNearbyCars (position:LatLng) = flow{
         callRunner.makeApiCall(carRepo.getNearbyCars(latitude = position.latitude, longitude = position.longitude)){
                 emit(it)
         }
     }

    suspend fun fetchSuggestions(query:String, position: LatLng){
        callRunner.makeApiCall(carRepo.search(latitude = position.latitude, longitude = position.longitude,query = query)){
            searchSuggestions.postValue(it)
        }
    }

    private fun fetchCarById(id:Long) = flow {
        callRunner.makeApiCall(carRepo.getCarByID(id)){
            emit(it)
        }
    }


   private  fun fetchUnavailableDates(carID:Long)  = flow{
        callRunner.makeApiCall(carRepo.getUnavailableDates(carID)){
            when(it){
                is State.Success-> emit(State.Success(it.data?.map { date -> date.toBooking() }))
                is State.Loading -> emit(State.Loading)
                is State.Error -> emit(State.Error(it.error))
            }
        }
    }.flowOn(GlobalScope.coroutineContext)
}