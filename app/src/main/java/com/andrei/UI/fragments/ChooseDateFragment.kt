package com.andrei.UI.fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.andrei.carrental.R
import com.andrei.carrental.databinding.Example4CalendarDayBinding
import com.andrei.carrental.databinding.Example4FragmentBinding
import com.andrei.carrental.entities.RentalDate
import com.andrei.carrental.viewmodels.ViewModelCar
import com.andrei.engine.State
import com.andrei.utils.*
import com.andrei.utils.setTextColorRes
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import java.time.temporal.ChronoUnit
class ChooseDateFragment : Fragment (){


    private val today = LocalDate.now()

    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    private val viewModelCar : ViewModelCar by activityViewModels()

    private var unavailableDates:List<RentalDate>? = null

    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEE'\n'd MMM")

    private val startBackground: GradientDrawable by lazy {
        ContextCompat.getDrawable(requireContext(),R.drawable.example_4_continuous_selected_bg_start) as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        ContextCompat.getDrawable(requireContext(),R.drawable.example_4_continuous_selected_bg_end) as GradientDrawable
    }

    private lateinit var binding: Example4FragmentBinding

   inner class DayViewContainer(view: View) : ViewContainer(view) {
        lateinit var day: CalendarDay // Will be set when this container is bound.
        val binding = Example4CalendarDayBinding.bind(view)

        init {
            view.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isAfter(today))) {
                    val date = day.date
                    if (startDate != null) {
                        if (date < startDate || endDate != null) {
                            startDate = date
                            endDate = null
                        } else if (date != startDate) {
                            endDate = date
                        }
                    } else {
                        startDate = date
                    }
                    this@ChooseDateFragment.binding.exFourCalendar.notifyCalendarChanged()
                    bindSummaryViews()
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Example4FragmentBinding.inflate(inflater,container, false)
        fetchUnavailableDates()
        initializeUI()
        return binding.root
    }

    private fun initializeUI() {

        binding.backButtonChooseDateFrg.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun fetchUnavailableDates() {
        viewModelCar.unavailableCarDates.reObserve(viewLifecycleOwner){
                if(it is State.Success) {
                    unavailableDates = it.data
                    binding.progressBar.hide()
                    binding.exFourCalendar.notifyCalendarChanged()
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // We set the radius of the continuous selection background drawable dynamically
        // since the view size is `match parent` hence we cannot determine the appropriate
        // radius value which would equal half of the view's size beforehand.
        binding.exFourCalendar.post {
            val radius = ((binding.exFourCalendar.width / 7) / 2).toFloat()
            startBackground.setCornerRadius(topLeft = radius, bottomLeft = radius)
            endBackground.setCornerRadius(topRight = radius, bottomRight = radius)
        }

        // Set the First day of week depending on Locale
        val daysOfWeek = daysOfWeekFromLocale()
        binding.legendLayout.legendLayout.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                setTextColorRes(R.color.example_4_grey)
            }
        }

        initializeCalendarBinder()

        val currentMonth = YearMonth.now()
        binding.exFourCalendar.setup(currentMonth, currentMonth.plusMonths(12), daysOfWeek.first())
        binding.exFourCalendar.scrollToMonth(currentMonth)



        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.exFourHeaderTextMonth)
        }
        binding.exFourCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val monthTitle = "${month.yearMonth.month.name.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)} ${month.year}"
                container.textView.text = monthTitle
            }
        }

        binding.buttonSaveSelection.setOnClickListener click@{
            val startDate = startDate

            if(startDate != null){
               val action = ChooseDateFragmentDirections.actionChooseDatesFragmentToConfirmSelectionFragment(
                       startDate.toUnix(),  (endDate ?: startDate).toUnix())
                findNavController().navigate(action)

            }


        }

        bindSummaryViews()
    }

    private fun isInDateBetween(inDate: LocalDate, startDate: LocalDate, endDate: LocalDate): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (inDate.yearMonth == startDate.yearMonth) return true
        if (inDate.yearMonth.plusMonths(1) == endDate.yearMonth) return true
        return inDate > startDate && inDate < endDate
    }

    private fun isOutDateBetween(outDate: LocalDate, startDate: LocalDate, endDate: LocalDate): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (outDate.yearMonth == endDate.yearMonth) return true
        if (outDate.yearMonth.minusMonths(1) == startDate.yearMonth) return true
        return outDate > startDate && outDate < endDate
    }

    private fun bindSummaryViews() {
        binding.exFourStartDateText.apply {
            if (startDate != null) {
                text = headerDateFormatter.format(startDate)
                setTextColorRes(R.color.example_4_grey)
            } else {
                text = getString(R.string.start_date)
                setTextColor(Color.GRAY)
            }
        }

        binding.exFourEndDateText.apply {
            if (endDate != null) {
                text = headerDateFormatter.format(endDate)
                setTextColorRes(R.color.example_4_grey)
            } else {
                text = getString(R.string.end_date)
                setTextColor(Color.GRAY)
            }
        }

        binding.buttonSaveSelection.isEnabled = startDate != null
    }

    private fun initializeCalendarBinder(){
        binding.exFourCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.exFourDayText
                val roundBgView = container.binding.exFourRoundBgView

                textView.text = null
                textView.background = null
                roundBgView.hide()

                val startDate = startDate
                val endDate = endDate

                when (day.owner) {
                    DayOwner.THIS_MONTH -> {
                        textView.text = day.day.toString()
                        val dayUnix = day.date.toUnix()

                        val unavailableDate = unavailableDates?.find {
                            dayUnix >= it.startDate && dayUnix <= it.endDate
                        }

                        if (day.date.isBefore(today) || unavailableDates == null || unavailableDate!= null ) {
                            textView.setTextColorRes(R.color.example_4_grey_past)
                        } else {
                            when {
                                startDate == day.date && endDate == null -> {
                                    textView.setTextColorRes(R.color.white)
                                    roundBgView.show()
                                    roundBgView.setBackgroundResource(R.drawable.example_4_single_selected_bg)
                                }
                                day.date == startDate -> {
                                    textView.setTextColorRes(R.color.white)
                                    textView.background = startBackground
                                }
                                startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                    textView.setTextColorRes(R.color.white)
                                    textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                                }
                                day.date == endDate -> {
                                    textView.setTextColorRes(R.color.white)
                                    textView.background = endBackground
                                }
                                day.date == today -> {
                                    textView.setTextColorRes(R.color.example_4_grey)
                                    roundBgView.show()
                                    roundBgView.setBackgroundResource(R.drawable.example_4_today_bg)
                                }
                                else -> textView.setTextColorRes(R.color.example_4_grey)
                            }
                        }
                    }
                    // Make the coloured selection background continuous on the invisible in and out dates across various months.
                    DayOwner.PREVIOUS_MONTH ->
                        if (startDate != null && endDate != null && isInDateBetween(day.date, startDate, endDate)) {
                            textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                        }
                    DayOwner.NEXT_MONTH ->
                        if (startDate != null && endDate != null && isOutDateBetween(day.date, startDate, endDate)) {
                            textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                        }
                }
            }
        }
    }



}
