package com.andrei.engine.repository.implementation

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.UserAccount
import com.andrei.engine.State
import com.andrei.engine.helpers.TokenManager
import com.andrei.engine.helpers.UserAccountManager
import com.andrei.engine.repository.interfaces.AuthRepository
import com.andrei.engine.repositoryInterfaces.AuthRepoInterface
import com.andrei.engine.states.LoginFlowState
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
        private val userAccountManager: UserAccountManager,
        private val tokenManager: TokenManager,
        private val authRepo: AuthRepoInterface,
        private val callRunner: CallRunner
): AuthRepository{

    override val userAccountDetails: MutableLiveData<UserAccount> by lazy {
        userAccountManager.userAccountDetails
    }

    val userToken : LiveData<String?> = Transformations.switchMap(userAccountDetails){
        liveData {
            if(it == null){
                emit(null)
                return@liveData
            }
            val token = tokenManager.getTokenForUser()
                if (tokenManager.isTokenValid()) {
                    emit(token)
                } else {
                    val response = fetchNewToken(it)
                    if(response == null){
                        loginState.postValue(LoginFlowState.Error("Unknown"))
                    }
                }
            }
    }


    override val loginState: MutableLiveData<LoginFlowState> by lazy {
        MutableLiveData()
    }

    override suspend fun startLoginFlow(email: String, password: String) {
         //fetch user

    }

    override suspend fun fetchNewToken(userAccount: UserAccount): String?{
        var token:String?  = ""
       callRunner.makeApiCall(authRepo.getToken()){
           if(it is State.Success){
             token = it.data?.token
           }
       }
        return token
    }


}