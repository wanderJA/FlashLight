package com.up.flashlight

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
    val openLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val openState: LiveData<Boolean> = openLiveData

    fun startDelay() {
        job?.cancel()
        job = GlobalScope.launch {
                delay(delayTime * 60 * 1000)
//            delay(delayTime * 100)
            withContext(Dispatchers.Main) {
                openLiveData.value = null
            }
        }
    }


}
