package com.andrei.UI.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.andrei.carrental.R
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.utils.PermissionHandlerFragment
import com.andrei.utils.fetchBitmap
import com.andrei.utils.reObserve
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CurrentLocationFragment : Fragment() {
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    private val viewModelCar:ViewModelCar by activityViewModels()
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private lateinit var map :GoogleMap
    private var lastKnownLocation: Location? = null
    private lateinit var  permissionHandlerFragment:PermissionHandlerFragment


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
    }

    @SuppressLint("MissingPermission")
    private fun enableLocation (){
        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = false
        getDeviceLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        permissionHandlerFragment = PermissionHandlerFragment(this)
        return inflater.inflate(R.layout.fragment_current_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // Construct a PlaceDetectionClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }
    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = mFusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    lastKnownLocation = task.result
                    lastKnownLocation?.let { location ->
                        val latLng = LatLng(
                            location.latitude,
                            location.longitude
                        )
                        map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                       latLng, 15.toFloat()
                                )
                        )
                       viewModelCar.nearbyCars.reObserve(viewLifecycleOwner){carsToRent->
                           carsToRent?.forEach {
                               fetchBitmap(requireContext(),it.imagePath){bitmap->
                                   map.addMarker(MarkerOptions().position(LatLng(
                                           location.latitude,
                                           location.longitude
                                   ))).setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
                               }
                           }
                           }
                        viewModelCar.fetchNearbyCars(latLng)
                       }
                } else {
                    map.moveCamera(
                        CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, 15.toFloat())
                    )
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) = permissionHandlerFragment.notifyChange(requestCode, grantResults)
}