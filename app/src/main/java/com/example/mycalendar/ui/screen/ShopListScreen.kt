package com.example.mycalendar.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mycalendar.ui.model.ShopItem
import com.example.mycalendar.ui.theme.MyCalendarTheme
import com.example.mycalendar.ui.viewModel.ShopPlannerViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopListScreen(viewModel: ShopPlannerViewModel) {

    val activeItems = viewModel.activeItems.collectAsStateWithLifecycle().value
    val reducedItems = viewModel.reducedItems.collectAsStateWithLifecycle().value

    val days = viewModel.daysWithItems.collectAsStateWithLifecycle().value
    val selectedDate = viewModel.selectedDate.collectAsStateWithLifecycle().value

    val dates by viewModel.datesList.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    var isInitialLaunch by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(dates) {
        if (isInitialLaunch) {
            val todayIndex = dates.indexOf(LocalDate.now())
            if (todayIndex != -1) {
                listState.scrollToItem(todayIndex - 1)
            }
            isInitialLaunch = false
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        text = "Список покупок",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.TwoTone.MoreVert,
                        contentDescription = "More icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ArrowBack icon",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            item {
                DataPicker(
                    dates = dates,
                    selectedDate = selectedDate,
                    hasItemsList = days.keys,
                    listState = listState,
                    onDateSelected = { viewModel.onDateSelected(it) },
                    onReachedStart = { viewModel.loadPrevToStart() },
                    onReachedEnd = { viewModel.loadNextToEnd() }
                )
            }
            item { ProgressCardHeader(activeItems.size + reducedItems.size, selectedDate) }
            item { ProgressCard(activeItems.size, reducedItems.size) }
            item {
                ShopItemHeader {
                    viewModel.checkAllItems()
                }
            }

            items(
                items = activeItems,
                key = { it.id }
            ) { item ->
                ActiveShopItem(
                    item = item,
                    onCheckedChange = { viewModel.checkItem(item.id) }
                )
            }
            item { AddToShopList(onClick = { viewModel.addToShopList(it) }) }

            items(
                items = reducedItems,
                key = { it.id }
            ) { item ->
                ReducedShopItem(
                    item = item,
                    onCheckedChange = { viewModel.checkItem(item.id) }
                )
            }
        }
    }
}

@Composable
fun DataPicker(
    dates: List<LocalDate>,
    selectedDate: LocalDate,
    hasItemsList: Set<LocalDate>,
    listState: LazyListState,
    onDateSelected: (LocalDate) -> Unit,
    onReachedStart: () -> Unit,
    onReachedEnd: () -> Unit
) {

    val isAtStart by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    val isAtEnd by remember {
        derivedStateOf {
            val last =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            last == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(
            items = dates,
            key = { it.toEpochDay() }
        ) { date ->
            DateItem(
                date = date,
                isSelected = date == selectedDate,
                isToday = date == LocalDate.now(),
                hasItems = hasItemsList.contains(date),
                onClick = { onDateSelected(date) }
            )
        }
    }
    LaunchedEffect(isAtStart) {
        if (isAtStart) onReachedStart()
    }

    LaunchedEffect(isAtEnd) {
        if (isAtEnd) onReachedEnd()
    }
}

@Composable
fun DateItem(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    hasItems: Boolean,
    onClick: () -> Unit
) {
    val background = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurface
    }

    val dotColor = when {
        hasItems -> MaterialTheme.colorScheme.secondary
        else -> background
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))
                .replaceFirstChar { it.uppercase() },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Button(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(containerColor = dotColor),
            onClick = onClick,
        ) {}
    }
}

@Composable
fun ProgressCardHeader(itemsCount: Int, selectedDate: LocalDate) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            color = MaterialTheme.colorScheme.onBackground,
            text = "${
                selectedDate.dayOfWeek
                    .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))
                    .replaceFirstChar { it.uppercase() }
            }, " +
                    "${selectedDate.dayOfMonth} " +
                    "${
                        selectedDate.month
                            .getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))
                    }",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = "$itemsCount товаров",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,

            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun ProgressCard(active: Int, reduced: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        val total = active + reduced

        val progressFraction = if (total == 0) {
            0f
        } else {
            reduced.toFloat() / total.toFloat()
        }

        val progressPercent = (progressFraction * 100).toInt()

        Text(
            text = "Прогресс",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp)
        )
        Text(
            text = "$progressPercent%",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
        ) //
        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            gapSize = 0.dp,
            progress = { progressFraction },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .height(5.dp)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Куплено: $reduced",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            Text(
                text = "Осталось: $active",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ShopItemHeader(onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Нужно купить")
        Button(
            contentPadding = ButtonDefaults.TextButtonContentPadding,
            onClick = onClick
        ) {
            Text(
                text = "Выбрать все",
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun BaseShopItem(
    item: ShopItem,
    containerColor: Color,
    contentColor: Color,
    textDecoration: TextDecoration,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.checked,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Transparent,
                    uncheckedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.secondary
                ),
                onCheckedChange = onCheckedChange
            )
            Text(
                text = item.title,
                color = contentColor,
                textDecoration = textDecoration
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "imageInShopItem",
                tint = contentColor,
                modifier = Modifier.padding(end = 1.dp)
            )
        }
    }
}

@Composable
fun ActiveShopItem(
    item: ShopItem,
    onCheckedChange: (Boolean) -> Unit
) {
    BaseShopItem(
        item = item,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        textDecoration = TextDecoration.None,
        onCheckedChange = onCheckedChange
    )
}

@Composable
fun ReducedShopItem(
    item: ShopItem,
    onCheckedChange: (Boolean) -> Unit
) {
    BaseShopItem(
        item = item,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        textDecoration = TextDecoration.LineThrough,
        onCheckedChange = onCheckedChange
    )
}

@Composable
fun AddToShopList(onClick: (String) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(35),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            var text by rememberSaveable { mutableStateOf("") }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                value = text,
                placeholder = { Text("Добавьте продукт") },
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth()
            )
            val isValid = text.isNotBlank()
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp),
                enabled = isValid,
                onClick = {
                    onClick(text)
                    text = ""
                }) {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = "Add",
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun ShopItemPreview() {
    MyCalendarTheme {
        ShopListScreen(viewModel = ShopPlannerViewModel().apply {
            fakeItemList()
        })
    }
}