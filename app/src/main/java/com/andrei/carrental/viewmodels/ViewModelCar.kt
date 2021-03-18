package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.carrental.entities.Booking
import com.andrei.carrental.entities.Car
import com.andrei.carrental.entities.BookingDate
import com.andrei.engine.State
import com.andrei.engine.repository.implementation.CarRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ViewModelCar @Inject constructor(
        private val carRepositoryImpl: CarRepositoryImpl
) : ViewModel (){

    val searchSuggestions :MutableLiveData<State<List<Car>>> by lazy {
        carRepositoryImpl.searchSuggestions
    }


    private val _currentSelectedCar: MutableStateFlow<State<Car>> = MutableStateFlow(State.Loading)

    val currentSelectedCar:StateFlow<State<Car>>
    get() = _currentSelectedCar.asStateFlow()


    private val _unavailableDates:MutableStateFlow<State<List<BookingDate>>> = MutableStateFlow(State.Loading)

    val unavailableDates:StateFlow<State<List<BookingDate>>>
    get() = _unavailableDates.asStateFlow()

   val currentSelectedDays:MutableLiveData<BookingDate> by lazy {
       MutableLiveData()
   }

    fun getCar(carID:Long){
        viewModelScope.launch(Dispatchers.IO) {
             carRepositoryImpl.fetchCarById(carID).collect {
                 _currentSelectedCar.emit(it)
             }
        }
    }

    private val _totalAmountToPay:MutableStateFlow<Double?> = MutableStateFlow(null)

     val totalAmountToPay:StateFlow<Double?>
     get() = _totalAmountToPay.asStateFlow()



    fun calculateTotalAmountToPay(){
        viewModelScope.launch {
            combine(currentSelectedCar, currentSelectedDays.asFlow().filterNotNull()) { car, date ->
                if (car is State.Success) {
                   return@combine calculateTotalAmount(date, car.data.pricePerDay)
                }
                return@combine null
            }.collect {
                _totalAmountToPay.emit(it)
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