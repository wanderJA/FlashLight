package com.up.flashlight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.up.flashlight.ui.theme.FlashLightTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SnapshotActivity : ComponentActivity() {
    val snap = mutableStateOf("hello")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashLightTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        name = snap.value,
                        modifier = Modifier.padding(innerPadding)
                    )
                    LaunchedEffect(1000) {
                        delay(1000)
                        snap.value = "world"
                    }
                }
            }
        }

        lifecycleScope.launch {
            delay(2000)
            snap.value = "modify"
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FlashLightTheme {
        Greeting2("Android")
    }
}