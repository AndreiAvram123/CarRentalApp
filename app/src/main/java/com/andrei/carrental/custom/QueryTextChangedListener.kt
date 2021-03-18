package com.andrei.carrental.custom

import android.widget.SearchView

interface QueryTextChangedListener : androidx.appcompat.widget.SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean = true

}