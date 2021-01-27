package com.andrei.engine.repository.implementation

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.UserAccount
import com.andrei.engine.State
import com.andrei.engine.helpers.TokenManager
import com.andrei.engine.helpers.TokenState
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

    override val isUserLoggedIn: MediatorLiveData<Boolean>  by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(userAccountDetails){
                if(it!=null && tokenManager.userToken.value is TokenState.Valid ){
                    value = true
                }
            }
            addSource(tokenManager.userToken){
                if(it is TokenState.Valid && userAccountDetails.value != null){
                    value = true
                }
            }
        }
    }

    override val userAccountDetails: MutableLiveData<UserAccount> by lazy {
        userAccountManager.userAccountDetails
    }

    override val loginState: MutableLiveData<LoginFlowState> by lazy {
        MutableLiveData()
    }

    override suspend fun startLoginFlow(email: String, password: String) {
         //fetch user

    }


}