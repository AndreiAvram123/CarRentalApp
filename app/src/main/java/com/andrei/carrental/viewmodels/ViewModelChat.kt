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


    private val _messageToUnsendState = chatRepository.messageToUnsendState
    val messageToUnsendState:SharedFlow<State<Message>>
        get() = _messageToUnsendState


    private val _messageToSendState = chatRepository.messageToSendState


    private val _oldMessagesLoaded:MutableStateFlow<State<List<Message>>> = MutableStateFlow(State.Default)
    val oldMessagesLoaded:StateFlow<State<List<Message>>>
    get() = _oldMessagesLoaded.asStateFlow()


    fun getUserChats(userID:Long){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.fetchUserChats(userID).collect { state->
                when(state){
                    is State.Success -> _userChats.emit(State.Success(state.data))
                    is State.Loading -> _userChats.emit(State.Loading)
                    is State.Error -> _userChats.emit(State.Error(state.error))
                }
            }
        }
    }

   private val _currentChatMessages:MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
   val currentOpenedChatMessages:StateFlow<List<Message>>
   get() = _currentChatMessages


      fun getChatMessages(chatID: Long){
          //todo
          //should perform a request to fetch the chat messages
            viewModelScope.launch {
                val messages = chatRepository.getInitialChatMessages(chatID)
                _currentChatMessages.emit(messages)
        }

    }


    fun resetOpenedChat(){
        viewModelScope.launch {
            _currentChatMessages.emit(emptyList())
            _oldMessagesLoaded.emit(State.Default)
        }
    }

    private val messagesToSend = LinkedList<CreateMessageRequest>()

    private val currentMessageToSend:MutableStateFlow<CreateMessageRequest?> = MutableStateFlow(null)


    init {
        viewModelScope.launch {
            _messageToSendState.collect {
                if(it is State.Success){
                    dequeueMessage()
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            currentMessageToSend.filterNotNull().collect {
                chatRepository.sendMessage(it)
            }
        }
    }

    private fun dequeueMessage(){
            val message = messagesToSend.peek()
            viewModelScope.launch(Dispatchers.IO) {   currentMessageToSend.emit(message)}
            if(message != null){
                messagesToSend.pop()
            }
        }


    fun sendMessage(createMessageRequest: CreateMessageRequest){
        messagesToSend.add(createMessageRequest)
        if(currentMessageToSend.value == null) {
            dequeueMessage()
        }
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
        viewModelScope.launch {
            chatRepository.unsendMessage(message)
        }
    }

    fun loadMoreMessages(chatID:Long, offset: Int) {
        viewModelScope.launch {
            chatRepository.loadMoreMessages(chatID,offset).collect {
             _oldMessagesLoaded.emit(it)
            }
        }
    }




}