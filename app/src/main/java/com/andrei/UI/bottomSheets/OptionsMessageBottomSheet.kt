package com.andrei.UI.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.dialogViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.BottomSheetOptionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OptionsMessageBottomSheet(
        private val unsend: () -> Unit,
       ): BottomSheetDialogFragment() {

    private   var binding:BottomSheetOptionsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetOptionsBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        attachListener()
    }

    private fun attachListener() {
       binding?.let {
           it.unsendBt.setOnClickListener {
               unsend.invoke()
           }
       }
    }
     fun showLoading(){
         binding?.apply {
             progressBar.visibility = View.VISIBLE
             unsendBt.visibility = View.INVISIBLE
         }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}