package com.andrei.engine.helpers

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.andrei.carrental.R
import com.andrei.utils.getStringOrNull
import com.auth0.android.jwt.JWT
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject

@ActivityScoped
class TokenManager @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        @ApplicationContext private val  context: Context) {


    private val mUserToken:MutableLiveData<TokenState> by lazy {
        MutableLiveData<TokenState>().also {
            checkTokenForUser()
        }
    }

    val userToken:LiveData<TokenState> = Transformations.map(mUserToken) {
        it
    }

    private fun checkTokenForUser(){
       val token =  sharedPreferences.getStringOrNull(context.getString(R.string.key_token))

        when{
            token == null -> mUserToken.postValue(TokenState.Invalid)
            isTokenValid(token) -> mUserToken.postValue(TokenState.Valid)
            else -> mUserToken.postValue(TokenState.Invalid)
        }
    }

    fun recheckToken(){
       checkTokenForUser()
    }


   private fun isTokenValid(token :String):Boolean{
       val parsed = JWT(token)
        parsed.expiresAt?.let{
            return it.before(Date())
        }
        return false

    }
}

sealed class TokenState{
    object Invalid : TokenState()
    object Valid : TokenState()
}