package com.andrei.engine.states


sealed class RegistrationFlowState{
    object Finished:RegistrationFlowState()
    object Loading:RegistrationFlowState()

    sealed class RegistrationError(val error:String)  : RegistrationFlowState() {
        object UnknownError:RegistrationError(unknownError)
        object PasswordTooWeak:RegistrationError(passwordTooWeak)
        object UsernameAlreadyTaken:RegistrationError(usernameAlreadyTaken)
        object EmailAlreadyTaken: RegistrationError(emailAlreadyTaken)


        companion object{
            const val unknownError = "Unknown error"
            const val passwordTooWeak = "Password too weak"
            const val usernameAlreadyTaken = "Username already taken"
            const val emailAlreadyTaken = "Email already taken"

            fun mapError(error: String):RegistrationError{
                when(error){
                    unknownError -> return RegistrationError.UnknownError
                    else -> return RegistrationError.UnknownError
                }
            }
        }
    }
}
