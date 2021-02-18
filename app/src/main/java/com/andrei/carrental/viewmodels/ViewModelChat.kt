package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.carrental.entities.Message
import com.andrei.carrental.helpers.ConsumeLiveData
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.ChatRepository
import com.stfalcon.chatkit.messages.MessagesListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelChat @Inject constructor(
        private val chatRepository: ChatRepository
) : ViewModel(

) {


   val userChats:LiveData<State<List<ChatDTO>>> by lazy {
       chatRepository.userChats
   }

   private val currentOpenedChat:MutableLiveData<Long> by lazy {
       MutableLiveData()
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

   private  val _messageToUnsend:MutableLiveData<Message> by lazy {
        MutableLiveData()
    }
    fun setMessageToUnsend(message:Message){

        _messageToUnsend.value = message
    }

    val messagesToUnsendState:LiveData<State<Message>> = Transformations.switchMap(_messageToUnsend){
         chatRepository.unsendMessage(it)
    }
}