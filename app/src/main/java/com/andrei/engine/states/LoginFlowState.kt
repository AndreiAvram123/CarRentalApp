package com.andrei.engine.states


sealed class LoginFlowState{
    object Loading: LoginFlowState()

    open class LoginError(val error:String)  : LoginFlowState() {
        object IncorrectPassword :LoginError(errorInvalidPassword)
        object IncorrectEmail:LoginError(errorInvalidEmail)
        object ConnectionError:LoginError(connectionError)

        companion object ErrorMessages {
            const val errorInvalidEmail = "This email does not exist in our system"
            const val errorInvalidPassword = "The password you entered is incorrect"
            const val connectionError = "Unknown error"
        }
    }
}
