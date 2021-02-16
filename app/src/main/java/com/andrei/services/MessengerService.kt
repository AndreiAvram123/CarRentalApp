package com.andrei.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pusher.client.Pusher

class MessengerService (usersIDs:List<Long>, private val pusher: Pusher){

    private val channels : Map<Long,ChannelService> = usersIDs.map { it to ChannelService(it,pusher) }.toMap()


    fun getUserOnlineLiveData(userID:Long): LiveData<Boolean> {
        val channel = channels[userID] ?: return MutableLiveData()
        return channel.isUserOnline
    }
    fun getLastMessageLiveData(userID: Long):LiveData<String>{
        return MutableLiveData()
    }
    fun connect(){
        pusher.connect()
    }
}