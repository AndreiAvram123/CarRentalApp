package com.andrei.engine

import java.lang.Exception

sealed class State<out T > {

    data class Success<out T >(val data: T?) : State<T>()
    object Loading : State<Nothing>()
    data class Error(val exception: Exception) : State<Nothing>()
}