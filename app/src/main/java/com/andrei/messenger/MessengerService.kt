package com.andrei.messenger

import com.andrei.DI.annotations.DefaultGlobalScope
import com.andrei.UI.helpers.InternetConnectionHandler
import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.ObservableChat
import com.andrei.carrental.room.dao.ChatDao
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.DTOEntities.Chat
import com.andrei.engine.helpers.SessionManager
import com.andrei.utils.defaultSharedFlow
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class MessengerService @Inject constructor (
         private val pusherConfig: PusherConfig,
         private val messagesDao: MessageDao,
         private val chatDao: ChatDao,
         @DefaultGlobalScope private val coroutineScope: CoroutineScope,
         private val internetConnectionHandler: InternetConnectionHandler,
         private val sessionManager: SessionManager
        ){


    private val channels : MutableMap<Long,ChannelService> = mutableMapOf()

    private val _observableChats:MutableStateFlow<List<ObservableChat>> = MutableStateFlow(emptyList())
    val observableChats:StateFlow<List<ObservableChat>>
    get() = _observableChats.asStateFlow()

    init {
        coroutineScope.launch {
            chatDao.findAllChatsDistinct().collect {
                if (internetConnectionHandler.isConnected()) {
                    disconnect()
                    configureChannels(it)
                    getObservableChats()
                    connect()
                }
            }
        }
        coroutineScope.launch {
            internetConnectionHandler.isConnectedState.collect { connected ->
                if (connected) {
                    connect()
                } else {
                    disconnect()
                }
            }
        }
        coroutineScope.launch {
            sessionManager.authenticationState.collect {
                if(it == SessionManager.AuthenticationState.NOT_AUTHENTICATED){
                    messagesDao.clean()
                    disconnect()
                }
            }
        }
    }


    private fun configureChannels(chats:List<Chat>){
          channels.clear()
          channels.putAll(chats.map { it.id to ChannelService(it.id,
                   pusherConfig,
                   messagesDao,
                   coroutineScope
          ) }.toMap())
    }

    private fun getObservableChats(){
        coroutineScope.launch {
            val observableChats = mutableListOf<ObservableChat>()
            channels.forEach {
                val channel = it.value
                observableChats.add(ObservableChat(
                        chat = chatDao.findChat(channel.chatID)!!,
                        isUserOnline = channel.isUserOnline,
                        lastMessage = messagesDao.findLastChatMessage(channel.chatID).stateIn(coroutineScope)
                ))
            }
            _observableChats.emit(observableChats)
        }
    }

     fun getLastMessageSharedFlow(chatID:Long): SharedFlow<Message> =
             messagesDao.findLastNotNullChatMessage(chatID).defaultSharedFlow(coroutineScope)

     fun getUnsentMessageSharedFlow(chatID:Long):SharedFlow<Message> =
             messagesDao.findLastUnsentChatMessageDistinct(chatID).defaultSharedFlow(coroutineScope)



    suspend fun getChat(chatID: Long):Chat? = chatDao.findChat(chatID)


    fun getUserOnlineFlow(chatID: Long):StateFlow<Boolean>{
          channels[chatID]?.let {
            return it.isUserOnline
        }
        return MutableStateFlow(false).asStateFlow()
    }


    private fun connect(){
      channels.forEach{
          it.value.connect()
      }
    }
    private fun disconnect(){
        channels.forEach{
            it.value.disconnect()
        }
    }
}