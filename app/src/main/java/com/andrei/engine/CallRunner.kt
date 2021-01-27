package com.andrei.engine

import com.andrei.engine.DTOEntities.ApiResult
import retrofit2.Call
import retrofit2.awaitResponse
import javax.inject.Inject

class CallRunner @Inject constructor(){

    private val responseHandler = ResponseHandler.getInstance()



    suspend fun <T> makeApiCall(call: Call<ApiResult<T>>, update:suspend (state: State<T>)->Unit){
        update(State.Loading)
        val url = call.request().url.toString()
        try {
            val response = call.awaitResponse()
            val body = response.body()
            if(body != null){
                if(response.isSuccessful){
                    update(responseHandler.handleSuccess(body.data))
                }else{
                   body.errors?.let { update( responseHandler.handleRequestException(it.map { stringException ->  Exception(stringException) },url))}
                }
            }
        } catch (e: Exception) {
            update(responseHandler.handleRequestException(listOf(e),url))
        }
    }



}