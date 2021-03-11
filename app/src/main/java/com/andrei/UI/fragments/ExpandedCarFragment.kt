package com.andrei.UI.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentExpandedCarBinding
import com.andrei.carrental.entities.Car
import com.andrei.carrental.entities.MediaFile
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.engine.DTOEntities.toLatLng
import com.andrei.engine.State
import com.andrei.utils.loadFromURl
import com.andrei.utils.reObserve
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions


class ExpandedCarFragment : Fragment(R.layout.fragment_expanded_car) {


    private  val  binding:FragmentExpandedCarBinding by viewBinding()

    private val viewModelCar:ViewModelCar by activityViewModels()

    private val navArgs:ExpandedCarFragmentArgs by navArgs()

    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    private val mapReadyCallback = OnMapReadyCallback{ map ->
        binding.isMapLoading = false
         binding.car?.let {
           val carLocation = it.location.toLatLng()
           map.addMarker(MarkerOptions().position(carLocation))
           map.moveCamera(
                   CameraUpdateFactory
                           .newLatLngZoom(carLocation, 15.toFloat())
           )
       }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelCar.currentCarID.postValue(navArgs.carID)

        initializeUI(savedInstanceState)


    }


    private fun initializeUI(bundle:Bundle?) {
        var mapViewBundle: Bundle? = null
        if (bundle != null) {
            mapViewBundle = bundle.getBundle(MAP_VIEW_BUNDLE_KEY)
        }
        binding.mapCarLocation.onCreate(mapViewBundle)

        binding.backButtonExpanded.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModelCar.currentSelectedCar.reObserve(viewLifecycleOwner){
           updateUI(it)
        }
        binding.selectDatesButton.setOnClickListener {
         val action = ExpandedCarFragmentDirections.actionExpandedToChooseDatesFragment()
            findNavController().navigate(action)
        }

    }


    private fun initializeMap() {
        binding.isMapLoading = true
        binding.mapCarLocation.getMapAsync(mapReadyCallback)
    }



    override fun onStart() {
        super.onStart()
        binding.mapCarLocation.onStart()
    }

    private fun updateUI(state : State<Car>) {
        when (state) {
            is State.Success -> {
                state.data?.let {
                    binding.car = it
                    initializeMap()
                    updateCarouselVies(it.mediaFiles)
                    binding.isLoading = false
                }
            }
            is State.Loading -> {
                binding.isLoading = true
            }
            is State.Error -> {

            }
        }
    }

   private  fun updateCarouselVies(mediaFiles:List<MediaFile>){
        binding.carouselCarExpanded.apply {
            size = mediaFiles.size
            setCarouselViewListener { view, position ->
                view.findViewById<ImageView>(R.id.image_item_carousel).loadFromURl(mediaFiles[position].mediaURL)
            }
            show()
        }
    }
}
