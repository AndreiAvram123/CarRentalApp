package com.andrei.engine.repository.implementation

import androidx.lifecycle.MutableLiveData
import com.andrei.engine.CallRunner
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.EmailValidationState
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.SignUpRepository
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.engine.APIs.SignUpAPI
import com.andrei.engine.requestModels.RegisterRequest
import com.andrei.engine.states.RegistrationFlowState
import com.andrei.engine.utils.mapState
import kotlinx.coroutines.flow.*
import javax.inject.Inject



class SignUpRepositoryImpl @Inject constructor(
        private val callRunner: CallRunner,
        private val signUpAPI: SignUpAPI
) : SignUpRepository {


    override val registrationState:MutableLiveData<RegistrationFlowState> by lazy {
        MutableLiveData()
    }


    override fun validateUsername(username: String): Flow<State<UsernameValidationState>> =
            callRunner.makeApiCall { signUpAPI.checkIfUsernameIsAvailable(username) }
                .transform {
                    emit(it.mapState {data ->
                        when {
                            data.valid ->UsernameValidationState.Valid
                            data.reason == RegistrationFlowState.RegistrationError.usernameAlreadyTaken ->
                                UsernameValidationState.AlreadyTaken

                            else -> UsernameValidationState.TooShort
                        }
                    })
                }

        override suspend fun validateEmail(email:String): Flow<State<EmailValidationState>> =
                callRunner.makeApiCall{signUpAPI.checkIfEmailIsAvailable(email)}.transform{state->
                emit(state.mapState {
                    when{
                        it.valid -> EmailValidationState.Valid
                        it.reason == RegistrationFlowState.RegistrationError.emailAlreadyTaken -> EmailValidationState.AlreadyTaken
                        else -> EmailValidationState.InvalidFormat
                    }
                })
            }


        override fun validatePassword(password: String):Flow<State<PasswordValidationState>> = flow{
             val passwordValidation = if(password.length > 6){
                 PasswordValidationState.Valid
             }else{
                 PasswordValidationState.TooWeak
             }

              emit(State.Success(passwordValidation))
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

