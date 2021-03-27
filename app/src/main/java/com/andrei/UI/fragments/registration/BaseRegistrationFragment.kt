package com.andrei.UI.fragments.registration

import androidx.annotation.LayoutRes
import com.andrei.UI.fragments.BaseFragment

abstract class BaseRegistrationFragment(@LayoutRes layoutID:Int) : BaseFragment(layoutID) {

    abstract val runnableDetail:Runnable

    abstract fun showError(error:String)

    abstract fun hideError()

    abstract fun disableNextButton()

    abstract fun enableNextButton()

    abstract fun navigateForward()
}