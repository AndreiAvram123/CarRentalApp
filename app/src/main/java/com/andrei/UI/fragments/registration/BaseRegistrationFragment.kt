package com.andrei.UI.fragments.registration

import androidx.annotation.LayoutRes
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.viewmodels.ViewModelSignUp

abstract class BaseRegistrationFragment(@LayoutRes layoutID:Int) : BaseFragment(layoutID) {

    protected val viewModelSignUp:ViewModelSignUp by activityViewModels()

    protected fun navigateBack() = findNavController().popBackStack()

    abstract fun showError(error:String)

    abstract fun hideError()

    abstract fun disableNextButton()

    abstract fun enableNextButton()

    abstract fun navigateForward()

}