package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.carrental.entities.CarToRent
import com.andrei.engine.State
import com.andrei.engine.repository.implementation.CarRepositoryImpl
import com.google.android.gms.maps.model.LatLng

class ViewModelLocation @ViewModelInject constructor(
    private val carRepositoryImpl: CarRepositoryImpl
) : ViewModel() {


    val currentLocation : MutableLiveData<LatLng> = MutableLiveData()

    val nearbyCars :LiveData<State<List<CarToRent>>> = Transformations.switchMap(currentLocation) {
            carRepositoryImpl.fetchNearbyCars(it).asLiveData()
    }
}