package com.andrei.carrental.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class ViewModelLocation : ViewModel() {

    val currentLocation : MutableLiveData<LatLng> = MutableLiveData()
}