package com.andrei.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentConfirmSelectionBinding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*


class ConfirmSelectionFragment : Fragment() {


    private lateinit var binding:FragmentConfirmSelectionBinding
   private val navArgs:ConfirmSelectionFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentConfirmSelectionBinding.inflate(inflater,container,false)
        val startDate =
        Instant.ofEpochMilli(navArgs.startDate * 1000).atZone(ZoneId.systemDefault()).toLocalDate()
        val endDate = Instant.ofEpochMilli(navArgs.endDate * 1000).atZone(ZoneId.systemDefault()).toLocalDate()
        val days = ChronoUnit.DAYS.between(startDate,endDate) + 1
        binding.tvNumberDays.text = getString(R.string.days ,days.toString() )
        return binding.root
    }
    
}