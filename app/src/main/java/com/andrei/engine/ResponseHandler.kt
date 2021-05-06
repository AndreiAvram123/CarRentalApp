package com.andrei.engine

import android.util.Log
import timber.log.Timber

object ResponseHandler {

    fun <T > handleSuccess(data: T): State<T> {
        return State.Success(data)
    }

    fun <T> handleResponseError(error:String):State<T>{
        return State.Error(error)
    }

    fun <T > handleResponseException(exception:Exception, url: String) :State<T>{
        Timber.e("Error with request $url")
        logException(exception)
        return State.Error("Unknown Error")
    }

    fun <T> handleNoInternetError():State<T>{
        logException(Exception("No internet "))
        return State.Error("No internet")
    }

    private fun logException(e:Exception){
       Timber.e(e)
    }
}