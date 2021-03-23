package com.andrei.UI.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.andrei.carrental.R
import com.andrei.carrental.viewmodels.ViewModelLocation
import com.andrei.engine.DTOEntities.GeoPoint
import com.andrei.engine.State
import com.andrei.utils.LocationSettingsHandler
import com.andrei.utils.fetchBitmap
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class CurrentLocationFragment : BaseFragment(R.layout.fragment_current_location) {


    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    private val viewModelLocation : ViewModelLocation by activityViewModels()

    private  var map :GoogleMap? = null

    private val markersOnMap:MutableMap<Marker,Long> = mutableMapOf()

    private lateinit var mapFragment : SupportMapFragment

    private val locationRequest = LocationRequest.create().apply {
        interval = 10000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }



    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        if(permissionHandlerFragment.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            enableLocation()
        }else{
            permissionHandlerFragment.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION){
                if(it) {
                    enableLocation()
                }
            }
        }
        googleMap.setOnMarkerClickListener {
                 val id = markersOnMap[it]
                 if(id != null){
                  findNavController().navigate( CurrentLocationFragmentDirections.actionGlobalToExpandedCarFragment(id))
                 }

            true
        }
    }

    private fun enableLocation (){
        map?.isMyLocationEnabled = true
        map?.uiSettings?.isMyLocationButtonEnabled = false
        getDeviceLocation()
    }

     override fun initializeUI() {
         lifecycleScope.launchWhenResumed {
             viewModelLocation.nearbyCars.collect {
                 clearMap()
                 val tempMap = map
                 if(it is State.Success && tempMap != null){
                     it.data.forEach {car->
                         if (car.mediaFiles.isNotEmpty()) {
                             fetchBitmap(requireContext(), car.mediaFiles.first().mediaURL)?.also {bitmap->
                                 addMarkerToMap(car.location,bitmap,car.id)
                             }

                         }
                     }
                 }
             }
         }
    }
    private fun clearMap(){
        map?.clear()
        markersOnMap.clear()
    }


    private fun addMarkerToMap(location:GeoPoint, bitmap:Bitmap, id:Long){
       map?.let {
            val marker = it.addMarker( MarkerOptions().position(
                LatLng(
                    location.latitude,
                    location.longitude
                ))).apply {
                setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
            }
            markersOnMap[marker] = id
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        requireAdequateLocationSettings()
    }

    private fun requireAdequateLocationSettings(){
        lifecycleScope.launchWhenResumed {
            LocationSettingsHandler.startLocationRequest(requireActivity(),locationRequest)
            LocationSettingsHandler.currentLocationNeedsSatisfied.collect {
                if(it){
                    mapFragment.getMapAsync(callback)
                    // Construct a PlaceDetectionClient.
                    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
                }
            }
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */

    private fun getDeviceLocation() {
            lifecycleScope.launchWhenResumed {
                try {
                    val lastLocation = mFusedLocationProviderClient.lastLocation.await()
                    val currentLocation = LatLng(
                        lastLocation.latitude,
                        lastLocation.longitude
                    )
                    viewModelLocation.setNewLocation(currentLocation)
                    moveCameraToLocation(currentLocation)
                    startLocationUpdates()
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
    }

    private fun moveCameraToLocation(location :LatLng){
        map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        location, 15.toFloat()
                )
        )
    }


    private fun startLocationUpdates() {
        val callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if(locationResult!=null){
                    locationResult.locations.firstOrNull()?.let{
                        moveCameraToLocation(LatLng(it.latitude,it.longitude))
                        mFusedLocationProviderClient.removeLocationUpdates(this)
                    }
                }
            }

        }
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest,callback, Looper.getMainLooper())
    }


}