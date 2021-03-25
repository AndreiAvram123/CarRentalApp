package com.andrei.messenger

import android.content.Context
import com.andrei.DI.annotations.DefaultGlobalScope
import com.andrei.UI.helpers.InternetConnectionHandler
import com.andrei.carrental.R
import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.ObservableChat
import com.andrei.carrental.entities.User
import com.andrei.carrental.room.dao.ChatDao
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.DTOEntities.Chat
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.utils.defaultSharedFlow
import com.pusher.client.PusherOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class MessengerService @Inject constructor (
         private val pusherConfig: PusherConfig,
         private val messagesDao: MessageDao,
         private val chatDao: ChatDao,
         @DefaultGlobalScope private val coroutineScope: CoroutineScope,
         private val internetConnectionHandler: InternetConnectionHandler
        ){


    private val channels : MutableMap<Long,ChannelService> = mutableMapOf()

    init {
        coroutineScope.launch {
            chatDao.findAllChatsDistinct().collect {
                disconnect()
                configureChannels(it)
                connect()
            }
        }
        internetConnectionHandler.onUnavailable {
            disconnect()
        }
        internetConnectionHandler.onAvailable {
            connect()
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

    suspend fun getObservableChats():List<ObservableChat> = channels.map {
           val channel = it.value
            ObservableChat(
                    chat =  chatDao.findChat(channel.chatID)!!,
                    isUserOnline = channel.isUserOnline,
                    lastMessage = getLastMessageFlow(channel.chatID)
            )
        }

     fun getLastMessageFlow(chatID:Long): SharedFlow<Message> =
             messagesDao.findLastChatMessageDistinct(chatID).defaultSharedFlow(coroutineScope)

     fun getUnsentMessageFlow(chatID:Long):SharedFlow<Message> =
             messagesDao.findLastUnsentChatMessageDistinct(chatID).defaultSharedFlow(coroutineScope)


    suspend fun getChat(chatID: Long):Chat? = chatDao.findChat(chatID)


    fun getUserOnlineFlow(chatID: Long):StateFlow<Boolean>{
        val channel = channels[chatID]
        channel?.let{
            return it.isUserOnline.asStateFlow()
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