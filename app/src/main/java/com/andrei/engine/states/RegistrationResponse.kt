package com.andrei.engine.states


sealed class RegistrationResponse{
    object Complete:RegistrationResponse()
    object Loading:RegistrationResponse()

    sealed class RegistrationError(val error:String)  : RegistrationResponse() {
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
                return when(error){
                    passwordTooWeak-> PasswordTooWeak
                    usernameAlreadyTaken -> UsernameAlreadyTaken
                    emailAlreadyTaken -> EmailAlreadyTaken
                    else -> return UnknownError
                }
            }
        }
    }
}
