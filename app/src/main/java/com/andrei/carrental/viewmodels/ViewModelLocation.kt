package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.carrental.entities.CarToRent
import com.andrei.engine.State
import com.andrei.engine.repository.CarRepositoryImpl
import com.google.android.gms.maps.model.LatLng

class ViewModelLocation : ViewModel() {

    private val carRepositoryImpl = CarRepositoryImpl()

    val currentLocation : MutableLiveData<LatLng> = MutableLiveData()

    val nearbyCars :LiveData<State<List<CarToRent>>> = Transformations.switchMap(currentLocation) {
            carRepositoryImpl.fetchNearbyCars(it).asLiveData()
    }
}