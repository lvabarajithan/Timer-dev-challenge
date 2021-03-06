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
package com.example.androiddevchallenge.ui.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.TimerState
import com.example.androiddevchallenge.ui.theme.digital

@Composable
fun TimerView(timerOption: TimerOption, timerValue: Int, timerState: TimerState) {
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(size = 24.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(24.dp)) {
            AnimatedProgress(
                timerOption = timerOption,
                timerValue = timerValue,
                timerState = timerState
            )
            AnimatedTimerText(
                timerValue = timerValue,
                timerState = timerState
            )
        }
    }
}

@Composable
fun AnimatedProgress(timerOption: TimerOption, timerValue: Int, timerState: TimerState) {
    val progress =
        if (timerState == TimerState.IDLE) 1f else timerValue.toFloat() / timerOption.time
    val progressVal: Float by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 200)
    )
    CircularProgressIndicator(
        progress = progressVal,
        modifier = Modifier.size(200.dp),
        strokeWidth = 8.dp
    )
}

@Composable
fun AnimatedTimerText(timerValue: Int, timerState: TimerState) {
    if (timerState != TimerState.START) {
        Text(
            text = "$timerValue",
            style = MaterialTheme.typography.h1,
            fontWeight = FontWeight.Medium,
            fontFamily = digital,
        )
        return
    }

    val transition = rememberInfiniteTransition()
    val animSpec = infiniteRepeatable(animation = tween<Float>(durationMillis = 985))
    val textUnit: Float by transition.animateFloat(
        initialValue = 0f,
        targetValue = MaterialTheme.typography.h1.fontSize.value,
        animationSpec = animSpec
    )

    Text(
        text = "$timerValue",
        style = MaterialTheme.typography.h1,
        fontWeight = FontWeight.Medium,
        fontSize = textUnit.sp,
        fontFamily = digital
    )
}
