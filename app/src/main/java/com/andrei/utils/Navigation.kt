package com.andrei.utils

import android.app.Activity
import android.content.Intent
import androidx.navigation.NavController
import com.andrei.UI.activities.MainActivity

fun NavController.navigateIfConnected(){

}

inline fun <reified T: Activity> Activity.startNewActivity(){
    Intent(this,T::class.java).also {
        startActivity(it)
    }
    finishAffinity()
}

fun Int.isResultOk() = this == Activity.RESULT_OK