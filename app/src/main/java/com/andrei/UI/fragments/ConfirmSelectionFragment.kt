package com.andrei.UI.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentConfirmSelectionBinding
import com.andrei.carrental.entities.BookingDate
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.carrental.viewmodels.ViewModelPayment
import com.andrei.engine.State
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.requestModels.NewBookingRequestModel
import com.andrei.engine.requestModels.PaymentRequest
import com.andrei.engine.responseModels.TokenResponse
import com.andrei.utils.formatWithPattern
import com.andrei.utils.observeRequest
import com.andrei.utils.reObserve
import com.andrei.utils.toUnix
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.braintreepayments.api.exceptions.InvalidArgumentException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmSelectionFragment : Fragment(R.layout.fragment_confirm_selection) {

    @Inject
    lateinit var userManager:UserManager


    private  val  binding: FragmentConfirmSelectionBinding by viewBinding()
    private val viewModelPayment:ViewModelPayment by viewModels()
    private val viewModelCar:ViewModelCar by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeUI()
    }

    private fun initializeUI() {
        viewModelCar.currentSelectedDays.reObserve(viewLifecycleOwner) { rentalPeriod ->
            if (rentalPeriod != null) {
             updateUIWithRentalPeriod(rentalPeriod)
            }
        }
        viewModelCar.totalAmountToPay.reObserve(viewLifecycleOwner){
            if(it !=null){
                binding.tvTotalConfirmation.text = "£$it"
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

        lifecycleScope.launch {
            viewModelPayment.clientToken.collect { state->
                when (state) {
                    is State.Success -> {
                        try {
                            state.data?.let { showDropInPaymentWindow(it) }


                        } catch (e: InvalidArgumentException) {
                            // There was an issue with your authorization string.
                        }

                    }
                    is State.Loading -> {}
                    is State.Error -> {}
                }
            }
        }
    }

    private fun showDropInPaymentWindow(token:String) {
        val dropInRequest: DropInRequest = DropInRequest()
            .collectDeviceData(true)
            .clientToken(token)
        startActivityForResult(dropInRequest.getIntent(requireContext()), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val result: DropInResult? = data?.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)
                if(result!=null) {
                    startCheckout(result)
                }
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                val error = data?.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
            }
        }
    }

    private fun startCheckout(result: DropInResult) {
        val amountToPay = viewModelCar.totalAmountToPay.value
        val currentCarID = viewModelCar.currentCarID.value
        val bookingDate = viewModelCar.currentSelectedDays.value
        val userLoginData = userManager.userLoginData.value

        if (amountToPay != null && currentCarID != null && bookingDate != null && userLoginData != null) {

            val paymentRequest = PaymentRequest(nonce = result.paymentMethodNonce?.nonce,
                    deviceData = result.deviceData, amount = amountToPay)

            val checkoutRequest = NewBookingRequestModel(paymentRequest = paymentRequest,
                    startDate = bookingDate.startDate.toUnix(),
                    endDate = bookingDate.endDate.toUnix(),
                    userID = userLoginData.id,
                    carID = currentCarID
            )

            viewModelPayment.checkout(checkoutRequest).observeRequest(viewLifecycleOwner) {
                when(it){
                    is State.Success -> {
                        val action = ConfirmSelectionFragmentDirections.actionConfirmSelectionFragmentToSuccessfulPaymentFragment()
                        findNavController().navigate(action)
                    }
                    is State.Loading ->{
                    
                    }
                    is State.Error ->{
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}