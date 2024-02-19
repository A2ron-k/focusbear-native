package com.example.focusbear

import RewardDatabaseHelper
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.size
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.NavController

class MainActivity : ComponentActivity() {

    // Create database helper to access the users & focusSessions db
    private lateinit var usersDatabaseHelper: UsersDatabaseHelper
    private lateinit var focusSessionDatabaseHelper: FocusSessionDatabaseHelper
    private lateinit var rewardDatabaseHelper: RewardDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise userdatabasehelper & focusSessiondatabasehelper
        usersDatabaseHelper = UsersDatabaseHelper(this)
        focusSessionDatabaseHelper = FocusSessionDatabaseHelper(this)
        rewardDatabaseHelper = RewardDatabaseHelper(this)

        setContent {
            FocusBearTheme {
                val navController = rememberNavController()
                Box(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(navController = navController, startDestination = "Homepage") {
                            composable("Homepage") {
                                Timer(
                                    usersDatabaseHelper,
                                    focusSessionDatabaseHelper,
                                    rewardDatabaseHelper,
                                    navController
                                )
                            }
                            composable("Profile") {
                                Profile()
                            }
                            composable("Desk") {
                                Gallery(navController, rewardDatabaseHelper)
                            }
                            composable("Shop") {
                                //Shop()
                            }
                        }
                        BottomNavigationBar(navController, rewardDatabaseHelper)
                    }
                }
            }
            /*setContent {
            FocusBearTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    //Timer(usersDatabaseHelper, focusSessionDatabaseHelper)
                    NavHost(navController = navController, startDestination = "timer") {
                        composable("timer") {
                            Timer(usersDatabaseHelper, focusSessionDatabaseHelper, navController)
                        }
                        composable("introduction") {
                            ProfileScreen()
                        }
                    }
                }
            }
        }*/
        }
    }

    @Composable
    fun BottomNavigationBar(
        navController: NavController,
        rewardDatabaseHelper: RewardDatabaseHelper
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                //horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home1),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { navController.navigate("Homepage") }
                        .size(48.dp)
                    //contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.gallery1),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { navController.navigate("Desk") }
                        .size(48.dp)
                    //contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.profile1),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { navController.navigate("Profile") }
                        .size(48.dp)
                    //contentScale = ContentScale.Crop
                )
            }
        }
    }

    // Add the databasehelpers into params when u need to access them
    @Composable
    fun Timer(
        usersDatabaseHelper: UsersDatabaseHelper,
        focusSessionDatabaseHelper: FocusSessionDatabaseHelper,
        rewardDatabaseHelper: RewardDatabaseHelper,
        navController: NavController
    ) {
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
                        val rewarD = Reward(
                            name = "Slime",
                            description = "Just a pink slime.",
                            imageResourceId = R.drawable.slime
                        )

                        // Attempt to add the reward to the database
                        try {
                            rewardDatabaseHelper.addReward(rewarD)
                            Log.d("Reward", "Reward added successfully.")
                            Log.d("Reward", "Image Resource ID: ${rewarD.imageResourceId}")
                        } catch (e: Exception) {
                            Log.e("Reward", "Error adding reward: ${e.message}")
                        }
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


    fun testdb(
        usersDatabaseHelper: UsersDatabaseHelper,
        focusSessionDatabaseHelper: FocusSessionDatabaseHelper
    ) {
        // Create a new user
        val newUser = User(
            id = 1,
            username = "john_doe",
            currency = 100,
            failedSessionCount = 0,
            totalSessionCount = 5,
            totalTimeFocused = 10000,
            totalConsecutiveCount = 2
        )
        usersDatabaseHelper.createUser(newUser)

        // Retrieve all users
        val userList = usersDatabaseHelper.getAllUsers()
//    for (user in userList) {
        // Do something with each user
//    }

        // Update a user
        val updatedUser = userList.firstOrNull()
        updatedUser?.let {
            it.currency += 50
            usersDatabaseHelper.updateUser(it)
        }

        // Delete a user
        val userToDelete = userList.firstOrNull()
        userToDelete?.let {
            usersDatabaseHelper.deleteUser(it.id)
        }

        // Create a new focus session
        val newFocusSession = FocusSession(
            id = 1,
            timeFocused = 10000,
            date = System.currentTimeMillis(),
        )
        focusSessionDatabaseHelper.createFocusSession(newFocusSession)

        // Retrieve all focus sessions
        val focusSessionList = focusSessionDatabaseHelper.getAllFocusSessions()
//    Loop through focusSessions - can be used in profile page to load all the sessions
//    for (focusSession in focusSessionList) {
        // Do something with each focus session
//    }

        // Update a focus session
        val updatedFocusSession = focusSessionList.firstOrNull()
        updatedFocusSession?.let {
            it.timeFocused += 5000
            focusSessionDatabaseHelper.updateFocusSession(it)
        }

        // Delete a focus session
        val focusSessionToDelete = focusSessionList.firstOrNull()
        focusSessionToDelete?.let {
            focusSessionDatabaseHelper.deleteFocusSession(it.id)
        }
    }
}

