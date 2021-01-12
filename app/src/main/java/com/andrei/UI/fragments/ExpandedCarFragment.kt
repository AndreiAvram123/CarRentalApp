package com.andrei.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentExpandedCarBinding
import com.andrei.carrental.entities.Car
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.engine.State
import com.andrei.utils.loadFromURL
import com.andrei.utils.reObserve


class ExpandedCarFragment : Fragment() {


    private lateinit var binding:FragmentExpandedCarBinding

    private val viewModelCar:ViewModelCar by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentExpandedCarBinding.inflate(inflater,container,false)


        viewModelCar.fetchCarById(1).reObserve(viewLifecycleOwner){
            when(it){
                is State.Success -> updateUI(it.data)
                is State.Loading -> {}
                is State.Error -> {}
            }
        }

        return binding.root
    }

    private fun updateUI(car:Car){
        binding.car = car

        binding.carouselCarExpanded.apply {
            size  = car.images.size
            setCarouselViewListener { view, position ->
                view.findViewById<ImageView>(R.id.image_item_carousel).loadFromURL(car.images[position].imagePath)
            }
            show()
        }
    }

}