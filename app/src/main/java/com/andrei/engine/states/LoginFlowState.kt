package com.andrei.engine.states

sealed class LoginFlowState{
    object LoggedIn:LoginFlowState()
    object Loading: LoginFlowState()
    data class Error(val error:String) : LoginFlowState()
}