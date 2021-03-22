package com.andrei.kit.bindingAdapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.andrei.carrental.entities.MediaFile
import com.andrei.utils.hide
import com.andrei.utils.show

import com.google.android.material.textfield.TextInputLayout

import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("imageFromURL")
fun bindImageFromURL(imageView: ImageView,
                     mediaFile: MediaFile?) {
    if (mediaFile != null) {
       imageView.load(mediaFile.mediaURL)
    }
}


@BindingAdapter("isVisible")
fun changeVisibilityBasedOnBoolean(view :View, visible:Boolean){
     if(visible){
         view.show()
     }else{
         view.hide()
     }
}

@BindingAdapter("endIconVisible")
fun showEndIcon(field:TextInputLayout,visible: Boolean){
   field.isEndIconVisible = visible
}