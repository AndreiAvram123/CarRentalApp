package com.andrei.carrental.binding

import androidx.databinding.BindingConversion
import com.andrei.utils.formatWithPattern
import java.time.LocalDate

@BindingConversion
fun convertLocalDateToString(localDate: LocalDate):String{
    return localDate.formatWithPattern()
}