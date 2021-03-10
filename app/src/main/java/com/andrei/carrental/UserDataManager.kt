package com.andrei.carrental

import android.content.Context
import com.andrei.utils.getLongOrZero
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserDataManager @Inject constructor(
    @ApplicationContext private val context:Context
) {

    fun getUserID():Long{
        val id = context.getSharedPreferences(context.getString(R.string.key_preferences), Context.MODE_PRIVATE).getLongOrZero(context.getString(R.string.key_user_id))
        check(id != 0L){"The user ID should not be 0 when this method is called"}
        return id
    }
}