package com.andrei.engine.utils

import com.andrei.engine.State

@Suppress("UNCHECKED_CAST")
fun <T,R> State<T>.mapState(transformation: (data:T) -> R): State<R> {
    return when(this){
        is State.Success ->  State.Success(transformation(this.data))
        is State.Loading ->  State.Loading
        is State.Error -> State.Error(this.error)
        is State.Default -> State.Default
    }
}