package com.example.androiddevchallenge.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.TimerState

val timerOptions = arrayListOf(
    TimerOption("🍬", time = 10),
    TimerOption("🍫", time = 20),
    TimerOption("🍦", time = 30),
    TimerOption("🥪", time = 40),
    TimerOption("🍕", time = 50),
    TimerOption("🍜", time = 60),
)

val DEFAULT_TIMER_OPTION_INDEX = timerOptions.lastIndex - 2
val DEFAULT_TIMER_OPTION = timerOptions[DEFAULT_TIMER_OPTION_INDEX]

@ExperimentalAnimationApi
@Composable
fun TimeChooser(timerState: TimerState, onTimeOptionSelected: (TimerOption) -> Unit) {
    val activeIndex = remember { mutableStateOf(DEFAULT_TIMER_OPTION_INDEX) }

    LazyRow(modifier = Modifier.padding(horizontal = 24.dp)) {
        itemsIndexed(timerOptions) { index, item ->
            val background = if (index == activeIndex.value) {
                if (timerState == TimerState.IDLE) MaterialTheme.colors.primary else Color.LightGray
            } else MaterialTheme.colors.surface
            Card(
                modifier = Modifier.padding(8.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                elevation = 0.dp,
                backgroundColor = background
            ) {
                val modifier = when (timerState) {
                    TimerState.IDLE -> Modifier
                        .clickable { activeIndex.value = index; onTimeOptionSelected(item) }
                        .padding(8.dp)
                    else -> Modifier.padding(8.dp)
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                ) {
                    Text(text = item.icon, style = MaterialTheme.typography.h4)
                    Text(text = "${item.time}s", style = MaterialTheme.typography.h6)
                }
            }
        }
    }
}

class TimerOption(val icon: String, val time: Int)