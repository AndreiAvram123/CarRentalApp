package com.andrei.carrental.factories

import android.content.Context
import com.andrei.carrental.R
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions

object PusherFactory {
  fun createPusher(pusherOptions: PusherOptions, key:String): Pusher{
      return Pusher(key,pusherOptions)
  }
}