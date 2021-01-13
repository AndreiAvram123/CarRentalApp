package com.andrei.UI.adapters

import android.view.View
import android.widget.TextView
import com.andrei.carrental.R
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val tvDay = view.findViewById<TextView>(R.id.calendarDayText)

    // With ViewBinding
    // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
}