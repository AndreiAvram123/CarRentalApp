package com.andrei.UI.fragments

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.CustomDivider
import com.andrei.UI.adapters.CarsRVSearchAdapter
import com.andrei.carrental.R
import com.andrei.carrental.custom.QueryTextChangedListener

import com.andrei.carrental.databinding.FragmentHomeBinding
import com.andrei.carrental.entities.Car
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.carrental.viewmodels.ViewModelLocation
import com.andrei.engine.State
import com.andrei.utils.hide
import com.andrei.utils.reObserve
import com.andrei.utils.show
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HomeFragment : BaseFragment(R.layout.fragment_home) {

  private val   binding:FragmentHomeBinding by viewBinding()


  private var currentLocation :LatLng? = null

  private val viewModelCar:ViewModelCar by activityViewModels()
    private val viewModelLocation:ViewModelLocation by activityViewModels()

    private val currentLocationObserver = Observer<LatLng>{
        if(it != null){
            currentLocation = it
        }
    }

    private val nearbyCarsObserver = Observer<State<List<Car>>>{
        when(it){
            is State.Success -> bottomSheetRVAdapter.setData(it.data)
        }
    }

  private val searchRVAdapter:CarsRVSearchAdapter by lazy {
      CarsRVSearchAdapter()
  }

  private val bottomSheetRVAdapter:CarsRVSearchAdapter by lazy {
      CarsRVSearchAdapter()
  }


    override fun initializeUI() {
        configureSearchView()
        configureBottomSheet()
        viewModelLocation.currentLocation.reObserve(viewLifecycleOwner,currentLocationObserver)
        viewModelLocation.nearbyCars.reObserve(viewLifecycleOwner,nearbyCarsObserver)
    }

    private fun configureBottomSheet() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheetNearby)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.rvSheetNearbyCars.apply {
            adapter = bottomSheetRVAdapter
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun configureSearchView() {
        configureRecyclerView()
        binding.searchViewCars.setOnClickListener {  binding.searchViewCars.isIconified = false }

        binding.searchViewCars.setOnQueryTextListener(object : QueryTextChangedListener {

            override fun onQueryTextChange(query: String): Boolean {
                if(query.isNotEmpty()){
                    currentLocation?.let{ viewModelCar.fetchSuggestions(query,it)}
                }else{
                   searchRVAdapter.clearData()
                }
                return true
            }

        })
    }

    private fun configureRecyclerView() {
        binding.recyclerViewSuggestions.apply {
            adapter = searchRVAdapter
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModelCar.searchSuggestions.reObserve(viewLifecycleOwner){
            when(it){
                is State.Success -> {
                    searchRVAdapter.setData(it.data)
                       binding.pbSearch.hide()
                }
                is State.Loading -> binding.pbSearch.show()
                is State.Error ->{
                    binding.pbSearch.hide()
                }
            }
        }
    }

}