package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.carrental.entities.CarSearchEntity
import com.andrei.carrental.entities.CarToRent
import com.andrei.carrental.entities.RentalPeriod
import com.andrei.engine.State
import com.andrei.engine.repository.CarRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit

class ViewModelCar : ViewModel (){
    private  val  carRepositoryImpl: CarRepositoryImpl = CarRepositoryImpl()

    val searchSuggestions :MutableLiveData<State<List<CarSearchEntity>>> by lazy {
        carRepositoryImpl.searchSuggestions
    }


    val currentCarID:MutableLiveData<Long> by lazy {
      carRepositoryImpl.currentCarID
    }

    val currentSelectedCar: LiveData<State<CarToRent>> by lazy {
        carRepositoryImpl.currentSelectedCar
    }

    val unavailableCarDates :LiveData<State<List<RentalPeriod>>> by lazy {
        carRepositoryImpl.unavailableDates
    }

   val currentSelectedDays:MutableLiveData<RentalPeriod> by lazy {
       MutableLiveData()
   }

   val totalAmountToPay:MediatorLiveData<Double> by lazy {
       MediatorLiveData<Double>().also {
           it.addSource(currentSelectedDays){rentalPeriod ->
               val carState = currentSelectedCar.value
               if(carState is State.Success
                       && carState.data != null
                       && rentalPeriod != null
               ){
                   totalAmountToPay.value = calculateTotalAmount(rentalPeriod,carState.data.pricePerDay)
               }
           }
           it.addSource(currentSelectedCar){
               car ->
                   val currentSelectedDaysValue = currentSelectedDays.value
                   if (currentSelectedDaysValue!= null &&
                           car is State.Success
                           && car.data != null) {
                       totalAmountToPay.value = calculateTotalAmount(currentSelectedDaysValue, car.data.pricePerDay)
                   }
               }
       }
   }


    private fun calculateTotalAmount(rentalPeriod: RentalPeriod, price :Double):Double{
       return  (ChronoUnit.DAYS.between(rentalPeriod.startDate,rentalPeriod.endDate) + 1) * price
    }

    fun fetchSuggestions(query:String, location:LatLng) {
        viewModelScope.launch { carRepositoryImpl.fetchSuggestions(query,location) }
    }


}