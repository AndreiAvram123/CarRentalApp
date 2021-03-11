package com.andrei.carrental.viewmodels

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.MessageType
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.ChatRepository
import com.andrei.utils.toBase64
import com.andrei.utils.toDrawable
import com.andreia.carrental.requestModels.CreateMessageRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.aprilapps.easyphotopicker.MediaFile
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ViewModelChat @Inject constructor(
        private val chatRepository: ChatRepository,
        app:Application
) : AndroidViewModel(app){


    private val _userChats:MutableStateFlow<State<List<ChatDTO>>>  = MutableStateFlow(State.Loading)

    val userChats:StateFlow<State<List<ChatDTO>>>
        get() = _userChats

    private val currentOpenedChat:MutableLiveData<Long> by lazy {
        MutableLiveData()
    }

    private val _messageToUnsendState = chatRepository.messageToUnsendState
    val messageToUnsendState:SharedFlow<State<Message>>
        get() = _messageToUnsendState


    private val _messageToSendState = chatRepository.messageToSendState




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

    private val messagesToSend = LinkedList<CreateMessageRequest>()

    private val currentMessageToSend:MutableStateFlow<CreateMessageRequest?> = MutableStateFlow(null)


    init {
        viewModelScope.launch {
            _messageToSendState.collect {
                if(it is State.Success){
                    dequeueMessageToSend()
                }
            }
        }
        viewModelScope.launch {
            currentMessageToSend.filterNotNull().collect {
                chatRepository.sendMessage(it)
            }
        }
    }

    private fun dequeueMessageToSend(){
        if(currentMessageToSend.value == null){
            val message = messagesToSend.peek()
            viewModelScope.launch(Dispatchers.IO) {   currentMessageToSend.emit(message)}
            if(message != null){
                messagesToSend.pop()
            }
        }
    }


    fun sendMessage(createMessageRequest: CreateMessageRequest){
        messagesToSend.add(createMessageRequest)
        dequeueMessageToSend()
    }




    fun sendImages(images:List<MediaFile>, chatID:Long, userID:Long){
        viewModelScope.launch(Dispatchers.IO) {
            images.forEach {
                val drawable = it.file.toUri().toDrawable(getApplication())
                val base64Image = drawable.toBase64()
                val createMessageModel = CreateMessageRequest(
                        senderID = userID,
                        chatID = chatID,
                        messageType = MessageType.MESSAGE_IMAGE,
                        mediaData = base64Image
                )
                sendMessage(createMessageModel)
            }
        }
    }


    fun unsendMessage(message:Message){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.unsendMessage(message)
        }
    }


}