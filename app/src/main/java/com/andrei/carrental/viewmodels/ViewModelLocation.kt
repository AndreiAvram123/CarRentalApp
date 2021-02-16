package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.carrental.entities.Car
import com.andrei.engine.State
import com.andrei.engine.repository.implementation.CarRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelLocation @Inject constructor(
    private val carRepositoryImpl: CarRepositoryImpl
) : ViewModel() {


    val currentLocation : MutableLiveData<LatLng> = MutableLiveData()

    val nearbyCars :LiveData<State<List<Car>>> = Transformations.switchMap(currentLocation) {
            carRepositoryImpl.fetchNearbyCars(it).asLiveData()
    }
}