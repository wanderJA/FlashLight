package com.up.flashlight

import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.up.flashlight.ui.theme.FlashLightTheme


private val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    private val cameraPermission = android.Manifest.permission.CAMERA
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashLightTheme {
                Greeting(viewModel = viewModel)
            }
        }
        checkCameraPermission {

        }
        viewModel.openLiveData.observeForever {
            toggleFlash(it ?: false)
            if (it == null) {
                finish()
            }
            if (it.not()) {
                viewModel.delayTime = 0
            }
        }


    }

    private fun toggleFlash(open: Boolean) {
        checkCameraPermission {
            // 获取CameraManager
            val cameraManager = ContextCompat.getSystemService(this, CameraManager::class.java)
            try {
                val cameraId = cameraManager!!.cameraIdList[0] // 通常使用第一个摄像头
                cameraManager.setTorchMode(cameraId, open) // 开启闪光灯
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun checkCameraPermission(onGran: () -> Unit) {
        if (checkSelfPermission(cameraPermission) != PackageManager.PERMISSION_GRANTED) {
            val permissionContract =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {

                }
            permissionContract.launch(cameraPermission)
        } else {
            onGran()
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    var sliderPosition by remember { mutableStateOf(60) }
    var open by remember { mutableStateOf(viewModel.openLiveData.value ?: false) }
    open = viewModel.delayTime != 0L
    Log.d(TAG, "Greeting:${viewModel.delayTime}")
    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "$sliderPosition 分钟")
        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = {
                Log.d("slider", "Greeting: $it")
                sliderPosition = it.toInt()
            },
            modifier = Modifier.padding(horizontal = 30.dp),
            enabled = open.not(),
            valueRange = 0f..120f,
            steps = 11
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button({
            open = open.not()
            if (open) {
                viewModel.delayTime = sliderPosition.toLong()
                viewModel.startDelay()
            }
            viewModel.openLiveData.value = open
        }) {
            Text(
                text = if (viewModel.openState.value == true) {
                    "关闭"
                } else {
                    "打开"
                },
                modifier = modifier
            ).also { Log.d(TAG, "Greeting: text :$open") }
        }

        Text(text = "open:${viewModel.openState.value}")
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlashLightTheme {
        Greeting(viewModel = MainViewModel())
    }
}