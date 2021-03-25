package com.andrei.UI.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.andrei.carrental.R
import com.andrei.carrental.UserDataManager
import com.andrei.carrental.databinding.FragmentExpandedCarBinding
import com.andrei.carrental.entities.Car
import com.andrei.carrental.entities.MediaFile
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.engine.DTOEntities.toLatLng
import com.andrei.engine.State
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class ExpandedCarFragment : BaseFragment(R.layout.fragment_expanded_car) {


    private  val  binding:FragmentExpandedCarBinding by viewBinding()

    private val viewModelCar:ViewModelCar by activityViewModels()

    private val navArgs:ExpandedCarFragmentArgs by navArgs()

    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    private  val mapView: MapView by lazy{
        binding.mapCarLocation
    }

    private val mapReadyCallback = OnMapReadyCallback{ map ->
        binding.isMapLoaded = true
        binding.car?.let {
            val carLocation = it.location.toLatLng()
            map.addMarker(MarkerOptions().position(carLocation))
            map.animateCamera(
                    CameraUpdateFactory
                            .newLatLngZoom(carLocation, 15.toFloat())
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapViewBundle = savedInstanceState?.getBundle(MAP_VIEW_BUNDLE_KEY)
        mapView.onCreate(mapViewBundle)
        viewModelCar.getCar(navArgs.carID)
    }



    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapCarLocation.onLowMemory()
    }

    override fun initializeUI() {
        binding.backButtonExpanded.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launchWhenResumed {
            viewModelCar.currentSelectedCar.collect {
                updateUI(it)
            }
        }

        binding.selectDatesButton.setOnClickListener {
            val action = ExpandedCarFragmentDirections.actionExpandedToChooseDatesFragment()
            findNavController().navigate(action)
        }



    }


    private fun getMap() {
        mapView.getMapAsync(mapReadyCallback)
    }




    private fun updateUI(state : State<Car>) {
        when (state) {
            is State.Success -> {
                binding.apply {
                    car = state.data
                    imageLender.setOnClickListener {
                        navigateToUserProfile(state.data.basicUser.userID)
                    }
                }
                getMap()
                updateCarouselVies(state.data.mediaFiles)
            }
        }
    }
    private fun navigateToUserProfile(userID:Long){
            val action = ExpandedCarFragmentDirections.actionExpandedToProfileFragment(userID)
            findNavController().navigate(action)
    }

    private  fun updateCarouselVies(mediaFiles:List<MediaFile>){
        binding.carouselCarExpanded.apply {
            size = mediaFiles.size
            setCarouselViewListener { view, position ->
                view.findViewById<ImageView>(R.id.image_item_carousel).load(mediaFiles[position].mediaURL)
            }
            show()
        }
    }
}
