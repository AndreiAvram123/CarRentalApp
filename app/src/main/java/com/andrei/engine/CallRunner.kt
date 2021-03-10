package com.andrei.engine

import android.net.ConnectivityManager
import com.andrei.engine.configuration.ApiError
import com.andrei.engine.configuration.ApiResult
import com.andrei.utils.isNotConnected
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject


typealias ServiceCall<DataType> = suspend () -> Response<ApiResult<DataType>>



class CallRunner @Inject constructor(
        private val connectivityManager: ConnectivityManager
){

    private val coroutineDispatcher = Dispatchers.IO


     fun <DataType> makeApiCall(call: ServiceCall<DataType>) = flow<State<DataType>>{

        if(connectivityManager.isNotConnected()){
            emit(ResponseHandler.handleNoInternetError())
        }

        emit(State.Loading)

        try {
             val response = call()
                if(response.isSuccessful){
                    response.body()?.let{emit(ResponseHandler.handleSuccess(it.data))}

                }else{
                    response.errorBody()?.let {
                        val error = convertJsonErrorBodyToApiResult(it.string())
                            emit(ResponseHandler.handleResponseError(error.message))
                    }
                }
        } catch (e: Exception) {
            emit(ResponseHandler.handleResponseException(e,call.toString()))
        }
    }.flowOn(coroutineDispatcher)

    private fun  convertJsonErrorBodyToApiResult(body:String): ApiError {
        val gson = Gson()
        return gson.fromJson(body, ApiError::class.java)
    }
}