package com.andrei.utils

import android.content.SharedPreferences

 fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
}

fun SharedPreferences.removeValue(key:String){
    edit {
        remove(key)
    }
}
