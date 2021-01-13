package com.andrei.UI.adapters

import android.view.View
import android.widget.TextView
import com.andrei.carrental.R
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.LocalDate

class DayViewContainer(view: View, private val calendarView: CalendarView) : ViewContainer(view) {
    val tvDay = view.findViewById<TextView>(R.id.calendarDayText)
     val selectedDates = mutableSetOf<LocalDate>()

    lateinit var day: CalendarDay
    init{
        view.setOnClickListener{
            // Will be set when this container is bound. See the dayBinder.
            view.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH) {
                    if (selectedDates.contains(day.date)) {
                        selectedDates.remove(day.date)
                    } else {
                        selectedDates.add(day.date)
                    }
                    calendarView.notifyDayChanged(day)

                }
            }
        }
    }
}