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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * @ProjectName: AndroidDevChallenge
 * @Package: com.example.androiddevchallenge
 * @ClassName: CountdownViewModel
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/6 17:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/6 17:45
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class CountdownViewModel : ViewModel() {
    private val _hour = MutableLiveData(0L)
    private val _minute = MutableLiveData(0L)
    private val _second = MutableLiveData(0L)

    private val _isCountdown = MutableLiveData(false)
    private val _buttonText = MutableLiveData("start")

    val hour: LiveData<Long> = _hour
    val minute: LiveData<Long> = _minute
    val second: LiveData<Long> = _second
    val isCountdown: LiveData<Boolean> = _isCountdown

    val buttonText: LiveData<String> = _buttonText

    fun onHourChanged(newHour: Long) {
        _hour.value = newHour
    }

    fun onHourPlus() {
        _hour.value = _hour.value?.plus(1)
    }

    fun onHourMinus() {
        _hour.value = _hour.value?.minus(1)
    }

    fun onMinutePlus() {
        _minute.value = _minute.value?.plus(1)
    }

    fun onMinuteMinus() {
        _minute.value = _minute.value?.minus(1)
    }

    fun onSecondPlus() {
        _second.value = _second.value?.plus(1)
    }

    fun onSecondMinus() {
        _second.value = _second.value?.minus(1)
    }

    fun getTime(): String {
        return "${_hour.value}:${_minute.value}:${_second.value}"
    }

    fun getCountdownTime(): Long {
        return (
            (_hour.value?.times(3600) ?: 0) +
                (_minute.value?.times(60) ?: 0) +
                (_second.value ?: 0L)
            ) * 1000
    }

    fun updateHour(hour: Long) {
        _hour.value = hour
    }

    fun updateMinute(minute: Long) {
        _minute.value = minute
    }

    fun updateSecond(second: Long) {
        _second.value = second
    }

    fun updateStatus(isCountdown: Boolean?) {
        this._isCountdown.value = isCountdown
        if (isCountdown == true) {
            _buttonText.value = "cancel"
        } else {
            _buttonText.value = "start"
        }
    }
}
