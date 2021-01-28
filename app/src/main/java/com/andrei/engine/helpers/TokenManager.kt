package com.andrei.engine.helpers

import android.content.Context
import android.content.SharedPreferences
import android.media.session.MediaSession
import android.os.Handler
import android.os.Looper
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
            isTokenValid(token) -> {
                mUserToken.postValue(TokenState.Valid)
                logUserOutWhenTokenExpires(token)
            }
            else -> mUserToken.postValue(TokenState.Invalid)
        }
    }

    private fun logUserOutWhenTokenExpires(token: String) {
        val parsedJWT = JWT(token)
        val expireDate = parsedJWT.expiresAt
        expireDate?.let {
            val logUserAfterSeconds = (it.time - Date().time) / 1000
            Handler(Looper.getMainLooper()).postDelayed({
                mUserToken.postValue(TokenState.Invalid)
            }, logUserAfterSeconds)
        }
    }


    private fun isTokenValid(token :String):Boolean{
       val parsed = JWT(token)
        return parsed.isExpired(5)
    }
}

sealed class TokenState{
    object Invalid : TokenState()
    object Valid : TokenState()
}