package com.andrei.carrental.factories

import android.content.Context
import com.andrei.carrental.R
import com.andrei.messenger.PusherConfig
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions

object PusherFactory {
  fun createPusher(pusherConfig: PusherConfig): Pusher{
      return Pusher(pusherConfig.pusherKey,pusherConfig.pusherOptions)
  }
}