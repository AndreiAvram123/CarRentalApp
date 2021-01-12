package com.andrei.engine

import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.awaitResponse

class CallRunner {

    private val responseHandler = ResponseHandler.getInstance()

    suspend fun <T> makeCall(call: Call<T>,update:suspend (state: State<T>)->Unit){
        update(State.Loading)
        val url = call.request().url.toString()
        try {
            val response = call.awaitResponse()
            if(response.isSuccessful){
                val body = response.body()
                if(body !=null){
                    update(responseHandler.handleSuccess(body))
                }
            }else{
                update( responseHandler.handleRequestException(Exception("Unknown"),url))
            }
        } catch (e: Exception) {
             update(responseHandler.handleRequestException(e,url))
        }
    }




}