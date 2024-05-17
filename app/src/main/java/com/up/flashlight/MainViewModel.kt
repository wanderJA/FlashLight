package com.up.flashlight

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Copyright (c) 2024. All rights reserved.
 * 类功能描述:
 *
 * @author wangdou
 * @date 2024/5/7
 */
class MainViewModel : ViewModel() {
    var delayTime: Long = 0L
    private var job: Job? = null
    val delayLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val openState: LiveData<Boolean> = delayLiveData
    val remainingTimeState = mutableStateOf("00:00")
    var sliderPosition = mutableIntStateOf(60)
    var lightOpen = mutableStateOf(false)

    fun startDelay() {
        job?.cancel()
        job = GlobalScope.launch {
            val times = (delayTime * 60).toInt()
            repeat(times) {
                val remaining = times - it
                sliderPosition.value = remaining / 60
                remainingTimeState.value =
                    "${(remaining / 60).formatIntTwo()}:${(remaining % 60).formatIntTwo()}"
                delay(1000)
            }
            withContext(Dispatchers.Main) {
                delayLiveData.value = null
                remainingTimeState.value = "00:00"
                lightOpen.value = false
            }
        }
    }

    fun stopDelay() {
        job?.cancel()
        remainingTimeState.value = "00:00"
        lightOpen.value = false
    }

    /**
     * 将int转为两位数
     */
    private fun Int.formatIntTwo(): String {
        return (if (this < 10) "0" else "") + this
    }


}
