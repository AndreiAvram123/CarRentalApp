package com.andrei.utils

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.andrei.UI.activities.MainActivity

inline fun <reified T: Activity> Activity.startNewActivity(){
    Intent(this,T::class.java).also {
        startActivity(it)
    }
    finish()
}

inline fun <reified T: Activity> Fragment.startNewActivity(){
    val currentActivity = requireActivity()
    Intent(currentActivity,T::class.java).also {
        startActivity(it)
    }
    currentActivity.finish()
}

fun Int.isResultOk() = this == Activity.RESULT_OK