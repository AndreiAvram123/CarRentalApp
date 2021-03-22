package com.andrei.carrental

import android.content.SharedPreferences
import androidx.core.content.edit
import com.andrei.utils.edit
import com.andrei.utils.getLongOrNull
import com.andrei.utils.getStringOrNull
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Singleton
class UserDataManager @Inject constructor(
    sharedPreferences: SharedPreferences
) {

    var userID:Long  by PrefsDelegateLong(sharedPreferences)
    var email:String? by PrefsDelegateString(sharedPreferences)

}


class PrefsDelegateLong(
     private val  sharedPreferences: SharedPreferences

) : ReadWriteProperty<Any?,Long>{

    override fun getValue(thisRef: Any?, property: KProperty<*>): Long {
       return sharedPreferences.getLong(property.name,0)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
        sharedPreferences.edit {
            putLong(property.name,value)
        }
    }

}

class PrefsDelegateString(
        private val sharedPreferences: SharedPreferences
):ReadWriteProperty<Any?,String?>{

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
       sharedPreferences.edit {
           putString(property.name,value)
       }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return sharedPreferences.getStringOrNull(property.name)
    }

}