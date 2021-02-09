package com.andrei.utils

 fun String.isEmailValid():Boolean{
    return this.length >=10

}
 fun String.isUsernameInvalid():Boolean{
    return length < 5
}

fun String.isPasswordTooWeak():Boolean{
    return this.length < 7
}
