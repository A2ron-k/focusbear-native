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
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

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
//            verticalArrangement = Arrangement.Center,
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
                    )
                }
            }
        }

    }
}