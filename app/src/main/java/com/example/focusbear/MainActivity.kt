package com.example.focusbear

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

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

@Composable
fun Timer() {
    var time by remember {
        mutableLongStateOf(0L)
    }

    // Timer clock boolean status (running)
    var isStarted by remember {
        mutableStateOf(false)
    }

    // Timer clock boolean status (stopped)
    var isStopped by remember {
        mutableStateOf(false)
    }

    var isReward by remember {
        mutableStateOf(false)
    }

    var startTime by remember {
        mutableLongStateOf(0L)
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TODO - Update the time condition after development is done
        if (isReward && time > 5000) {
            // TODO - Replace the text field with an image
            Text(text = "hi")
        }

        if (isReward && time < 5000) {
            // TODO - Replace the text field with an image
            Text(text = "ded")
        }

        Text(
            text = formatMS(timeInMS = time),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(10.dp)
        )

        Spacer(modifier = Modifier.padding(24.dp))

        Row {
            if (isStarted && isStopped) {
                // This part shows the reset
                Button(onClick = {
                    time = 0
                    isStarted = false
                    isStopped = false
                    isReward = false
                    startTime = 0 // Reset start time
                }, modifier = Modifier.weight(1f)) {
                    Text(text = "Reset")
                }
            } else if (isStarted && !isStopped) {
                // This part shows the stop
                Button(onClick = {
                    isStopped = true // Only mark as stopped
                    isReward = true

                    // Check if elapsed time is greater than 15 minutes (900,000 milliseconds)
                    // TODO - Update the time condition after development is done
                    if (time > 5000) {
                        // Display success message
                        Toast.makeText(
                            context,
                            "Success! You focused for over 15 minutes.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Display fail message
                        Toast.makeText(
                            context,
                            "Fail! You focused for less than 15 minutes.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, modifier = Modifier.weight(1f)) {
                    Text(text = "Stop")
                }
            } else {
                // This part shows the start
                Button(onClick = {
                    isStarted = true
                    isStopped = false
                    startTime = System.currentTimeMillis() // Set start time to current time
                }, modifier = Modifier.weight(1f)) {
                    Text(text = "Start")
                }
            }
        }
    }

    LaunchedEffect(isStarted && !isStopped) {
        while (isStarted && !isStopped) {
            delay(1000)
            time = System.currentTimeMillis() - startTime
        }
    }
}


@Composable
fun formatMS(timeInMS: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(timeInMS)
    val mins = TimeUnit.MILLISECONDS.toMinutes(timeInMS) % 60
    val secs = TimeUnit.MILLISECONDS.toSeconds(timeInMS) % 60

    return String.format("%02d:%02d:%02d", hours, mins, secs)

}