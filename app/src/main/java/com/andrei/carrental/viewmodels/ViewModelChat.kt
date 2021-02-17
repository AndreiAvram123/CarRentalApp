package com.andrei.carrental.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.andrei.carrental.entities.Message
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

}