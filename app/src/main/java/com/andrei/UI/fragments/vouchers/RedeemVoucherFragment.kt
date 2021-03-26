package com.andrei.UI.fragments.vouchers

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.vouchers.VouchersAdapter
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentRedeemVouchersLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelVoucher
import com.andrei.engine.State
import com.andrei.utils.show
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.SwipeableMethod
import kotlinx.coroutines.flow.collect

class RedeemVoucherFragment : BaseFragment(R.layout.fragment_redeem_vouchers_layout), VouchersAdapter.VouchersAdapterCallback, CardStackListener {

    private val binding:FragmentRedeemVouchersLayoutBinding by viewBinding ()
    private val viewModelVoucher:ViewModelVoucher by activityViewModels()
    private val vouchersAdapter = VouchersAdapter(this)

    override fun initializeUI() {

        configureStackView()

        lifecycleScope.launchWhenResumed {
            viewModelVoucher.availableVouchers.collect {
                if(it is State.Success){
                    val vouchers = it.data
                    vouchersAdapter.setData(vouchers)
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModelVoucher.noVouchersAvailable.collect {
                if(it){
                    binding.noVouchersAnimation.playAnimation()
                    binding.btBackToApp.show()
                }
            }
        }
        binding.btBackToApp.setOnClickListener {
            if (it.isVisible){
                findNavController().popBackStack()
            }
        }
    }

    private fun configureStackView() {
        val layoutManager = CardStackLayoutManager(requireContext(),this)
        layoutManager.apply {
           setSwipeableMethod(SwipeableMethod.Automatic)
            setDirections(Direction.FREEDOM)
        }

        binding.stackView.apply {
            this.layoutManager = layoutManager
            this.adapter = vouchersAdapter
        }
    }

    override fun onVoucherRedeemed() {
        binding.stackView.swipe()
    }
    override fun onCardSwiped(direction: Direction?) {
        viewModelVoucher.redeemLastVoucher()
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }


    override fun onCardRewound() {

    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }


}