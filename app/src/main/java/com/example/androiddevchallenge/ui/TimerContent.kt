/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.components.DEFAULT_TIMER_OPTION
import com.example.androiddevchallenge.ui.components.StartButton
import com.example.androiddevchallenge.ui.components.StopButton
import com.example.androiddevchallenge.ui.components.TimeChooser
import com.example.androiddevchallenge.ui.components.TimerOption
import com.example.androiddevchallenge.ui.components.TimerView
import com.example.androiddevchallenge.ui.theme.red500
import java.util.Timer
import java.util.TimerTask

enum class TimerState {
    IDLE, START, PAUSE
}

enum class BlinkState {
    NORMAL, BLINKED
}

@ExperimentalAnimationApi
@Composable
fun TimerContent(viewModel: TimerViewModel = viewModel()) {
    val timerState by viewModel.timerState.observeAsState()
    val timerValue by viewModel.timer.observeAsState()
    val timerOption by viewModel.timerOption.observeAsState(DEFAULT_TIMER_OPTION)

    val blinkState = remember { mutableStateOf(BlinkState.NORMAL) }
    val blinkColor = when (blinkState.value) {
        BlinkState.NORMAL -> MaterialTheme.colors.background
        BlinkState.BLINKED -> red500
    }

    val background by animateColorAsState(
        targetValue = blinkColor,
        animationSpec = repeatable(10, animation = tween(500)),
        finishedListener = {
            blinkState.value = when (blinkState.value) {
                BlinkState.NORMAL -> BlinkState.BLINKED
                BlinkState.BLINKED -> BlinkState.NORMAL
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (timerValue!! < 5) background else MaterialTheme.colors.background),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TimerView(timerOption = timerOption, timerValue = timerValue!!, timerState = timerState!!)
        TimeChooser(timerState = timerState!!) { viewModel.updateTimerOption(it) }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StartButton(timerState = timerState!!) {
                viewModel.updateTimerState(it)
                if (it == TimerState.START) {
                    viewModel.startTimer()
                } else {
                    viewModel.pauseTimer()
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            StopButton(visible = timerState != TimerState.IDLE) {
                viewModel.updateTimerState(TimerState.IDLE)
                viewModel.resetTimer()
            }
        }
    }
}

class TimerViewModel : ViewModel() {
    private val _timerState = MutableLiveData(TimerState.IDLE)
    val timerState: LiveData<TimerState> = _timerState

    private val _timerValue = MutableLiveData(DEFAULT_TIMER_OPTION.time)
    val timer: LiveData<Int> = _timerValue

    private val _timerOption = MutableLiveData(DEFAULT_TIMER_OPTION)
    val timerOption: LiveData<TimerOption> = _timerOption

    private var timerJob: Timer? = null

    fun updateTimerState(state: TimerState) {
        this._timerState.value = state
    }

    fun updateTimerOption(timerOption: TimerOption) {
        this._timerOption.value = timerOption
        this._timerValue.value = timerOption.time
    }

    fun startTimer() {
        if (timerJob == null) {
            timerJob = Timer()
        }
        schedule()
    }

    private fun schedule() {
        timerJob!!.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    if (_timerValue.value!! <= 0) {
                        resetTimer()
                        return
                    }
                    _timerValue.postValue(_timerValue.value!! - 1)
                }
            },
            0, 1000
        )
    }

    fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resetTimer() {
        pauseTimer()
        _timerValue.postValue(_timerOption.value!!.time)
        _timerState.postValue(TimerState.IDLE)
    }

    override fun onCleared() {
        resetTimer()
    }
}
