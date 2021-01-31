package com.andrei.utils

import android.content.Context
import android.net.ConnectivityManager


fun Context.getConnectivityManager() = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun ConnectivityManager.isConnected() = this.activeNetwork != null
fun ConnectivityManager.isNotConnected() = !isConnected()