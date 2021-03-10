package com.andrei.engine.repository.implementation

import androidx.lifecycle.MutableLiveData
import com.andrei.engine.CallRunner
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.EmailValidationState
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.SignUpRepository
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.engine.repositoryInterfaces.SignUpAPI
import com.andrei.engine.requestModels.RegisterRequest
import com.andrei.engine.states.RegistrationFlowState
import com.andrei.utils.isEmailValid
import com.andrei.utils.isPasswordTooWeak
import com.andrei.utils.isUsernameInvalid
import kotlinx.coroutines.flow.collect
import javax.inject.Inject



class SignUpRepositoryImpl @Inject constructor(
        private val callRunner: CallRunner,
        private val signUpAPI: SignUpAPI
) : SignUpRepository {


    override val registrationState:MutableLiveData<RegistrationFlowState> by lazy {
        MutableLiveData()
    }


    override suspend fun validateUsername(username: String): UsernameValidationState  {
        var result:UsernameValidationState = UsernameValidationState.TooShort

        if(username.isUsernameInvalid()){
            return result
        }

        callRunner.makeApiCall{signUpAPI.checkIfUsernameIsAvailable(username)}.collect {
            if (it is State.Success) {
                if (it.data != null) {
                    result = when {
                        it.data.valid -> UsernameValidationState.Valid
                        it.data.reason == RegistrationFlowState.RegistrationError.usernameAlreadyTaken ->
                            UsernameValidationState.AlreadyTaken

                        else -> UsernameValidationState.TooShort
                    }
                }
            }
        }
        return result
    }

    override suspend fun validateEmail(email:String): EmailValidationState{
        var result:EmailValidationState = EmailValidationState.InvalidFormat

        if(!email.isEmailValid()) {
            return result
        }
        callRunner.makeApiCall{signUpAPI.checkIfEmailIsAvailable(email)}.collect{
            if(it is State.Success && it.data != null){
                result = when{
                    it.data.valid -> EmailValidationState.Valid
                    it.data.reason == RegistrationFlowState.RegistrationError.emailAlreadyTaken -> EmailValidationState.AlreadyTaken
                    else -> EmailValidationState.InvalidFormat
                }
            }
        }
        return result
    }

    override fun validatePassword(password: String):PasswordValidationState{
        if(password.isPasswordTooWeak()){
            return PasswordValidationState.TooWeak
        }
        return PasswordValidationState.Valid
    }



    override suspend fun register(username:String, email:String, password: String) {
        val registerRequest = RegisterRequest(
                username = username,
                email = email,
                password = password
        )
         callRunner.makeApiCall{signUpAPI.register(registerRequest)}.collect{
             when(it){
                 is State.Success -> registrationState.postValue(RegistrationFlowState.Finished)
                 is State.Loading -> registrationState.postValue(RegistrationFlowState.Loading)
                 is State.Error -> registrationState.postValue(RegistrationFlowState.RegistrationError.mapError(it.error))
             }
         }
    }

}

