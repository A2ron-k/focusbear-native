package com.example.focusbear

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
    fun Profile() {
        var showStatistics by remember { mutableStateOf(false) }
        val user = User(
            id = 1,
            username = "I'm Egg",
            currency = 100,
            failedSessionCount = 3,
            totalSessionCount = 5,
            totalTimeFocused = 666,
            totalConsecutiveCount = 2
        )

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

            // user position
            Text(text = "Student")


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
            SessionHistoryItem(date = "01/01/2024", startTime = "10:00 AM", endTime = "12:00 PM")
            SessionHistoryItem(date = "02/01/2024", startTime = "02:00 PM", endTime = "04:00 PM")
            SessionHistoryItem(date = "19/02/2024", startTime = "09:00 PM", endTime = "11:00 PM")
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

    @Composable
    fun SessionHistoryItem(date: String, startTime: String, endTime: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            //elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(date)
                Text("$startTime - $endTime")
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


/*
//connect to database
fun Profile(usersDatabaseHelper: UsersDatabaseHelper, userId: Int) {
    // 使用remember来观察数据库查询的结果
    val user = remember { mutableStateOf<User?>(null) }

    // 在LaunchedEffect中异步加载用户数据
    LaunchedEffect(key1 = userId) {
        user.value = usersDatabaseHelper.getUserByID(userId)
    }

    // 如果用户数据不为空，展示在UI上
    user.value?.let { currentUser ->
        ProfileContent(user = currentUser)
    }
}

@Composable
fun ProfileContent(user: User) {
    Column(modifier = Modifier.padding(16.dp)) {
        // 显示用户头像，此处假设您有头像图片资源
        Image(
            painter = painterResource(id = R.drawable.egg),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 显示用户名
        Text(text = user.username)

        // 显示用户其他信息
        Text(text = "Total Hours Focused: ${user.totalTimeFocused}")
        Text(text = "Failed Session Count: ${user.failedSessionCount}")
        Text(text = "Total Session Count: ${user.totalSessionCount}")
        Text(text = "Total Consecutive Count: ${user.totalConsecutiveCount}")

        // 其他用户信息和操作
    }
}
*/