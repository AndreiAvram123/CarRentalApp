package com.andrei.engine

import com.andrei.engine.DTOEntities.ApiResult
import com.google.gson.Gson
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

    private fun  convertJsonErrorBodyToApiResult(body:String):ApiResult<*>{
        val gson = Gson()
        return gson.fromJson(body,ApiResult::class.java)
    }



}