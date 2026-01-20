package com.example.mycalendar.ui.model.calendar

import java.time.LocalDate

data class CalendarDay(
    val day: LocalDate,
    val isToday: Boolean,
    val isCurrentMonth: Boolean,
    val hasEvents: Boolean
)