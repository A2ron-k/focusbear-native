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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.focusbear.ui.theme.FocusBearTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    // Create database helper to access the users & focusSessions db
    private lateinit var usersDatabaseHelper: UsersDatabaseHelper
    private lateinit var focusSessionDatabaseHelper: FocusSessionDatabaseHelper
    private lateinit var rewardDatabaseHelper: RewardDatabaseHelper
    private lateinit var itemsDatabaseHelper: ItemsDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise userdatabasehelper & focusSessiondatabasehelper
        usersDatabaseHelper = UsersDatabaseHelper(this)
        focusSessionDatabaseHelper = FocusSessionDatabaseHelper(this)
        rewardDatabaseHelper = RewardDatabaseHelper(this)
        itemsDatabaseHelper = ItemsDatabaseHelper(this)

        // Create tables
        usersDatabaseHelper.createTable()
        focusSessionDatabaseHelper.createTable()
        itemsDatabaseHelper.createTable()
        rewardDatabaseHelper.createTable()

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
                                Profile(usersDatabaseHelper, focusSessionDatabaseHelper)
                            }
                            composable("Desk") {
                                Gallery(navController, rewardDatabaseHelper)
                            }
                            composable("Shop") {
                                Shop(usersDatabaseHelper)
                            }
                        }
                        BottomNavigationBar(navController, rewardDatabaseHelper)
                    }
                }
            }
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
        // Get user and currency
        val user = usersDatabaseHelper.getUserByID(1)
        val userCurrency = user.currency

        var time by rememberSaveable {
            mutableLongStateOf(0L)
        }

        // Flag used to check if the timer has started
        var isStarted by rememberSaveable {
            mutableStateOf(false)
        }

        // Flag used to check if the timer has stopped
        var isStopped by rememberSaveable { mutableStateOf(false) }


        // Flag used to check if the user is in a round/focus session
        var isOngoingRound by rememberSaveable {
            mutableStateOf(false)
        }

        // Flag used to check if the user has an award pending
        var isReward by rememberSaveable {
            mutableStateOf(false)
        }

        var startTime by rememberSaveable {
            mutableLongStateOf(0L)
        }

        var reward by rememberSaveable {
            mutableStateOf<Reward?>(null)
        }

        val context = LocalContext.current


        val lifecycleOwner = LocalLifecycleOwner.current

        // This execute code when the activity is paused
        LaunchedEffect(key1 = lifecycleOwner) {
            val lifecycle = lifecycleOwner.lifecycle
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_PAUSE) {

                    if(time > 9000){
                        isReward = true
                    }

                    isStopped = true
                    isOngoingRound = true
                }
            }
            lifecycle.addObserver(observer)
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background1),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 24.dp, top = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.coin),
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                    )
                    Text(
                        text = userCurrency.toString(),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
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

                if (isOngoingRound && time > 9000) {
                    if (reward != null) {
                        // Attempt to add the reward and time to the database
                        try {
                            reward!!.let { rewardDatabaseHelper.addReward(it) }
                            Log.d("Reward", "Reward added successfully.")
                            Log.d("Reward", "Image Resource ID: ${reward!!.imageResourceId}")
                        } catch (e: Exception) {
                            Log.e("Reward", "Error adding reward: ${e.message}")
                        }
                        val painter = painterResource(id = reward!!.getID())
                        Image(painter = painter, contentDescription = null)
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.egg_hatched),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }


                } else if (isOngoingRound && isStopped && time < 9000) {
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
                            if (reward != null) {
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
                                            reward = null
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

                            Image(
                                painter = painterResource(id = R.drawable.big_button_fail),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        time = 0
                                        isStarted = false
                                        isStopped = false
                                        isOngoingRound = false
                                        isReward = false
                                        startTime = 0 // Reset start time
                                    }
                            )
                        }

                    } else if (isStarted && !isStopped) {
                        // This part shows the stop
                        Image(
                            painter = painterResource(id = R.drawable.big_button_stop),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                isStopped = true // Only mark as stopped
                                isOngoingRound = true

                                // Check if elapsed time is greater than 15 minutes (900,000 milliseconds)
                                if (time > 9000) {
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
                                System.currentTimeMillis();
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

            if (isStopped) {
                val currentDateInMillis = Calendar.getInstance().timeInMillis
                val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val formattedDate = dateFormat.format(currentDateInMillis)
                val focusSession = FocusSession(1, time, formattedDate)
                focusSessionDatabaseHelper.createFocusSession(focusSession)
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

    fun randomReward(): Reward? {
        // Array of reward options
        val rewardOptions = arrayOf(
            Reward(
                name = "Slime",
                description = "Just a pink slime.",
                imageResourceId = R.drawable.slime
            ),
            Reward(
                name = "Mug",
                description = "A cool mug.",
                imageResourceId = R.drawable.mug
            ),
            Reward(
                name = "Books",
                description = "A stack of books.",
                imageResourceId = R.drawable.books
            ),
            Reward(
                name = "Pen Holder",
                description = "A pen holder.",
                imageResourceId = R.drawable.pen_holder
            ),
            Reward(
                name = "Cat",
                description = "A cutie cat.",
                imageResourceId = R.drawable.cat
            ),
            Reward(
                name = "Plant",
                description = "A thriving plant, try not to kill it.",
                imageResourceId = R.drawable.plant
            )
            // Add more reward options as needed
        )

        // Generate a random index within the range of the rewardOptions array
        val randomIndex = (0 until rewardOptions.size).random()
        // Return the randomly selected reward
        return rewardOptions[randomIndex]
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
            date = "20240301",
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

