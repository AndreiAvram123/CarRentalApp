 package com.andrei.carrental.custom

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.andrei.utils.isResultOk
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult

class PaymentUIContract : ActivityResultContract<String, DropInResult?>() {

    override fun createIntent(context: Context, input: String): Intent {
        val dropInRequest: DropInRequest = DropInRequest()
                .collectDeviceData(true)
                .clientToken(input)
        return  dropInRequest.getIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): DropInResult? {
        if(resultCode.isResultOk()){
           return intent?.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)
        }
        return null
    }




}