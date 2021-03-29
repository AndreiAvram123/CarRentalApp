package com.andrei.engine.repository.interfaces

import androidx.lifecycle.MutableLiveData
import com.andrei.engine.State
import com.andrei.engine.states.RegistrationResponse
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {
     fun validateUsername(username:String) : Flow<State<UsernameValidationState>>
    fun validatePassword(password:String): Flow<State<PasswordValidationState>>
     fun register(username: String, email: String, password: String, base64ProfilePicture:String): Flow<RegistrationResponse>
    val registrationState: MutableLiveData<RegistrationResponse>
    suspend fun validateEmail(email: String): Flow<State<EmailValidationState>>
}
sealed class UsernameValidationState{
    object Unvalidated:UsernameValidationState()
    object Valid : UsernameValidationState()
    object AlreadyTaken: UsernameValidationState()
    object  TooShort:UsernameValidationState()

}

sealed class PasswordValidationState{
    object Valid:PasswordValidationState()
    object TooWeak:PasswordValidationState()
    object Unvalidated:PasswordValidationState()
}
sealed class EmailValidationState{
   object Valid: EmailValidationState()
   object AlreadyTaken : EmailValidationState()
   object InvalidFormat: EmailValidationState()
    object Unvalidated:EmailValidationState()
}
