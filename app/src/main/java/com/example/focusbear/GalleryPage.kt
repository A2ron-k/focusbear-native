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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.compose.material3.Button
import androidx.compose.ui.tooling.preview.Preview


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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.shop_button),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clickable { navController.navigate("Shop") }
                    .padding(bottom = 50.dp)
            )
        }

            Spacer(modifier = Modifier.height(16.dp))
            //Display fetched rewards
            if (rewards.isNotEmpty()) {
                rewards.forEach { reward ->
                    RewardItem(reward = reward)
                }
            }
        }
}


//shi

@Composable
fun RewardItem(reward: Reward) {
    val x = reward.getname()
    when(x){
        "Slime" -> Display_slime()
        "Mug"   -> Display_mug()
        "Books" -> Display_books()
        "Pen Holder" -> Display_pen_holder()
        "Cat" -> Display_cat()
        "Plant" -> Display_plant()
    }

}

@Composable
fun Display_slime(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 70.dp, top = 260.dp) // Adjust the padding as needed
    ) {
            Image(
                painter = painterResource(id = R.drawable.slime),
                contentDescription = "slime",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Fit
            )
    }
}

@Composable
fun Display_mug(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 290.dp, top = 230.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.mug),
            contentDescription = "mug",
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
fun Display_books(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 230.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.books),
            contentDescription = "books",
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
fun Display_pen_holder(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 100.dp, top = 220.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.pen_holder),
            contentDescription = "pen holder",
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun Display_plant(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 350.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.plant),
            contentDescription = "pen holder",
            modifier = Modifier.size(180.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview
@Composable
fun Display_cat(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 230.dp, top = 60.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.cat),
            contentDescription = "pen holder",
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
    }
}