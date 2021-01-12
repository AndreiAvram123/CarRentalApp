package com.andrei.UI.fragments

import android.database.MatrixCursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.UI.adapters.CustomCursorAdapter
import com.andrei.UI.adapters.CustomDivider
import com.andrei.UI.adapters.SuggestionsAdapter
import com.andrei.carrental.R

import com.andrei.carrental.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

  private lateinit var binding:FragmentHomeBinding
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
        binding.searchViewCars.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isNotEmpty()){
                    suggestionsAdapter.setData(listOf("pupu","lalala"))
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
    }
}