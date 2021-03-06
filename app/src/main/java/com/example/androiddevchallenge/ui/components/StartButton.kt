package com.example.androiddevchallenge.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.TimerState
import com.example.androiddevchallenge.util.IDLE_BUTTON_WIDTH
import com.example.androiddevchallenge.util.STARTED_BUTTON_WIDTH

@Composable
fun StartButton(timerState: TimerState, onClick: (TimerState) -> Unit) {
    // size anim
    val targetValue = when (timerState) {
        TimerState.START -> STARTED_BUTTON_WIDTH
        else -> IDLE_BUTTON_WIDTH
    }
    val animState: Dp by animateDpAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 700),
    )
    OutlinedButton(
        onClick = {
            val newState: TimerState = if (timerState == TimerState.START) {
                TimerState.PAUSE
            } else {
                TimerState.START
            }
            onClick(newState)
        },
        modifier = Modifier.size(animState, STARTED_BUTTON_WIDTH),
        border = BorderStroke(4.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(percent = 50),
    ) {
        StartButtonContent(timerState = timerState, timerState)
    }
}

@Composable
fun StartButtonContent(timerState: TimerState, state: TimerState) {
    Crossfade(targetState = state, animationSpec = tween(durationMillis = 300, delayMillis = 100)) {
        when (state) {
            TimerState.START -> StartedStateContent()
            else -> IdleStateContent(timerState = timerState)
        }
    }
}

@Composable
fun StartedStateContent() {
    Icon(
        imageVector = Icons.Default.Pause,
        contentDescription = "Pause"
    )
}

@Composable
fun IdleStateContent(timerState: TimerState) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Start"
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = if (timerState == TimerState.PAUSE) "Resume" else "Start")
    }
}

@Preview
@Composable
fun StartButtonPreview() {
    StartButton(timerState = TimerState.IDLE) {}
}