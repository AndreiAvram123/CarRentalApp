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

fun SharedPreferences.removeValue(key:String){
    edit {
        remove(key)
    }
}
