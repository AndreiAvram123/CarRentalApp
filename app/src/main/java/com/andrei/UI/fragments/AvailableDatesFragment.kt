package com.andrei.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.andrei.UI.adapters.DayViewContainer
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentAvailableDatesBinding
import com.andrei.utils.daysOfWeekFromLocale
import com.andrei.utils.setTextColorRes
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.utils.yearMonth
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

class AvailableDatesFragment : Fragment() {

   private lateinit var binding:FragmentAvailableDatesBinding
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentAvailableDatesBinding.inflate(inflater,container,false)
        val calendarView = binding.calendarLayout.calendarAvailableDates
        binding.calendarLayout.calendarAvailableDates.dayBinder= object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.tvDay.text = day.date.dayOfMonth.toString()
            }
        }
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
       calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
       calendarView.scrollToMonth(currentMonth)


        val daysOfWeek = daysOfWeekFromLocale()

       val legendLayout =  binding.calendarLayout.legendLayout.legendLayout
        legendLayout.children.forEachIndexed{ index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.UK).toUpperCase(Locale.UK)
                setTextColorRes(R.color.example_1_white_light)
            }
        }



        binding.calendarLayout.calendarAvailableDates.monthScrollListener = {
                binding.calendarLayout.exOneYearText.text = it.yearMonth.year.toString()
                binding.calendarLayout.exOneMonthText.text = monthTitleFormatter.format(it.yearMonth)
        }



        return binding.root
    }

}