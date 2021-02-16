package com.andrei.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pusher.client.Pusher

class ChannelService(
    private val userID:Long,
    private val pusher: Pusher) {

    private val _isUserOnline:MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    val isUserOnline:LiveData<Boolean>
    get() = _isUserOnline


    init {
        bindToNewUserAddedEvent()
    }

    private fun bindToNewUserAddedEvent() {
        val listener = CustomPresenceChannelListener()
        pusher.subscribePresence("presence-my-channel",listener,"pusher:member_added")

        listener.onUserSubscribed = { _,_ ->
            _isUserOnline.postValue(true)
        }
        listener.onUserUnsubscribed = {_,_ ->
           _isUserOnline.postValue(false)
        }
        listener.onUsersInformationRetrieved = { _,users ->
            _isUserOnline.postValue(users?.isNotEmpty())
        }



    }

    companion object {
        private const val eventNewUserAdded = "pusher:member_added"
    }

}