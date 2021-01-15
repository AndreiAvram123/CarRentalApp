package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.carrental.entities.CarSearchEntity
import com.andrei.carrental.entities.CarToRent
import com.andrei.carrental.entities.RentalDate
import com.andrei.engine.State
import com.andrei.engine.repository.CarRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class ViewModelCar : ViewModel (){
    private  val  carRepositoryImpl: CarRepositoryImpl = CarRepositoryImpl()

    val searchSuggestions :MutableLiveData<State<List<CarSearchEntity>>> by lazy {
        carRepositoryImpl.searchSuggestions
    }

    val nearbyCars : MutableLiveData<List<CarToRent>> by lazy {
        carRepositoryImpl.nearbyCars
    }

    val currentCarID:MutableLiveData<Long> by lazy {
      carRepositoryImpl.currentCarID
    }

    val currentSelectedCar: LiveData<State<CarToRent>> by lazy {
        carRepositoryImpl.currentSelectedCar
    }


    val unavailableCarDates :LiveData<State<List<RentalDate>>> by lazy {
        carRepositoryImpl.unavailableDates
    }


    fun fetchNearbyCars(location :LatLng){
        viewModelScope.launch { carRepositoryImpl.fetchNearbyCars(location) }
    }


    fun fetchSuggestions(query:String, location:LatLng) {
        viewModelScope.launch { carRepositoryImpl.fetchSuggestions(query,location) }
    }


}