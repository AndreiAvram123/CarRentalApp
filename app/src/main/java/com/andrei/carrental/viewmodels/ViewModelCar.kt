package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.carrental.entities.Car
import com.andrei.carrental.entities.BookingDate
import com.andrei.engine.State
import com.andrei.engine.repository.implementation.CarRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit

class ViewModelCar @ViewModelInject constructor(
        private val carRepositoryImpl: CarRepositoryImpl
) : ViewModel (){

    val searchSuggestions :MutableLiveData<State<List<Car>>> by lazy {
        carRepositoryImpl.searchSuggestions
    }


    val currentCarID:MutableLiveData<Long> by lazy {
      carRepositoryImpl.currentCarID
    }

    val currentSelectedCar: LiveData<State<Car>> by lazy {
        carRepositoryImpl.currentSelectedCar
    }

    val unavailableCarDates :LiveData<State<List<BookingDate>>> by lazy {
        carRepositoryImpl.unavailableDates
    }

   val currentSelectedDays:MutableLiveData<BookingDate> by lazy {
       MutableLiveData()
   }

   val totalAmountToPay:MediatorLiveData<Double> by lazy {
       MediatorLiveData<Double>().apply {
           addSource(currentSelectedDays){rentalPeriod ->

               val carState = currentSelectedCar.value
               if(carState is State.Success
                       && carState.data != null
                       && rentalPeriod != null
               ){
                  value = calculateTotalAmount(rentalPeriod,carState.data.pricePerDay)
                   val list = mutableListOf<String>()
                   list.isNullOrEmpty()
               }
           }
           addSource(currentSelectedCar){
               car ->
                   val currentSelectedDaysValue = currentSelectedDays.value
                   if (currentSelectedDaysValue!= null &&
                           car is State.Success
                           && car.data != null) {
                               value = calculateTotalAmount(currentSelectedDaysValue, car.data.pricePerDay)
                   }
               }
       }
   }



    private fun calculateTotalAmount(bookingDate: BookingDate, price :Double):Double{
       return  (ChronoUnit.DAYS.between(bookingDate.startDate,bookingDate.endDate) + 1) * price
    }

    fun fetchSuggestions(query:String, location:LatLng) {
        viewModelScope.launch { carRepositoryImpl.fetchSuggestions(query,location) }
    }

}