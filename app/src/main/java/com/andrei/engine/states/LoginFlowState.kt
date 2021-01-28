package com.andrei.engine.states


sealed class LoginFlowState{
    object LoggedIn:LoginFlowState()
    object Loading: LoginFlowState()
    data class Error(val error:String) : LoginError()
}

open class LoginError : LoginFlowState() {
    object IncorrectPassword :LoginError()
    object IncorrectEmail:LoginError()
    object Unknown :LoginError()

    companion object ErrorMessages {
        const val errorInvalidEmail = "This email does not exist in our system"
        const val errorInvalidPassword = "The password you entered is incorrect"

    }
}
