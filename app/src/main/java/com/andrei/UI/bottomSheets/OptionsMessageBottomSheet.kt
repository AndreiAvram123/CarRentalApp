package com.andrei.UI.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.dialogViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.BottomSheetOptionsBinding
import com.andrei.carrental.viewmodels.ViewModelChat
import com.andrei.engine.State
import com.andrei.utils.hide
import com.andrei.utils.reObserve
import com.andrei.utils.show
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OptionsMessageBottomSheet(
        private val unsend: () -> Unit
       ): BottomSheetDialogFragment() {

    private   val binding:BottomSheetOptionsBinding by viewBinding()
    private val viewModelChat:ViewModelChat by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_options,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        attachListener()
        attachObserver()
    }

    private fun attachObserver() {
        lifecycleScope.launch {
            viewModelChat.messageToUnsendState.collect {
                when(it){
                    is State.Loading ->
                        showLoading()
                    else -> {
                        closeSheet()
                    }
                }
            }
        }

    }

    private fun closeSheet(){
        dismiss()
    }

    private fun attachListener() {
           binding.unsendBt.setOnClickListener {
               unsend.invoke()
           }
    }
     private fun showLoading(){
         binding.apply {
             progressBar.show()
             unsendBt.hide()
         }
    }

}