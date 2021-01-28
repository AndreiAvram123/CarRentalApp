package com.andrei.UI.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentConfirmSelectionBinding
import com.andrei.carrental.entities.RentalPeriod
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.carrental.viewmodels.ViewModelPayment
import com.andrei.engine.DTOEntities.*
import com.andrei.engine.State
import com.andrei.utils.formatWithPattern
import com.andrei.utils.observeRequest
import com.andrei.utils.reObserve
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.braintreepayments.api.exceptions.InvalidArgumentException
import java.time.temporal.ChronoUnit


class ConfirmSelectionFragment : Fragment() {


    private lateinit var binding: FragmentConfirmSelectionBinding
    private val  viewModelPayment:ViewModelPayment by viewModels()
    private val  viewModelCar:ViewModelCar by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentConfirmSelectionBinding.inflate(inflater, container, false)
        initializeUI()

        return binding.root
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
    private fun updateUIWithRentalPeriod(rentalPeriod: RentalPeriod){
        val startDate = rentalPeriod.startDate
        val endDate = rentalPeriod.endDate
        val days = ChronoUnit.DAYS.between(startDate,endDate)
        binding.apply {
            tvNumberDays.text = getString(R.string.days, days.toString())
            tvStartDateConfirmation.text = startDate.formatWithPattern("d MMMM yyyy")
            tvEndDateConfirmation.text = endDate.formatWithPattern("d MMMM yyyy")
        }
    }


    private fun startPaymentFlow() {
        viewModelPayment.clientToken.reObserve(viewLifecycleOwner){
            when (it) {
                is State.Success -> {
                    try {
                        if(it.data!=null){
                            showDropInPaymentWindow(it.data)
                        }
                    } catch (e: InvalidArgumentException) {
                        // There was an issue with your authorization string.
                    }

                }
                is State.Loading -> {}
                is State.Error -> {}
            }

        }
    }

    private fun showDropInPaymentWindow(data: TokenResponse) {
        val dropInRequest: DropInRequest = DropInRequest()
            .collectDeviceData(true)
            .clientToken(data.token)
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
        val selectedDates = viewModelCar.currentSelectedDays.value

        if (amountToPay != null &&
                currentCarID != null &&
                selectedDates != null) {

            val paymentRequest = PaymentRequest(nonce = result.paymentMethodNonce?.nonce,
                    deviceData = result.deviceData, amount = amountToPay)
            val rentInformation = RentInformation(
                    rentalPeriodDTO = selectedDates.toRentalPeriodDTO(),
                    currentCarID
            )

            val checkoutRequest = CheckoutRequest(paymentRequest = paymentRequest,
                    rentInformation = rentInformation)

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