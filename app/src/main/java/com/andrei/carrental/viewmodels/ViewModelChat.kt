package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.carrental.entities.Message
import com.andrei.carrental.helpers.ConsumeLiveData
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelChat @Inject constructor(
        private val chatRepository: ChatRepository
) : ViewModel(

) {


    private val _userChats:MutableStateFlow<State<List<ChatDTO>>>  = MutableStateFlow(State.Loading)

    val userChats:StateFlow<State<List<ChatDTO>>>
    get() = _userChats

   private val currentOpenedChat:MutableLiveData<Long> by lazy {
       MutableLiveData()
   }


    fun getUserChats(){
        viewModelScope.launch {
            chatRepository.fetchUserChats().collect { state->
                when(state){
                    is State.Success -> _userChats.emit(State.Success(state.data))
                    is State.Loading -> _userChats.emit(State.Loading)
                    is State.Error -> _userChats.emit(State.Error(state.error))
                }
            }
        }
    }


    suspend fun getInitialChatMessages():List<Message>{
        val chatID = currentOpenedChat.value
        if(chatID != null){
           return chatRepository.getInitialChatMessages(chatID)
        }
        return emptyList()

    }

    fun setCurrentOpenedChatID(chatID:Long){
        currentOpenedChat.value = chatID
    }


    fun sendMessage(text:String){
        val currentChatID = currentOpenedChat.value
        if( currentChatID !=null){
             viewModelScope.launch {
                 chatRepository.sendMessage(text,currentChatID)
             }
        }
    }



    fun unsendMessage(message:Message){
       viewModelScope.launch(Dispatchers.IO) {
           chatRepository.unsendMessage(message)
       }
    }

    private val _messageToUnsendState = chatRepository.messageToUnsendState
     val messageToUnsendState:SharedFlow<State<Message>>
     get() = _messageToUnsendState


}