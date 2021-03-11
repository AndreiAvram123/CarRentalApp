package com.andrei.engine.helpers

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.andrei.DI.annotations.DefaultGlobalScope
import com.andrei.carrental.R
import com.andrei.utils.edit
import com.andrei.utils.getStringOrNull
import com.andrei.utils.removeValue
import com.auth0.android.jwt.JWT
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TokenManager @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        @ApplicationContext private val  context: Context,
        @DefaultGlobalScope private val coroutineScope: CoroutineScope
        ) {

    private val handler:Handler by lazy{
        Handler(Looper.getMainLooper())
    }
    private val invalidationRunnable = Runnable {
        clearToken()
    }

    private var currentToken:String? =  sharedPreferences.getStringOrNull(context.getString(R.string.key_token))

    private val _tokenState:MutableStateFlow<TokenState> = MutableStateFlow(
            if(isTokenValid(currentToken)){
                TokenState.Valid
            }else{
                TokenState.Invalid
            }
    )
    val tokenState:StateFlow<TokenState>
    get() = _tokenState


    fun isCurrentTokenValid():Boolean = isTokenValid(currentToken)

    private fun isTokenValid(token:String?):Boolean{
        return token?.isTokenExpired() ?: false
    }

    init {
        if(isTokenValid(currentToken)){
            logUserOutWhenCurrentTokenExpires()
        }else{
            removeTokenFromPersistence()
        }
    }



    fun clearToken(){
        removeTokenFromPersistence()
        stopInvalidationTimer()
        coroutineScope.launch {
            _tokenState.emit(TokenState.Invalid)
        }
    }

    private fun logUserOutWhenCurrentTokenExpires() {
        currentToken?.let { token->
            val parsedJWT = JWT(token)
            val expireDate = parsedJWT.expiresAt
            expireDate?.let {
                val invalidationTime = (it.time - Date().time) / 1000
                handler.postDelayed(invalidationRunnable, invalidationTime)
            }
        }
    }

    private fun stopInvalidationTimer(){
        handler.removeCallbacks(invalidationRunnable)
    }


    private fun removeTokenFromPersistence(){
        sharedPreferences.removeValue(context.getString(R.string.key_token))
    }

    fun setNewToken(neToken:String){
        if(isTokenValid(neToken)) {
            saveTokenInStorage(neToken)
            logUserOutWhenCurrentTokenExpires()
            coroutineScope.launch {
                _tokenState.emit(TokenState.Valid)
            }
        }
    }

    private fun saveTokenInStorage(token: String) {
        sharedPreferences.edit{
            putString(context.getString(R.string.key_token),token)
        }
    }


    private fun String.isTokenExpired():Boolean{
        val parsed = JWT(this)
        return !parsed.isExpired(5)
    }

}

sealed class TokenState{
    object Invalid : TokenState()
    object Valid : TokenState()
}