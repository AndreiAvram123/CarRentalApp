package com.andrei.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import java.io.ByteArrayOutputStream
import java.io.InputStream

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
    Glide.with(this)
            .load(url).override(SIZE_ORIGINAL).into(this)
}

fun Uri.toDrawable(context: Context): Drawable {
    val inputStream: InputStream? = context.contentResolver.openInputStream(this)
    return Drawable.createFromStream(inputStream, path.toString())
}

fun Drawable.toBase64(): String {
    val bitmap = (this as BitmapDrawable).bitmap
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT).replace("\\s".toRegex(), "");
}

fun getHeightWidthAspectRation(width:Int,height:Int): Double = height/width.toDouble()