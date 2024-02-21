package com.example.focusbear

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun Shop(
//    itemsDatabaseHelper: ItemsDatabaseHelper
) {
    // Get all items and check what is not bought yet
//    val items = itemsDatabaseHelper.getAllItems()
    // Dummy data
    val items = arrayOf(
        Item(1, "Item 1", R.drawable.books, 10, 0),
        Item(2, "Item 2", R.drawable.mug, 20, 0),
        Item(3, "Item 3", R.drawable.pens, 30, 0),
        Item(4, "Item 4", R.drawable.pen_holder, 40, 0),
        Item(5, "Item 5", R.drawable.books, 50, 1),
        Item(6, "Item 6", R.drawable.mug, 60, 0),
        Item(7, "Item 7", R.drawable.pens, 70, 1),
        Item(8, "Item 8", R.drawable.pen_holder, 80, 0),
        Item(9, "Item 9", R.drawable.books, 90, 0),
        Item(10, "Item 10", R.drawable.mug, 100, 0)
    )
//    var shopItems = arrayOf<Item>()
//    for (item in items) {
//        if (item.isPurchased == 0) {
//            shopItems += item
//        }
//    }

    val shopItems = items.filter { it.isPurchased == 0 }

    //Only take first 9 items
    val displayFirstRow = shopItems.slice(0 until minOf(shopItems.size, 3))
    val displaySecondRow = shopItems.slice( 3 until minOf(shopItems.size, 6))
    val displayThirdRow = shopItems.slice(6 until minOf(shopItems.size, 9))


    //control dialog and selectedItems
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.shop_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(id = R.drawable.shelf),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Need to add clickable to buy item
            Row(
                modifier = Modifier.padding(top = 190.dp),
            ) {
                displayFirstRow.forEach { item ->
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                selectedItem = item
                                showDialog = true
                            }
                    )
                }
            }
            Row(
                modifier = Modifier.padding(top = 35.dp),
            ) {
                displaySecondRow.forEach { item ->
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                selectedItem = item
                                showDialog = true
                            }
                    )
                }
            }
            Row(
                modifier = Modifier.padding(top = 35.dp),
            ) {
                displayThirdRow.forEach { item ->
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                selectedItem = item
                                showDialog = true
                            }
                    )
                }
            }
        }

    }

    if (showDialog) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.beary),
                contentDescription = "Bear",
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-90).dp)
                    .offset(y = (-20).dp)
                    .padding(bottom = 5.dp)
            )

            // Bubble dialog above the bear
            Column(
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = -100.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.chat_bubble),
                        contentDescription = "Background",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(300.dp)
                            .offset(x = 30.dp)
                    )
                    Text(
                        text = "Buy ${selectedItem?.name} for ${selectedItem?.price} coins?",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 140.dp, end = 30.dp)
                            .offset(x = 15.dp)
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(50.dp)
                            .offset(y = (-40).dp)
                    ) {
                        // confirm
                        Image(
                            painter = painterResource(id = R.drawable.yes_button),
                            contentDescription = "Confirm",
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    showDialog = false
                                }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        // cancel button
                        Image(
                            painter = painterResource(id = R.drawable.no_button),
                            contentDescription = "Cancel",
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    showDialog = false
                                }
                        )
                    }
                }
            }
        }
    }
}
