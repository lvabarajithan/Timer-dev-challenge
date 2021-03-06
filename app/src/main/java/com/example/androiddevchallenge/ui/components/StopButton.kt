package com.example.androiddevchallenge.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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