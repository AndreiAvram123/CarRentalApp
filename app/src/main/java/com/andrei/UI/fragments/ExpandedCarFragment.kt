package com.andrei.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentExpandedCarBinding
import com.andrei.utils.loadFromURL


class ExpandedCarFragment : Fragment() {


    private lateinit var binding:FragmentExpandedCarBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentExpandedCarBinding.inflate(inflater,container,false)

        binding.carouselCarExpanded.apply {
            size  = 10
           setCarouselViewListener { view, position ->
               view.findViewById<ImageView>(R.id.image_item_carousel).loadFromURL("https://robohash.org/139.162.116.133.png")
           }
            show()
        }
        return binding.root
    }


}