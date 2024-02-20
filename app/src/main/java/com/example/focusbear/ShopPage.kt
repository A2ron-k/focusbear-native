package com.example.focusbear

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.lang.reflect.Modifier

@Composable
fun Shop(
    itemsDatabaseHelper: ItemsDatabaseHelper
) {
    // Get all items and check what is not bought yet
//    val items = itemsDatabaseHelper.getAllItems()
    // Dummy data
    val items = arrayOf(
        Item(1, "Item 1", "image1.jpg", 10, 1),
        Item(2, "Item 2", "image2.jpg", 20, 1),
        Item(3, "Item 3", "image3.jpg", 30, 1),
        Item(4, "Item 4", "image4.jpg", 40, 1),
        Item(5, "Item 5", "image5.jpg", 50, 1),
        Item(6, "Item 6", "image6.jpg", 60, 1),
        Item(7, "Item 7", "image7.jpg", 70, 1),
        Item(8, "Item 8", "image8.jpg", 80, 0),
        Item(9, "Item 9", "image9.jpg", 90, 0),
        Item(10, "Item 10", "image10.jpg", 100, 0)
    )
    var shopItems = arrayOf<Item>()
    for (item in items) {
        if (item.isPurchased == 0) {
            shopItems += item
        }
    }
    //Only take first 6 items
    val displayItems = shopItems.slice(0..5)

}


