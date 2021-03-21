    package com.andrei.UI.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.UserDataManager
import com.andrei.carrental.custom.PaymentUIContract
import com.andrei.carrental.databinding.FragmentConfirmSelectionBinding
import com.andrei.carrental.entities.BookingDate
import com.andrei.carrental.entities.CheckoutCarData
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.carrental.viewmodels.ViewModelPayment
import com.andrei.engine.State
import com.andrei.engine.helpers.SessionManager
import com.andrei.engine.requestModels.PaymentRequest
import com.andrei.utils.formatWithPattern
import com.andrei.utils.reObserve
import com.braintreepayments.api.dropin.DropInResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmSelectionFragment : Fragment(R.layout.fragment_confirm_selection) {

    @Inject
    lateinit var sessionManager:SessionManager

    @Inject
    lateinit var userDataManager: UserDataManager

    private  val  binding: FragmentConfirmSelectionBinding by viewBinding()
    private val viewModelPayment:ViewModelPayment by viewModels()
    private val viewModelCar:ViewModelCar by activityViewModels()

    private val dropInUIIntent = registerForActivityResult(PaymentUIContract()){
        it?.let{
          viewModelPayment.setDropInResult(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeUI()
    }

    private fun initializeUI() {

        viewModelCar.calculateTotalAmountToPay()

        lifecycleScope.launchWhenResumed {
            viewModelCar.currentSelectedDays.filterNotNull().collect {
                updateUIWithRentalPeriod(it)
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModelCar.totalAmountToPay.filterNotNull().collect {
                binding.tvTotalConfirmation.text = "£$it"
            }

            lifecycleScope.launchWhenResumed {
                viewModelPayment.navigateForward.collect {
                    if(it){
                        val action = ConfirmSelectionFragmentDirections.actionConfirmSelectionFragmentToSuccessfulPaymentFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }

        binding.backButtonConfirmFragment.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonConfirm.setOnClickListener {
            startPaymentFlow()
        }

    }
    private fun updateUIWithRentalPeriod(bookingDate: BookingDate){
        val startDate = bookingDate.startDate
        val endDate = bookingDate.endDate
        val days = ChronoUnit.DAYS.between(startDate,endDate)
        binding.apply {
            tvNumberDays.text = getString(R.string.days, days.toString())
            tvStartDateConfirmation.text = startDate.formatWithPattern("d MMMM yyyy")
            tvEndDateConfirmation.text = endDate.formatWithPattern("d MMMM yyyy")
        }
    }


    private fun startPaymentFlow() {
        viewModelPayment.getClientToken()
        viewModelCar.newCarCheckoutData()?.let { viewModelPayment.setCheckoutData(it) }

        lifecycleScope.launch {
            viewModelPayment.clientToken.collect {
                if(it is State.Success){
                    dropInUIIntent.launch(it.data)
                }
            }
        }
    }
}