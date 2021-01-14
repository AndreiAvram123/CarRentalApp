package com.andrei.engine

import android.util.Log
import com.andrei.carrental.BuildConfig

class ResponseHandler private constructor(){
    //  private val firebaseCrashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

      companion object {
         @JvmStatic
         fun getInstance() = ResponseHandler()
      }


    private val TAG = ResponseHandler::class.java.simpleName

    fun <T : Any> handleSuccess(data: T?): State<T> {
        return State.Success(data)
    }

    fun <T > handleRequestException(exceptions: List<Exception>, string: String): State<T> {
        Log.e(TAG,"Error with request $string")
        exceptions.forEach { logException(it) }
        return State.Error(exceptions.first())
    }

    private fun logException(e:Exception){
        e.printStackTrace()
        if(!BuildConfig.DEBUG){
        //   firebaseCrashlytics.recordException(e)
        }
    }
}