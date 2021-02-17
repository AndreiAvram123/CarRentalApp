package com.andrei.services

import android.content.Context
import com.andrei.carrental.R
import com.andrei.carrental.entities.ObservableChat
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.DTOEntities.toUser
import com.pusher.client.PusherOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MessengerService @Inject constructor (
        @ApplicationContext private val context: Context,
        private val pusherOptions: PusherOptions,
        private val messagesDao: MessageDao
        ){


    private val channels : MutableMap<Long,ChannelService> = mutableMapOf()


    fun configureChannels(chats:List<ChatDTO>){
          channels.clear()
          channels.putAll(chats.map { it.id to ChannelService(it.id,
                   pusherOptions,
                   context.getString(R.string.pusher_key),
                   messagesDao,
                   it.friend.toUser()
          )
               }.toMap())
    }

    fun getObservableChats():List<ObservableChat>{
        val chats = mutableListOf<ObservableChat>()
        channels.forEach {
            val chat = ObservableChat(
                    id = it.value.chatID,
                    friend = it.value.friend,
                    isUserOnline = it.value.isUserOnline,
                    lastMessageDTO = it.value.lastMessageDTO
            )
            chats.add(chat)
        }
        return chats
    }



    fun connect(){
      channels.forEach{
          it.value.connect()
      }
    }
}