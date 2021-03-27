package com.andrei.engine.repository.interfaces

import androidx.lifecycle.MutableLiveData
import com.andrei.engine.State
import com.andrei.engine.requestModels.RegisterRequest
import com.andrei.engine.states.RegistrationFlowState
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {
     fun validateUsername(username:String) : Flow<State<UsernameValidationState>>
    fun validatePassword(password:String): PasswordValidationState
    suspend fun register(username: String, email: String, password: String)
    val registrationState: MutableLiveData<RegistrationFlowState>
    suspend fun validateEmail(email: String): EmailValidationState
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
}
sealed class EmailValidationState{
   object Valid: EmailValidationState()
   object AlreadyTaken : EmailValidationState()
   object InvalidFormat: EmailValidationState()
}
