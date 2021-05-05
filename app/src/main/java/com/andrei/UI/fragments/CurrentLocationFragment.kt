package com.andrei.UI.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
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
import com.andrei.utils.isResultOk
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

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class CurrentLocationFragment : BaseFragment(R.layout.fragment_current_location) {


    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    private val viewModelLocation : ViewModelLocation by activityViewModels()
    private val locationSettingsHandler = LocationSettingsHandler()

    private lateinit var map :GoogleMap


    private lateinit var mapFragment : SupportMapFragment

    private val locationRequest = LocationRequest.create().apply {
        interval = 10000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }



    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
         permissionHandlerFragment.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION){granted->
             if(granted){
                 enableLocation()
             }
         }
        googleMap.setOnMarkerClickListener {marker ->
                 marker.snippet?.toLong()?.let {
                     findNavController().navigate(CurrentLocationFragmentDirections.actionGlobalToExpandedCarFragment(it))
                 }

            true
        }
        startCollecting()
    }


    private fun enableLocation (){
        map.apply {
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = false
        }
        getDeviceLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(LocationSettingsHandler.request_check_settings == requestCode){
            locationSettingsHandler.currentLocationNeedsSatisfied.tryEmit(resultCode.isResultOk())
        }
    }


    private fun GoogleMap.addCustomMarker(location:GeoPoint, bitmap:Bitmap , id :Long):Marker{
        val marker = addMarker( MarkerOptions().position(
                LatLng(
                        location.latitude,
                        location.longitude
                )))?.apply {
            setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
            snippet = id.toString()
        }
        return marker!!
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        requireAdequateLocationSettings()
    }

    override fun initializeUI() {
    }

    private fun startCollecting(){
        lifecycleScope.launchWhenResumed {
            viewModelLocation.nearbyCars.collect {
                map.clear()
                if(it is State.Success){
                    it.data.forEach { car->
                        if (car.mediaFiles.isNotEmpty()) {
                            fetchBitmap(requireContext(), car.mediaFiles.first().mediaURL, maxWidth = 300)?.let {bitmap->
                                  map.addCustomMarker(
                                        location = car.location,
                                        bitmap = bitmap,
                                        id= car.id)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun requireAdequateLocationSettings(){
        lifecycleScope.launchWhenResumed {
            locationSettingsHandler.startLocationRequest(requireActivity(),locationRequest)
            locationSettingsHandler.currentLocationNeedsSatisfied.collect {
                if(it){
                    mapFragment.getMapAsync(callback)
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
                    e.printStackTrace()
                }
            }
    }

    private fun moveCameraToLocation(location :LatLng){
        val update = CameraUpdateFactory.newLatLngZoom(location,15.toFloat())
        map.animateCamera(update)
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