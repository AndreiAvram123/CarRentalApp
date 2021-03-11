package com.andrei.utils

import android.util.Patterns

fun String.isEmailValid():Boolean{
    val regex = Patterns.EMAIL_ADDRESS.toRegex()
    return this.matches(regex)
}

fun String.isEmailInvalid():Boolean = !isEmailValid()
 fun String.isUsernameInvalid():Boolean{
    return length < 5
}

fun String.isPasswordTooWeak():Boolean{
    return this.length < 7
}
