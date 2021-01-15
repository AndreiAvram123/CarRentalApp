package com.andrei.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

fun fetchBitmap(context: Context, url: String, maxWidth: Int = 400, completion: (bitmap: Bitmap) -> Unit){
    Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    val aspectRatio = getHeightWidthAspectRation(width = resource.width, height = resource.height)
                    val newHeight  = (maxWidth * aspectRatio).toInt()

                     completion(Bitmap.createScaledBitmap(resource,maxWidth,newHeight,true))
                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })

}

fun ImageView.loadFromURl(url:String){
    Glide.with(this).load(url).into(this)
}

fun getHeightWidthAspectRation(width:Int,height:Int): Double = height/width.toDouble()