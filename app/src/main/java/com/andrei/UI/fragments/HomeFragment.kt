package com.andrei.UI.fragments

import android.database.MatrixCursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.UI.adapters.CustomCursorAdapter
import com.andrei.UI.adapters.CustomDivider
import com.andrei.UI.adapters.SuggestionsAdapter
import com.andrei.carrental.R

import com.andrei.carrental.databinding.FragmentHomeBinding
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.engine.State
import com.andrei.utils.hide
import com.andrei.utils.reObserve
import com.andrei.utils.show
import com.google.android.gms.maps.model.LatLng

class HomeFragment : Fragment() {

  private lateinit var binding:FragmentHomeBinding

  private val viewModelCar:ViewModelCar by activityViewModels()

  private val suggestionsAdapter:SuggestionsAdapter by lazy {
      SuggestionsAdapter()
  }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        configureSearchView()
        return binding.root
    }

    private fun configureSearchView() {
        configureRecyclerView()
        binding.searchViewCars.setOnClickListener {  binding.searchViewCars.isIconified = false }

        binding.searchViewCars.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if(query.isNotEmpty()){
                    viewModelCar.fetchSuggestions(query, LatLng(53.4875,2.2901))
                }else{
                   suggestionsAdapter.clearData()
                }
                return true
            }

        })
    }

    private fun configureRecyclerView() {
        binding.recyclerViewSuggestions.apply {
            adapter = suggestionsAdapter
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModelCar.searchSuggestions.reObserve(viewLifecycleOwner){
            when(it){
                is State.Success -> {
                      suggestionsAdapter.setData(it.data)
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