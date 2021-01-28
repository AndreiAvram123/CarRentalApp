package com.andrei.carrental.bindingAdapters

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorText")
fun bindErrorTextToField(textInputLayout: TextInputLayout, text: String?){
   if(!text.isNullOrBlank())   {
       textInputLayout.apply {
           error = text
           isErrorEnabled = true
       }
   }else{
        textInputLayout.isErrorEnabled = false
   }
}