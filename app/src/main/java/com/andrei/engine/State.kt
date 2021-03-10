package com.andrei.engine



sealed class State<out T > {
    data class Success<out T >(val data: T?) : State<T>()
    object Loading : State<Nothing>()
    data class Error(val error:String) : State<Nothing>()

}

