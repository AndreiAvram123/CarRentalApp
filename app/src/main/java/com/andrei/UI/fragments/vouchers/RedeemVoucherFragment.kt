package com.andrei.UI.fragments.vouchers

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentRedeemVoucherLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelVoucher
import com.andrei.engine.State
import com.andrei.utils.gone
import com.andrei.utils.hide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect

class RedeemVoucherFragment : BaseFragment(R.layout.fragment_redeem_voucher_layout) {

    private val binding:FragmentRedeemVoucherLayoutBinding by viewBinding ()
    private val viewModelVoucher:ViewModelVoucher by activityViewModels()

    override fun initializeUI() {

        lifecycleScope.launchWhenResumed {
            viewModelVoucher.availableVouchers.collect {
                if(it is State.Success){
                    binding.voucher = it.data.firstOrNull()
                }
            }
        }

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}