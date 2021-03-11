package com.andrei.UI

sealed class FieldValidation{
    object Unvalidated:FieldValidation()
    object Valid:FieldValidation()
    data class Invalid(val error:String):FieldValidation()
}
