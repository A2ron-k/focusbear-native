package com.example.focusbear

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.focusbear.ui.theme.FocusBearTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FocusBearTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
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

    // Flag used to check if the timer has started
    var isStarted by remember {
        mutableStateOf(false)
    }

    // Flag used to check if the timer has stopped
    var isStopped by remember {
        mutableStateOf(false)
    }

    // Flag used to check if the user is in a round/focus session
    var isOngoingRound by remember {
        mutableStateOf(false)
    }

    // Flag used to check if the user has an award pending
    var isReward by remember {
        mutableStateOf(false)
    }

    var startTime by remember {
        mutableLongStateOf(0L)
    }

    var reward by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = formatMS(timeInMS = time),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.LightGray.copy(alpha = 0.6f))
                    .padding(8.dp)
            )

            // TODO - Update the time condition after development is done
            if (isOngoingRound && time > 5000) {
                if (reward != "") {
                    Text(text = reward)
                    val painter = painterResource(id = R.drawable.slime)
                    Image(painter = painter, contentDescription = null)
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.egg_hatched),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }


            } else if (isOngoingRound && time < 5000) {
                Image(
                    painter = painterResource(id = R.drawable.egg_dead_1),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Spacer(modifier = Modifier.padding(24.dp))
                Image(
                    painter = painterResource(id = R.drawable.egg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.padding(0.dp))

            Row {
                if (isStarted && isStopped) {
                    // This part shows the reset
                    if (isReward) {
                        if (reward != "") {
                            Image(
                                painter = painterResource(id = R.drawable.reward_speech),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        time = 0
                                        isStarted = false
                                        isStopped = false
                                        isOngoingRound = false
                                        isReward = false
                                        startTime = 0 // Reset start time
                                        reward = ""
                                    }
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.big_button_claim),
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    reward = randomReward()
                                },
                                contentScale = ContentScale.FillWidth,
                            )
                        }

                    } else {
                        Button(onClick = {
                            time = 0
                            isStarted = false
                            isStopped = false
                            isOngoingRound = false
                            isReward = false
                            startTime = 0 // Reset start time
                        }, modifier = Modifier.weight(1f)) {
                            Text(text = "Reset")
                        }
                    }

                } else if (isStarted && !isStopped) {
                    // This part shows the stop

                    Image(
                        painter = painterResource(id = R.drawable.big_button_stop),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            isStopped = true // Only mark as stopped
                            isOngoingRound = true

                            // TODO - Update the time condition after development is done
                            // Check if elapsed time is greater than 15 minutes (900,000 milliseconds)
                            if (time > 5000) {
                                // Display success message
                                isReward = true
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

                        },
                        contentScale = ContentScale.FillWidth,
                    )
                } else {
                    // This part shows the start
                    Image(
                        painter = painterResource(id = R.drawable.big_button_start),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            isStarted = true; isStopped = false; startTime =
                            System.currentTimeMillis()
                        },
                        contentScale = ContentScale.FillWidth,
                    )
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

fun randomReward(): String {
    // TODO - Add reward logic here
    var reward = Random.nextInt(1, 15).toString()
    return reward

}