package com.andrei.engine.helpers

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.R
import com.andrei.utils.edit
import com.andrei.utils.getStringOrNull
import com.andrei.utils.removeValue
import com.auth0.android.jwt.JWT
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TokenManager @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        @ApplicationContext private val  context: Context) {


    private val handler:Handler by lazy{
        Handler(Looper.getMainLooper())
    }
    private val invalidationRunnable = Runnable {
        clearToken()
    }

    private val _userToken:MutableLiveData<TokenState> by lazy {
        MutableLiveData<TokenState>().also {
            checkTokenForUser()
        }
    }

    val userToken:LiveData<TokenState>
    get() = _userToken

    private fun checkTokenForUser(){
        //make sure to use post value on a background thread
        GlobalScope.launch(Dispatchers.IO) {
            val token =  sharedPreferences.getStringOrNull(context.getString(R.string.key_token))
            when{
                token == null -> _userToken.postValue(TokenState.Invalid)
                isTokenValid(token) -> useToken(token)
                else -> _userToken.postValue(TokenState.Invalid)
            }
        }
    }

    private fun useToken(token:String){
        _userToken.postValue(TokenState.Valid)
        logUserOutWhenTokenExpires(token)
    }

    fun clearToken(){
        removeTokenFromPersistence()
        _userToken.postValue(TokenState.Invalid)
        stopInvalidationTimer()
    }

    private fun logUserOutWhenTokenExpires(token: String) {
        val parsedJWT = JWT(token)
        val expireDate = parsedJWT.expiresAt
        expireDate?.let {
            val invalidationTime = (it.time - Date().time) / 1000
            handler.postDelayed(invalidationRunnable,invalidationTime)
        }
    }

    private fun stopInvalidationTimer(){
        handler.removeCallbacks(invalidationRunnable)
    }


    private fun removeTokenFromPersistence(){
        sharedPreferences.removeValue(context.getString(R.string.key_token))
    }

    fun setNewToken(token:String){
        saveToken(token)
        _userToken.postValue(TokenState.Valid)
    }

    private fun saveToken(token: String) {
        sharedPreferences.edit{
            putString(context.getString(R.string.key_token),token)
        }
    }

    private fun isTokenValid(token :String):Boolean{
        val parsed = JWT(token)
        return !parsed.isExpired(5)
    }

}

sealed class TokenState{
    object Invalid : TokenState()
    object Valid : TokenState()
}