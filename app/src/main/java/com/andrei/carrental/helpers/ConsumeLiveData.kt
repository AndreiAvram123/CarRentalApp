package com.andrei.carrental.helpers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
/**
  Live data that clears it's value once it's fired the event
 */
class  ConsumeLiveData<T> : MutableLiveData<T>() {
 override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
    super.observe(owner){
        if(it!=null){
          observer.onChanged(it)
           value = null
        }
    }
 }
}