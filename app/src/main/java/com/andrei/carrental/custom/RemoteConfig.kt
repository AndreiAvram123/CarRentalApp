package com.andrei.carrental.custom

import com.andrei.engine.configuration.APIResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import timber.log.Timber

object RemoteConfig {

    private val  api:RemoteConfigAPI =
            Retrofit.Builder().baseUrl("https://car-rental-api-kotlin.herokuapp.com")
                    .addConverterFactory(GsonConverterFactory.create()).build().create(RemoteConfigAPI::class.java)

    private val data:MutableMap<String,Any> = mutableMapOf()


    fun start(){
        GlobalScope.launch(Dispatchers.IO) {
            try {
               val response =  api.getRemoteConfig()
                data.putAll(response.data)
            }catch (e:Exception){
                Timber.e(e)
            }
        }
    }

    fun getString(key:String):String{
        data[key]?.let { if (it is String) return it }
        return "Unknown"
    }



}
interface RemoteConfigAPI{
    @GET("/remoteConfig")
     suspend fun getRemoteConfig():APIResponse<Map<String,String>>
}