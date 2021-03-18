package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.carrental.entities.Car
import com.andrei.engine.State
import com.andrei.engine.repository.implementation.CarRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelLocation @Inject constructor(
    private val carRepositoryImpl: CarRepositoryImpl
) : ViewModel() {


    private val _currentLocation : MutableStateFlow<LatLng?> = MutableStateFlow(null)

     val currentLocation : StateFlow<LatLng?>
     get() = _currentLocation.asStateFlow()

    private val _nearbyCars:MutableStateFlow<State<List<Car>>> = MutableStateFlow(State.Loading)
    val nearbyCars:StateFlow<State<List<Car>>>
    get() = _nearbyCars.asStateFlow()



    init {
        viewModelScope.launch {
            currentLocation.filterNotNull().collect {location->
              carRepositoryImpl.fetchNearbyCars(location).collect {
                 _nearbyCars.emit(it)
              }
            }
        }
    }

    fun setNewLocation(newLocation:LatLng){
        viewModelScope.launch {
            _currentLocation.emit(newLocation)
        }
    }
}