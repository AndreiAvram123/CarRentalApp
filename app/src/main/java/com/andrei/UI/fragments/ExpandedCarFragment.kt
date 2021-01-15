package com.andrei.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentExpandedCarBinding
import com.andrei.carrental.entities.CarToRent
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.engine.State
import com.andrei.utils.loadFromURL
import com.andrei.utils.reObserve
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class ExpandedCarFragment : Fragment() {


    private lateinit var binding:FragmentExpandedCarBinding

    private val viewModelCar:ViewModelCar by activityViewModels()

    private val navArgs:ExpandedCarFragmentArgs by navArgs()
    private var carToRent:CarToRent? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    private val mapReadyCallback = OnMapReadyCallback{ map ->
       carToRent?.let {
           val carLocation =LatLng(it.latitude, it.longitude)
           map.addMarker(MarkerOptions().position(carLocation))
           map.moveCamera(
                   CameraUpdateFactory
                           .newLatLngZoom(carLocation, 15.toFloat())
           )
       }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentExpandedCarBinding.inflate(inflater, container, false)

        viewModelCar.currentCarID.postValue(navArgs.carID)

        initializeUI(savedInstanceState)


        return binding.root
    }

    private fun initializeUI(bundle:Bundle?) {
        var mapViewBundle: Bundle? = null
        if (bundle != null) {
            mapViewBundle = bundle.getBundle(MAP_VIEW_BUNDLE_KEY)
        }
        binding.mapExpandedFragment.onCreate(mapViewBundle)

        binding.backButtonExpanded.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModelCar.currentSelectedCar.reObserve(viewLifecycleOwner){
            when (it) {
                is State.Success -> if (it.data != null) {
                    carToRent = it.data
                    updateUI()
                    initializeMap()
                }
                is State.Loading -> {
                }
                is State.Error -> {
                }

            }
        }

    }


    private fun initializeMap() {
        binding.mapExpandedFragment.getMapAsync(mapReadyCallback)
    }



    override fun onStart() {
        super.onStart()
        binding.mapExpandedFragment.onStart()
    }

    private fun updateUI() {
        carToRent?.let {
            binding.car = it

            binding.carouselCarExpanded.apply {
                size = it.images.size
                setCarouselViewListener { view, position ->
                    view.findViewById<ImageView>(R.id.image_item_carousel).loadFromURL(it.images[position].imagePath)
                }
                show()
            }
        }
    }

}