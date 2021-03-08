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
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.MainActivity.Companion.TIME_TYPE_HOUR
import com.example.androiddevchallenge.MainActivity.Companion.TIME_TYPE_MINUTE
import com.example.androiddevchallenge.MainActivity.Companion.TIME_TYPE_SECOND
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<CountdownViewModel>()

    companion object {
        const val TIME_TYPE_HOUR = 0
        const val TIME_TYPE_MINUTE = 1
        const val TIME_TYPE_SECOND = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
                MyApp(viewModel)
            }
        }
    }
}

var countDownTimer: CountDownTimer? = null

// Start building your app here!
@Composable
fun MyApp(viewModel: CountdownViewModel) {

    val isCountdown: Boolean by viewModel.isCountdown.observeAsState(false)
    val backgroundShape: Shape = RoundedCornerShape(4.dp)

    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CountdownViews(viewModel)

            val buttonText: String by viewModel.buttonText.observeAsState("start")

            Text(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                text = buttonText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 20.dp, 10.dp, 0.dp)
                    .shadow(3.dp, shape = backgroundShape)
                    .clip(backgroundShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xff00ff),
                                Color.Blue,
                                Color(0xff00ff),
                            ),
                            radius = 900f
                        )
                    )
                    .clickable {
                        val time = viewModel.getCountdownTime()
                        if (isCountdown) {
                            countDownTimer?.cancel()
                            viewModel.updateStatus(isCountdown = false)
                            return@clickable
                        }

                        if (time < 1000) return@clickable

                        countDown(viewModel)
                        viewModel.updateStatus(isCountdown = true)
                    }
            )
        }
    }
}

@Composable
private fun CountdownViews(viewModel: CountdownViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(10.dp, 0.dp, 0.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OnTimeItem(viewModel, TIME_TYPE_HOUR)
        }
        Text(
            modifier = Modifier
                .weight(0.2f),
            fontSize = 50.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            text = ":",
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OnTimeItem(viewModel, TIME_TYPE_MINUTE)
        }
        Text(
            modifier = Modifier
                .weight(0.2f),
            fontSize = 50.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            text = ":",
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 0.dp, 10.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OnTimeItem(viewModel, TIME_TYPE_SECOND)
        }
    }
}

/**
 * 倒计时
 */
private fun countDown(viewModel: CountdownViewModel) {
    val time = viewModel.getCountdownTime()
    countDownTimer = object : CountDownTimer(time, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val leftSecond: Long = (millisUntilFinished / 1000) + 1
            val h = leftSecond / 3600
            val m = leftSecond % 3600 / 60
            val s = leftSecond % 3600 % 60

            viewModel.updateHour(h)
            viewModel.updateMinute(m)
            viewModel.updateSecond(s)
        }

        override fun onFinish() {
            viewModel.updateStatus(isCountdown = false)
            viewModel.updateSecond(0L)
        }
    }
    countDownTimer?.start()
}

@Composable
fun OnTimeItem(viewModel: CountdownViewModel, unit: Int) {
    val hour: Long by viewModel.hour.observeAsState(0L)
    val minute: Long by viewModel.minute.observeAsState(0L)
    val second: Long by viewModel.second.observeAsState(0L)
    val isCountdown: Boolean by viewModel.isCountdown.observeAsState(false)

    val timeLimit = when (unit) {
        0 -> 23
        1 -> 59
        2 -> 59
        else -> 59
    }

    val time = when (unit) {
        0 -> hour
        1 -> minute
        2 -> second
        else -> second
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PlusButton(isCountdown, time, timeLimit, unit, viewModel)

        Text(
            text = time.toString(),
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold
        )

        MinusButton(isCountdown, time, unit, viewModel)
    }
}

/**
 * 加 按钮
 */
@Composable
private fun PlusButton(
    isCountdown: Boolean,
    time: Long,
    timeLimit: Int,
    unit: Int,
    viewModel: CountdownViewModel
) {
    Button(
        enabled = !isCountdown,
        onClick = {
            if (isCountdown) {
                return@Button
            }
            if (time < timeLimit) {
                when (unit) {
                    0 -> viewModel.onHourPlus()
                    1 -> viewModel.onMinutePlus()
                    2 -> viewModel.onSecondPlus()
                }
            }
        }
    ) {
        Text(
            text = "+",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

/**
 * 减 按钮
 */
@Composable
private fun MinusButton(
    isCountdown: Boolean,
    time: Long,
    unit: Int,
    viewModel: CountdownViewModel
) {
    Button(
        enabled = !isCountdown,
        onClick = {
            if (isCountdown) {
                return@Button
            }
            if (time > 0) {
                when (unit) {
                    0 -> viewModel.onHourMinus()
                    1 -> viewModel.onMinuteMinus()
                    2 -> viewModel.onSecondMinus()
                }
            }
        }
    ) {
        Text(
            text = "-",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(CountdownViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(CountdownViewModel())
    }
}
