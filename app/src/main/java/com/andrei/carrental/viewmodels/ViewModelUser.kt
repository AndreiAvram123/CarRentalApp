package com.andrei.carrental.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei.carrental.entities.User
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelUser @Inject constructor(
    private val userRepository: UserRepository
):ViewModel() {

    private val _currentUser:MutableStateFlow<State<User>> = MutableStateFlow(State.Loading)
    val currentUser:StateFlow<State<User>>
    get() = _currentUser.asStateFlow()



    fun getUser(userID:Long){
        viewModelScope.launch {
            userRepository.getUser(userID).collect {
                _currentUser.emit(it)
            }
        }
    }




}