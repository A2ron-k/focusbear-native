package com.example.focusbear

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset


class ProfilePage {
}
    @Composable
    fun Profile(usersDatabaseHelper: UsersDatabaseHelper,focusSessionDatabaseHelper: FocusSessionDatabaseHelper) {
        var showStatistics by remember { mutableStateOf(false) }

        // Get user and currency
        val user = usersDatabaseHelper.getUserByID(1)
        val sessionHistory = focusSessionDatabaseHelper.getAllFocusSessions()

        Log.d("UserInformation", "ID: ${user.id}")
        Log.d("UserInformation", "Username: ${user.username}")
        Log.d("UserInformation", "Currency: ${user.currency}")
        Log.d("UserInformation", "Failed Session Count: ${user.failedSessionCount}")
        Log.d("UserInformation", "Total Session Count: ${user.totalSessionCount}")
        Log.d("UserInformation", "Total Time Focused: ${user.totalTimeFocused}")
        Log.d("UserInformation", "Total Consecutive Count: ${user.totalConsecutiveCount}")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // user icon
            Image(
                painter = painterResource(id = R.drawable.slime),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // username
            Text(text = user.username)

            ProfileStatItem(label = "${user.totalTimeFocused} Total Hours Focused")
            ProfileStatItem(label = "${user.failedSessionCount} Failed Sessions")
            ProfileStatItem(label = "${user.totalConsecutiveCount} Focus Streak")

            // Statistics page
            Button(
                onClick = { showStatistics = !showStatistics },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Statistics")
            }
            if (showStatistics) {
                StatisticsPage(
                    user = user,
                    onClose = { showStatistics = false }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // history
            Text(text = "Session History")
            sessionHistory.forEach { session ->
                SessionHistoryItem(
                    date = session.date.toString(),
                    time = session.timeFocused.toString(),
                )
            }
        }
    }

    @Composable
    fun ProfileStatItem(label: String) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Icon(Icons.Default.Info, contentDescription = null)
            //Spacer(modifier = Modifier.width(8.dp))
            Text(text = label)
        }
    }

/* trying to change into minute, fail now
fun convertMillisecondsToMinutes(milliseconds: Long): String  {
    val minutes = milliseconds.toDouble() / (1000 * 60)
    return "%.2f".format(minutes)
}*/

fun formatTime(time: String): String {
    val timeInSeconds = time.toDouble() / 1000.0
    return "%.1f".format(timeInSeconds)
}

    @Composable
    fun SessionHistoryItem(date: String, time: String) {
        /* minutes part, fail now
        val timeInMilliseconds = time.toLongOrNull() ?: 0
        val timeInMinutes = convertMillisecondsToMinutes(timeInMilliseconds)
        val formattedTime = "%d minutes".format(timeInMinutes)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            //elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Date: $date")
                Text("Time: $formattedTime")
            }
        }*/
        val formattedTime = formatTime(time)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("FocusDate: $date")
                Text("FocusTime: $formattedTime"+"s")
            }
        }
    }

@Composable
fun StatisticsPage(user: User, onClose: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Daily Statistics")
        BarChart(userStatistics = user)
        Button(
            onClick = { onClose() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Close")
        }
    }
}

@Composable
fun BarChart(userStatistics: User) {
    val focusHoursPerDay = listOf(7f, 6f, 8f, 10f, 4f, 3f, 6f, 9f, 5f, 6f, 8f, 4f, 12f)
    val maxHours = focusHoursPerDay.maxOrNull() ?: 1f
    val barWidth = 30f

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(16.dp)
    ) {
        val barSpacing = (size.width - (focusHoursPerDay.size * barWidth)) / (focusHoursPerDay.size + 1)
        var startOffset = barSpacing

        focusHoursPerDay.forEach { hours ->
            val barHeight = hours / maxHours * size.height
            drawRoundRect(
                color = Color(0xFF3F51B5),
                topLeft = Offset(x = startOffset, y = size.height - barHeight),
                size = androidx.compose.ui.geometry.Size(width = barWidth, height = barHeight),
                cornerRadius = CornerRadius(5f)
            )
            startOffset += barWidth + barSpacing
        }
    }
}


