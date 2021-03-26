package com.andrei.utils

import android.os.Handler

fun Handler.executeDelayed(callback:Runnable){
    removeCallbacks(callback)
    postDelayed(callback,1200)
}