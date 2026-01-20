package com.example.mycalendar.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycalendar.ui.model.ShopItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicLong

private const val INITIAL_RANGE = 14L // по 2 недели
private const val PAGE_SIZE = 7L


class ShopPlannerViewModel : ViewModel() {

    fun fakeItemList() {
        _daysWithItems.update { map ->
            val list = listOf(
                ShopItem(1L, "Хлеб"),
                ShopItem(2L, "Молоко"),
                ShopItem(3L, "Макароны", true),
                ShopItem(4L, "Чипсы", true),
                ShopItem(5L, "Пельмени"),
                ShopItem(6L, "Консервы", true),
                ShopItem(7L, "Огурцы"),
                ShopItem(8L, "Помидоры"),
                ShopItem(9L, "Оливки", true)
            )
            map + (LocalDate.now() to list)
        }

    }

    val today = LocalDate.now()

    private val _datesList =
        MutableStateFlow((-INITIAL_RANGE..INITIAL_RANGE).map { today.plusDays(it) })
    val datesList = _datesList.asStateFlow()

    fun loadNextToEnd() {
        _datesList.update { dates ->
            val last = dates.last()
            dates + (1L..PAGE_SIZE).map { last.plusDays(it) }
        }
    }

    fun loadPrevToStart() {
        _datesList.update { dates ->
            val first = dates.first()
            (PAGE_SIZE downTo 1).map { first.minusDays(it) } + dates
        }
    }


    private val _daysWithItems = MutableStateFlow<Map<LocalDate, List<ShopItem>>>(emptyMap())
    val daysWithItems = _daysWithItems.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate>(today)
    val selectedDate = _selectedDate.asStateFlow()

    val items: StateFlow<List<ShopItem>> =
        combine(_daysWithItems, _selectedDate) { days, date ->
            days[date].orEmpty()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    val activeItems: StateFlow<List<ShopItem>> =
        items
            .map { list -> list.filter { !it.checked } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    val reducedItems: StateFlow<List<ShopItem>> =
        items
            .map { list -> list.filter { it.checked } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val newId = AtomicLong()
    fun addToShopList(title: String) {
        val date = selectedDate.value
        val newItem = ShopItem(
            id = newId.getAndIncrement(),
            title = title.replaceFirstChar { it.uppercase() }
        )
        _daysWithItems.update { map ->
            val list = map[date].orEmpty()
            map + (date to (list + newItem))
        }
    }

    fun checkItem(id: Long) {
        val date = selectedDate.value

        _daysWithItems.update { map ->
            val list = map[date]?.map {
                if (it.id == id) it.copy(checked = !it.checked)
                else it
            } ?: emptyList()
            map + (date to list)
        }
    }

    fun checkAllItems() {
        val date = selectedDate.value
        _daysWithItems.update { map ->
            val list = map[date]?.map {
                it.copy(checked = true)
            } ?: emptyList()
            map + (date to list)
        }
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }
}