package com.example.focusbear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.focusbear.ui.theme.FocusBearTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FocusBearTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Timer()
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Timer() {
    var time by remember {
        mutableStateOf(0L)
    }

    var isStarted by remember {
        mutableStateOf(false)
    }

    var startTime by remember {
        mutableStateOf(0L)
    }

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = formatMilisec(timeInMilisec = time),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(10.dp)
        )

        Spacer(modifier = Modifier.padding(24.dp))

        Row {
            Button(onClick = {
                if (isStarted) {
                    isStarted = false
                } else {
                    startTime = System.currentTimeMillis() - time
                    isStarted = true
                    keyboardController?.hide()
                }
            }, modifier = Modifier.weight(1f)) {

                Text(text = if (isStarted) "Pause" else "Start", color = Color.White)

            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { time = 0; isStarted = false }, modifier = Modifier.weight(1f)) {
                Text(text = "Reset")
            }
        }
    }

    LaunchedEffect(isStarted) {
        while (isStarted) {
            delay(1000)
            time = System.currentTimeMillis() - startTime
        }
    }
}


@Composable
fun formatMilisec(timeInMilisec: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(timeInMilisec)
    val mins = TimeUnit.MILLISECONDS.toMinutes(timeInMilisec) % 60
    val secs = TimeUnit.MILLISECONDS.toSeconds(timeInMilisec) % 60

    return String.format("%02d:%02d:%02d", hours, mins, secs)


}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    FocusBearTheme {
//        Greeting("Android")
//    }
//}