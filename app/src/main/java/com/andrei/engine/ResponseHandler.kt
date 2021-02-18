package com.andrei.engine

import android.util.Log

object ResponseHandler {

    private val TAG = ResponseHandler::class.java.simpleName


    fun <T : Any> handleSuccess(data: T?): State<T> {
        return State.Success(data)
    }

    fun <T> handleResponseError(error:String):State<T>{
        return State.Error(error)
    }

    fun <T > handleResponseException(exception:Exception, url: String) :State<T>{
        Log.e(TAG,"Error with request $url")
        logException(exception)
        return State.Error("Unknown Error")
    }

    fun <T> handleNoInternetError():State<T>{
        logException(Exception("No internet "))
        return State.Error("No internet")
    }

    private fun logException(e:Exception){
       Log.e(ResponseHandler::class.simpleName,e.stackTraceToString())
    }
}