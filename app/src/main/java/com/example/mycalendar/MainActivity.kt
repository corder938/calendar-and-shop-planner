package com.example.mycalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.mycalendar.ui.screen.CalendarScreen
import com.example.mycalendar.ui.theme.MyCalendarTheme
import com.example.mycalendar.ui.viewModel.CalendarViewModel
import com.example.mycalendar.ui.viewModel.ShopPlannerViewModel

class MainActivity : ComponentActivity() {

    val shopPlannerViewModel by lazy { ViewModelProvider(this)[ShopPlannerViewModel::class.java] }
    val calendarViewModel by lazy { ViewModelProvider(this)[CalendarViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyCalendarTheme {
                CalendarScreen(calendarViewModel)
            }
        }
    }
}


