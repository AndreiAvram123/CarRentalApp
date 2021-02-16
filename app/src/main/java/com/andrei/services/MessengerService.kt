package com.andrei.services

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.R
import com.andrei.carrental.entities.Chat
import com.andrei.carrental.entities.User
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.DTOEntities.MessageDTO
import com.pusher.client.PusherOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MessengerService @Inject constructor (
        @ApplicationContext private val context: Context,
        private val pusherOptions: PusherOptions,
        private val messagesDao: MessageDao
        ){


    private val channels : MutableMap<Long,ChannelService> = mutableMapOf()

    fun setChannelsIds(channelIDs:List<Long>){
          channels.clear()
          channels.putAll(channelIDs.map { it to ChannelService(it,
                   pusherOptions,
                   context.getString(R.string.pusher_key),
                   messagesDao)
               }.toMap())
    }

    fun getObservableChats():List<Chat>{
        val chats = mutableListOf<Chat>()
        channels.forEach {
            val chat = Chat(
                   friend = User(id = 3,username = "andrei"),
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