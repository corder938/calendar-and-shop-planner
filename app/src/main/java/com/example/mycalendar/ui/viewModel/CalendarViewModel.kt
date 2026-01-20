package com.example.mycalendar.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.mycalendar.ui.screen.LANGUAGE_TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class CalendarViewModel : ViewModel() {

    val currentYearMonth = MutableStateFlow(YearMonth.now())

    val daysOfWeek = DayOfWeek.entries.drop(0).map {
        it.getDisplayName(
            TextStyle.SHORT,
            Locale.forLanguageTag(LANGUAGE_TAG)
        ).replaceFirstChar { first -> first.uppercase() }
    }

    fun monthBuilder(): List<LocalDate> {
        val firstDayOfMonth = currentYearMonth.value.atDay(1)
        val firstDayOfGrid = firstDayOfMonth.minusDays(
            ((firstDayOfMonth.dayOfWeek.value + 6) % 7).toLong()
        )
        return (0 until 42).map { offset ->
            firstDayOfGrid.plusDays(offset.toLong())
        }
    }

    fun toPreviousMonth() {
        currentYearMonth.update { it.minusMonths(1) }
    }

    fun toNextMonth() {
        currentYearMonth.update { it.plusMonths(1) }
    }
}