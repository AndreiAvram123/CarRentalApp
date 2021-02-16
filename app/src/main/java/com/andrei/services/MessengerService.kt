package com.andrei.services

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andreia.carrental.entities.Message
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions

class MessengerService (usersIDs:List<Long>,
                        private val context: Context,
                        private val pusherOptions: PusherOptions){

    private val channels : Map<Long,ChannelService> = usersIDs.map { it to ChannelService(it,pusherOptions,context) }.toMap()

    fun setUserIDs(){

    }

    fun getUserOnlineLiveData(userID:Long): LiveData<Boolean> {
        val channel = channels[userID] ?: return MutableLiveData()
        return channel.isUserOnline
    }
    fun getLastMessageLiveData(userID: Long):LiveData<Message>{
        val channel = channels[userID] ?: return MutableLiveData()
        return channel.lastMessageSent
    }
    fun connect(){
      channels.forEach{
          it.value.connect()
      }
    }
}