package com.example.focusbear

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ItemsDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_IS_PURCHASED = "isPurchased"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_IMAGE INTEGER, " +
                "$COLUMN_PRICE INTEGER, " +
                "$COLUMN_IS_PURCHASED INTEGER)"
        db?.execSQL(createTableQuery)
    }

//  Upgrade database table
//  Deletes old table, and creates a new one using onCreate function
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    //  Function to add data into database, then close the db connection
    fun createItem(item: Item){
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_NAME, item.name)
            put(COLUMN_IMAGE, item.image)
            put(COLUMN_PRICE, item.price)
            put(COLUMN_IS_PURCHASED, item.isPurchased)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    //  Function for read all data in database
    fun getAllItems(): List<Item> {
        val itemsList = mutableListOf<Item>()
        val db = readableDatabase
        val query = "SELECT * FROM ${TABLE_NAME}"
        val cursor = db.rawQuery(query, null)

    //  iterate through rows of table
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val image = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
            val isPurchased = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_PURCHASED))

            val item = Item(id, name, image, price, isPurchased)
            itemsList.add(item)
        }

        // close cursor + db
        cursor.close()
        db.close()
        return itemsList
    }

    //  Function to update data
    fun updateItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, item.name)
            put(COLUMN_IMAGE, item.image)
            put(COLUMN_PRICE, item.price)
            put(COLUMN_IS_PURCHASED, item.isPurchased)
        }

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(item.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    // Function to get one particular row of data by ID
    // Since we are only having 1 user, we can probably just call the id=1
    fun getItemByID(itemID: Int): Item {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $itemID"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val image = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
        val price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
        val isPurchased = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_PURCHASED))

        cursor.close()
        db.close()

        return Item(id, name, image, price, isPurchased)
    }

    //  Function for deleting data
    fun deleteItem(itemId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(itemId.toString())

        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}