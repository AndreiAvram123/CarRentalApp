package com.andrei.engine.states

sealed class RegistrationFlowState{
    object Finished:RegistrationFlowState()
    object Loading:RegistrationFlowState()
    sealed class RegistrationError(val error:String)  : RegistrationFlowState() {
        object UnknownError:RegistrationError(unknownError)


        companion object{
            const val unknownError = "Unknown error"

            fun mapError(error: String):RegistrationError{
                when(error){
                    unknownError -> return RegistrationError.UnknownError
                    else -> return RegistrationError.UnknownError
                }
            }
        }
    }
}
