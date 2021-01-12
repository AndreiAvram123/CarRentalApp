package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.engine.CarToRent
import com.andrei.engine.repository.CarRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class ViewModelCar : ViewModel (){
    private  val  carRepositoryImpl: CarRepositoryImpl = CarRepositoryImpl()

    val nearbyCars : MutableLiveData<List<CarToRent>> by lazy {
        carRepositoryImpl.nearbyCars
    }

    fun fetchNearbyCars(location :LatLng){
        viewModelScope.launch { carRepositoryImpl.fetchNearbyCars(location) }
    }
}