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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.util.IDLE_BUTTON_WIDTH
import com.example.androiddevchallenge.util.STARTED_BUTTON_WIDTH

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
                backgroundColor = MaterialTheme.colors.surface
            )
        ) {
            Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop")
            Text(text = "Stop")
        }
    }
}
