package com.andrei.engine

import android.net.ConnectivityManager
import com.andrei.engine.configuration.ApiResult
import com.andrei.engine.configuration.CallWrapper
import com.andrei.utils.isNotConnected
import com.google.gson.Gson
import retrofit2.awaitResponse
import javax.inject.Inject

class CallRunner @Inject constructor(
        private val connectivityManager: ConnectivityManager
){

    private val responseHandler = ResponseHandler.getInstance()



    suspend fun <T> makeApiCall(call: CallWrapper<T>, update:suspend (state: State<T>)->Unit){

        if(connectivityManager.isNotConnected()){
            update(responseHandler.handleNoInternetError())
            return
        }
        update(State.Loading)
        val url = call.request().url.toString()
        try {
            val response = call.awaitResponse()
                if(response.isSuccessful){
                    response.body()?.let{update(responseHandler.handleSuccess(it.data))}

                }else{
                    response.errorBody()?.let {
                        val convertedErrorResponse = convertJsonErrorBodyToApiResult(it.string())
                        if(convertedErrorResponse.error != null){
                            update(responseHandler.handleResponseError(convertedErrorResponse.error))
                        }
                    }
                }
        } catch (e: Exception) {
            update(responseHandler.handleResponseException(e,url))
        }
    }

    private fun  convertJsonErrorBodyToApiResult(body:String): ApiResult<*> {
        val gson = Gson()
        return gson.fromJson(body, ApiResult::class.java)
    }



}