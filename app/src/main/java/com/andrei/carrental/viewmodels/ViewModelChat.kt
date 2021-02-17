package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.carrental.entities.Message
import com.andrei.carrental.helpers.ConsumeLiveData
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

   val lastChatMessage:LiveData<Message> = Transformations.switchMap(currentOpenedChat){
       chatRepository.getLastChatMessage(it)
   }

   private val _enteredMessageText:MediatorLiveData<String> by lazy {
       MediatorLiveData<String>().apply {
           addSource(currentOpenedChat){
              if(it !=null){
                  value = ""
              }
           }
       }
   }


   val enteredMessageText:LiveData<String>
   get() = _enteredMessageText



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


    fun setMessageText(messageText:String){
        _enteredMessageText.value = messageText
    }
}