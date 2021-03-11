package com.andrei.UI.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.andrei.UI.helpers.PermissionHandlerFragment

abstract class BaseFragment (layoutID:Int) : Fragment(layoutID) {

    protected lateinit var permissionHandlerFragment : PermissionHandlerFragment



    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.permissionHandlerFragment = PermissionHandlerFragment(this)
        initializeUI()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) = permissionHandlerFragment.notifyChange(requestCode, grantResults)

    abstract fun initializeUI()

}
