package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.engine.DTOEntities.UserAccount
import com.andrei.engine.State
import com.andrei.engine.states.LoginFlowState

interface AuthRepository {
    val userAccountDetails : MutableLiveData<UserAccount>
    val loginState : MutableLiveData<LoginFlowState>

    suspend fun startLoginFlow(email:String,password:String)
    suspend fun fetchNewToken(userAccount: UserAccount): String?

}

