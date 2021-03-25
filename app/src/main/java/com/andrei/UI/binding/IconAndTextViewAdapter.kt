package com.andrei.UI.binding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.load
import com.andrei.UI.customViews.IconAndTextView
import com.andrei.carrental.entities.MediaFile


@BindingAdapter("iconText")
fun bindText(iconAndTextView: IconAndTextView,
                     text: String?) {

    text?.let {
        iconAndTextView.setIconText(it)
    }
}