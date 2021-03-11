package com.andrei.kit.bindingAdapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.andrei.carrental.entities.MediaFile
import com.andrei.utils.hide
import com.andrei.utils.loadFromURl
import com.andrei.utils.show

import com.google.android.material.textfield.TextInputLayout

import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("imageFromURL")
fun bindImageFromURL(imageView: ImageView,
                     mediaFile: MediaFile?) {
    if (mediaFile != null) {
       imageView.loadFromURl(mediaFile.mediaURL)
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

@BindingAdapter("dateFromUnix")
fun getDateFromUnix(textView: TextView, unixTime: Long) {
    if (unixTime > 0) {
        val compareDate = SimpleDateFormat("yyyy-MM-dd", Locale.UK)

        val nowDate = Date(Calendar.getInstance().timeInMillis)
        val messageDate = Date(unixTime)

        if (compareDate.format(nowDate) == compareDate.format(messageDate)) {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.UK);
            textView.text = dateFormat.format(messageDate)
        } else {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            textView.text = dateFormat.format(messageDate)
        }
    }
}