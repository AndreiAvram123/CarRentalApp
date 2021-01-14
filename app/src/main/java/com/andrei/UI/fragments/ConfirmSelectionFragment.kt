package com.andrei.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentConfirmSelectionBinding
import com.andrei.carrental.viewmodels.ViewModelPayment
import com.andrei.engine.State
import com.andrei.utils.fromUnixToLocalDate
import com.andrei.utils.reObserve
import com.braintreepayments.api.BraintreeFragment
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.exceptions.InvalidArgumentException
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class ConfirmSelectionFragment : Fragment() {


    private lateinit var mBraintreeFragment: BraintreeFragment
    private lateinit var binding: FragmentConfirmSelectionBinding
    private val navArgs: ConfirmSelectionFragmentArgs by navArgs()
    private val  viewModelPayment:ViewModelPayment by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentConfirmSelectionBinding.inflate(inflater, container, false)
        updateUI()

        return binding.root
    }

    private fun updateUI() {
        val startDate = navArgs.startDate.fromUnixToLocalDate()
        val endDate = navArgs.endDate.fromUnixToLocalDate()
        val days = ChronoUnit.DAYS.between(startDate, endDate) + 1
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
        binding.apply {
            tvNumberDays.text = getString(R.string.days, days.toString())
            tvStartDateConfirmation.text = startDate.format(formatter)
            tvEndDateConfirmation.text = endDate.format(formatter)
        }
        binding.backButtonConfirmFrg.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonConfirm.setOnClickListener {
            startPaymentFlow()
        }
    }

    private fun startPaymentFlow() {
        viewModelPayment.clientToken.reObserve(viewLifecycleOwner){
            when{
                it is State.Success ->{
                    try {
                        val dropInRequest: DropInRequest = DropInRequest()
                                .clientToken(it.data.token)
                        startActivityForResult(dropInRequest.getIntent(requireContext()), 1)
                    } catch (e: InvalidArgumentException) {
                        // There was an issue with your authorization string.
                    }

                }
                it is State.Loading ->{}
                it is State.Error -> {}
            }

        }
    }
}