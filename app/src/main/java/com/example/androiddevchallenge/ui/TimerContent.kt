package com.example.androiddevchallenge.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
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
import com.example.androiddevchallenge.ui.components.StartButton
import com.example.androiddevchallenge.ui.components.TimerView
import com.example.androiddevchallenge.ui.theme.red500
import com.example.androiddevchallenge.util.DEFAULT_TIMER_VALUE
import com.example.androiddevchallenge.util.IDLE_BUTTON_WIDTH
import com.example.androiddevchallenge.util.STARTED_BUTTON_WIDTH
import java.util.*

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
        TimerView(timerValue!!, timerState!!)
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

@ExperimentalAnimationApi
@Composable
fun StopButton(visible: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(visible = visible) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(percent = 50),
            border = BorderStroke(4.dp, MaterialTheme.colors.secondary),
            modifier = Modifier
                .height(STARTED_BUTTON_WIDTH)
                .width(IDLE_BUTTON_WIDTH),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colors.secondary,
                backgroundColor = MaterialTheme.colors.background
            )
        ) {
            Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop")
            Text(text = "Stop")
        }
    }
}


class TimerViewModel : ViewModel() {
    private val _timerState = MutableLiveData(TimerState.IDLE)
    val timerState: LiveData<TimerState> = _timerState

    private val _timerValue = MutableLiveData(DEFAULT_TIMER_VALUE)
    val timer: LiveData<Int> = _timerValue

    private var timerJob: Timer? = null

    fun updateTimerState(state: TimerState) {
        this._timerState.value = state
    }

    fun startTimer() {
        if (timerJob == null) {
            timerJob = Timer()
        }
        schedule()
    }

    private fun schedule() {
        timerJob!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (_timerValue.value!! <= 0) {
                    resetTimer()
                    return
                }
                _timerValue.postValue(_timerValue.value!! - 1)
            }
        }, 0, 1000)
    }

    fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resetTimer() {
        pauseTimer()
        _timerValue.postValue(DEFAULT_TIMER_VALUE)
        _timerState.postValue(TimerState.IDLE)
    }

    override fun onCleared() {
        resetTimer()
    }
}