package com.andrei.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionHandler(private val context:Context){

    private val requestPermissionMap :MutableMap<Int,(permissionGranted:Boolean)->Unit> = mutableMapOf()

    private  var currentRequestCode = 1

    fun hasPermission(permission:String) : Boolean{
       return ContextCompat.checkSelfPermission(
                context,
                permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(fragment: Fragment, permission: String, callback: (permissionGranted:Boolean)-> Unit){
        requestPermissionMap[currentRequestCode] = callback
        fragment.requestSinglePermission(permission,currentRequestCode)
        currentRequestCode ++
    }


    fun notifyChange(requestCode: Int,
                     grantResults: IntArray) {

        if(requestPermissionMap.containsKey(requestCode)){
            val callback = requestPermissionMap[requestCode]
            requestPermissionMap.remove(requestCode)
            if(isPermissionGranted(grantResults)){
                callback?.invoke(true)
            }else{
                callback?.invoke(false)
            }
        }
    }

    private fun isPermissionGranted(grantResults:IntArray) : Boolean {
        return grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED
    }

    private fun Fragment.requestSinglePermission(permission: String, requestCode: Int){
        val permissions = Array(1){permission}
        requestPermissions(permissions,requestCode)
    }
}