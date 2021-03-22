package com.andrei.utils

import android.content.SharedPreferences

 fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
}

fun SharedPreferences.getStringOrNull(key:String):String? {
    return this.getString(key, null)
}
fun SharedPreferences.getLongOrNull(key:String): Long? {
    val value =  this.getLong(key,0)
    return if(value == 0L){
        null
    }else{
        value
    }
}

fun SharedPreferences.removeValue(key:String){
    edit {
        remove(key)
    }
}
