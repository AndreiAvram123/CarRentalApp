package com.andrei.UI.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.andrei.carrental.R
import com.andrei.carrental.viewmodels.ViewModelLocation
import com.andrei.engine.DTOEntities.GeoPoint
import com.andrei.engine.State
import com.andrei.utils.LocationSettingsHandler
import com.andrei.utils.fetchBitmap
import com.andrei.utils.reObserve
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
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class CurrentLocationFragment : BaseFragment(R.layout.fragment_current_location) {


    @Inject
    lateinit var locationSettingsHandler: LocationSettingsHandler

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    private val viewModelLocation : ViewModelLocation by activityViewModels()

    private  var map :GoogleMap? = null

    private val markersOnMap:MutableMap<Marker,Long> = mutableMapOf()

    private lateinit var mapFragment : SupportMapFragment

    private val locationRequest = LocationRequest.create().apply {
        interval = 10000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val observerLocationSettings = Observer<Boolean>{
        if(it){
            mapFragment.getMapAsync(callback)
            // Construct a PlaceDetectionClient.
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        }

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
                             fetchBitmap(requireContext(), car.mediaFiles.first().mediaURL) { bitmap ->
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
       map?.addMarker(
                MarkerOptions().position(
                        LatLng(
                                location.latitude,
                                location.longitude
                        )
                ))?.apply {
                  setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
                }?.also {
                    markersOnMap[it] = id
       }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        locationSettingsHandler.locationRequest = locationRequest
        locationSettingsHandler.startLocationRequest()

        locationSettingsHandler.currentLocationNeedsSatisfied.reObserve(viewLifecycleOwner,observerLocationSettings)
    }
    /**
     * Gets the current location of the device, and positions the map's camera.
     */

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
           mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
               if (it != null){
               val currentLocation = LatLng(
                       it.latitude,
                       it.longitude
               )
               viewModelLocation.setNewLocation(currentLocation)
              moveCameraToLocation(currentLocation)
           }else{
               startLocationUpdates()
           }
           }
        } catch (e: SecurityException) {
            Timber.e(e)
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