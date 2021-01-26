package com.andrei.UI.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.andrei.utils.PermissionHandlerFragment

open class BaseFragment : Fragment() {
    protected lateinit var permissionHandlerFragment :PermissionHandlerFragment


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.permissionHandlerFragment = PermissionHandlerFragment(this)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) = permissionHandlerFragment.notifyChange(requestCode, grantResults)

}