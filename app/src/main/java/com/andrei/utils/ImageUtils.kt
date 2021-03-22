package com.andrei.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import java.io.ByteArrayOutputStream
import java.io.InputStream

suspend fun fetchBitmap(context: Context, url: String, maxWidth: Int = 400):Bitmap?{
    val imageLoader = context.imageLoader
    val imageRequest = ImageRequest.Builder(context).data(url).allowHardware(false).build()

    try {
        val result = imageLoader.execute(imageRequest)
        result.drawable?.let {
            val aspectRatio = getHeightWidthAspectRation(
                    width = it.intrinsicWidth,
                    height = it.intrinsicHeight
            )
            val newHeight = (maxWidth * aspectRatio).toInt()
            return it.toBitmap(width = maxWidth, height = newHeight)
        }
    }catch (e:Exception){
        return null
    }
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

private fun getHeightWidthAspectRation(width:Int,height:Int): Double = height/width.toDouble()