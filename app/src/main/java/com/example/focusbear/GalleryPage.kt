package com.example.focusbear

import RewardDatabaseHelper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.compose.material3.Button


@Composable
fun Gallery(
    navController: NavController,
    rewardDatabaseHelper: RewardDatabaseHelper
) {
    val rewards = rewardDatabaseHelper.getAllRewards() // Fetch rewards from the database
    val reward1 = rewards.lastOrNull()
    if (reward1 != null) {
        Log.d("Reward", "Image Resource ID: ${reward1.imageResourceId}")
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.shop_button),
                contentDescription = null,
                modifier = Modifier
                    .clickable { navController.navigate("Shop") }
                    .padding(bottom = 50.dp)
            )
//            Button(
//                onClick = { navController.navigate("Shop") }, // Navigate to "Shop" page
//                modifier = Modifier.padding(bottom = 50.dp)
//            ) {
//                Text("Go to Shop")
//            }
            Spacer(modifier = Modifier.height(16.dp))
            //Display fetched rewards
            if (rewards.isNotEmpty()) {
                rewards.forEach { reward ->
                    RewardItem(reward = reward)
                }
            }
        }
    }
}


@Composable
fun RewardItem(reward: Reward) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(110.dp)
    ) {
        // Check if the reward has a valid image resource ID
        if (reward.imageResourceId > 1000) {
            Log.d("Reward", "Image Resource ID: ${reward.imageResourceId}")
            Image(
                painter = painterResource(id = reward.imageResourceId),
                contentDescription = reward.name,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}