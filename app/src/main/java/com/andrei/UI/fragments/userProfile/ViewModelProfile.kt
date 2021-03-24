package com.andrei.UI.fragments.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelProfile @Inject constructor(
        private val chatRepository: ChatRepository
):ViewModel() {

    private val _usersChat: MutableStateFlow<State<ChatDTO>> = MutableStateFlow(State.Loading)

    val usersChat: StateFlow<State<ChatDTO>>
        get() = _usersChat



    fun getUsersChat(user1ID:Long,user2ID:Long){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.fetchUsersChat(user1ID,user2ID).collect { state->
                when(state){
                    is State.Success -> _usersChat.emit(State.Success(state.data))
                    is State.Loading -> _usersChat.emit(State.Loading)
                    is State.Error ->{
                         if(state.error == ChatRepository.chatNotFoundError){
                             TODO("Create chat")
                         }else {
                             _usersChat.emit(State.Error(state.error))
                         }
                    }
                }
            }
        }
    }

}