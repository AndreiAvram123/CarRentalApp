package com.andrei.engine.helpers

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.andrei.carrental.R
import com.andrei.utils.getStringOrNull
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        @ApplicationContext private val  context: Context) {


    private val mUserToken:MutableLiveData<TokenState> by lazy {
        MutableLiveData<TokenState>().also {
            getTokenForUser()
        }
    }

    val userToken:LiveData<TokenState> = Transformations.map(mUserToken) {
        it
    }

    private fun getTokenForUser(){
       val token =  sharedPreferences.getStringOrNull(context.getString(R.string.key_token))

        when{
            token == null -> mUserToken.postValue(TokenState.Invalid)
            isTokenValid(token) -> mUserToken.postValue(TokenState.Valid)
            else -> mUserToken.postValue(TokenState.Invalid)
        }
    }


   private fun isTokenValid(token:String):Boolean{
        return true

    }
}

sealed class TokenState{
    object Invalid : TokenState()
    object Valid : TokenState()
}