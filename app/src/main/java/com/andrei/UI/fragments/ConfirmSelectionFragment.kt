package com.andrei.UI.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.carrental.viewmodels.ViewModelPayment
import com.andrei.engine.State
import com.andrei.engine.helpers.SessionManager
import com.andrei.engine.requestModels.NewBookingRequestModel
import com.andrei.engine.requestModels.PaymentRequest
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
        if(it!= null){

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeUI()
    }

    private fun initializeUI() {

        viewModelCar.calculateTotalAmountToPay()


        viewModelCar.currentSelectedDays.reObserve(viewLifecycleOwner) { rentalPeriod ->
            if (rentalPeriod != null) {
             updateUIWithRentalPeriod(rentalPeriod)
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModelCar.totalAmountToPay.filterNotNull().collect {
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
            viewModelPayment.clientToken.collect {
                when (it) {
                    is State.Success -> {
                        try {
                           dropInUIIntent.launch(it.data)

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





//    private fun startCheckout(result: DropInResult) {
//        val amountToPay = viewModelCar.totalAmountToPay.value
//        val currentCarID = viewModelCar.currentCarID.value
//        val bookingDate = viewModelCar.currentSelectedDays.value
//        val userLoginData = userDataManager.getUserID()
//
//        if (amountToPay != null && currentCarID != null && bookingDate != null) {
//
//            val paymentRequest = PaymentRequest(nonce = result.paymentMethodNonce?.nonce,
//                    deviceData = result.deviceData, amount = amountToPay)
//
//            val checkoutRequest = NewBookingRequestModel(paymentRequest = paymentRequest,
//                    startDate = bookingDate.startDate.toUnix(),
//                    endDate = bookingDate.endDate.toUnix(),
//                    userID = userLoginData,
//                    carID = currentCarID
//            )
//
//            viewModelPayment.checkout(checkoutRequest).observeRequest(viewLifecycleOwner) {
//                when(it){
//                    is State.Success -> {
//                        val action = ConfirmSelectionFragmentDirections.actionConfirmSelectionFragmentToSuccessfulPaymentFragment()
//                        findNavController().navigate(action)
//                    }
//                    is State.Loading ->{
//
//                    }
//                    is State.Error ->{
//                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }
//    }
}