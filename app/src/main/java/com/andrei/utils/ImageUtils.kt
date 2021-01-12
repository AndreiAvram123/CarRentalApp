package com.andrei.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

 fun  Any.fetchBitmap(context: Context , url:String, completion:(bitmap:Bitmap) -> Unit ){
    Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                  completion(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
}