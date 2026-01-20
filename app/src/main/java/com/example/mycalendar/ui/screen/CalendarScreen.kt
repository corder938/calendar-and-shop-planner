package com.example.mycalendar.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mycalendar.ui.model.calendar.CalendarMode
import com.example.mycalendar.ui.theme.MyCalendarTheme
import com.example.mycalendar.ui.viewModel.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

const val LANGUAGE_TAG = "RU"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: CalendarViewModel) {

    val yearMonth = viewModel.currentYearMonth.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = { CalendarTopAppBar() }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            item {
                FormatPicker()
            }
            item {
                CalendarView(
                    yearMonth = yearMonth.value,
                    weekDaysNames = viewModel.daysOfWeek,
                    days = viewModel.monthBuilder(),
                    onPreviousClick = { viewModel.toPreviousMonth() },
                    onNextClick = { viewModel.toNextMonth() }
                )
            }
        }
    }
}


@Composable
fun FormatPicker() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(30))
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(30)
            )

            .padding(vertical = 7.dp)
    ) {
        var mode by rememberSaveable { mutableStateOf(CalendarMode.MONTH) }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Месяц",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(10f)
                .background(
                    if (mode == CalendarMode.MONTH) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(30)
                )
                .padding(vertical = 5.dp)
                .clickable(onClick = { mode = CalendarMode.MONTH })

        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Неделя",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(10f)
                .background(
                    if (mode == CalendarMode.WEEK) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(30)
                )
                .padding(vertical = 5.dp)
                .clickable(onClick = { mode = CalendarMode.WEEK })
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "День",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(10f)
                .background(
                    if (mode == CalendarMode.DAY) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(30)
                )
                .padding(vertical = 5.dp)
                .clickable(onClick = { mode = CalendarMode.DAY })
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun CalendarView(
    yearMonth: YearMonth,
    weekDaysNames: List<String>,
    days: List<LocalDate>,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.padding(bottom = 10.dp)) {
                Column {
                    Text(text = yearMonth.year.toString())
                    Row {

                        Text(
                            text = yearMonth.month.getDisplayName(
                                TextStyle.FULL_STANDALONE, Locale.forLanguageTag(LANGUAGE_TAG)
                            ).replaceFirstChar { it.uppercase() })
                        IconButton(
                            onClick = { TODO() },
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "MonthChooser",
                            )
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onPreviousClick() }, Modifier.fillMaxHeight()) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "PreviousMonth",
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape
                            )
                            .padding(5.dp)
                    )
                }
                IconButton(onClick = { onNextClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "NextMonth",
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape
                            )
                            .padding(5.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                weekDaysNames.forEach {
                    Text(
                        text = it, textAlign = TextAlign.Center, modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.fillMaxWidth()) {
                    days.chunked(7).forEach { week ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                        ) {
                            week.forEach { day ->
                                CalendarCell(day, Color.Red, Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarCell(day: LocalDate, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = day.dayOfMonth.toString(), textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .background(color, CircleShape)
                .size(5.dp)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopAppBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        title = {
            Text(text = "Календарь")
        },
        actions = {
            val actionIconPadding = Modifier.padding(horizontal = 8.dp)
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "SearchIcon",
                    actionIconPadding
                )
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "NotificationsIcon",
                    actionIconPadding
                )
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .then(actionIconPadding)
                        .border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .padding(8.dp)

                )
            }

        })
}

@PreviewLightDark
@Composable
fun Preview() {
    MyCalendarTheme {
        CalendarScreen(CalendarViewModel())
    }
}