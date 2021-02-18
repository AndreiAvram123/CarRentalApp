package com.andrei.carrental

import android.content.Context
import com.andrei.utils.getLongOrZero

object UserDataManager {

    fun getUserID(context: Context):Long{
        val id = context.getSharedPreferences(context.getString(R.string.key_preferences), Context.MODE_PRIVATE).getLongOrZero(context.getString(R.string.key_user_id))
        check(id != 0L){"The user ID should not be 0 when this method is called"}
        return id
    }
}