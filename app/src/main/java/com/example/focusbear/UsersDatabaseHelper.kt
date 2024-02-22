package com.example.focusbear

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UsersDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_CURRENCY = "currency"
        private const val COLUMN_FAILED_SESSION_COUNT = "failed_session_count"
        private const val COLUMN_TOTAL_SESSION_COUNT = "total_session_count"
        private const val COLUMN_TOTAL_TIME_FOCUSED = "total_time_focused"
        private const val COLUMN_TOTAL_CONSECUTIVE_COUNT = "total_consecutive_count"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_CURRENCY INTEGER, " +
                "$COLUMN_FAILED_SESSION_COUNT INTEGER, " +
                "$COLUMN_TOTAL_SESSION_COUNT INTEGER, " +
                "$COLUMN_TOTAL_TIME_FOCUSED INTEGER, " +
                "$COLUMN_TOTAL_CONSECUTIVE_COUNT INTEGER)"
        db?.execSQL(createTableQuery)
    }

//  Upgrade database table
//  Deletes old table, and creates a new one using onCreate function
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun createTable(){
        val db = writableDatabase
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_CURRENCY INTEGER, " +
                "$COLUMN_FAILED_SESSION_COUNT INTEGER, " +
                "$COLUMN_TOTAL_SESSION_COUNT INTEGER, " +
                "$COLUMN_TOTAL_TIME_FOCUSED INTEGER, " +
                "$COLUMN_TOTAL_CONSECUTIVE_COUNT INTEGER)"
        db.execSQL(createTableQuery)
        createUser(User(
            id = 1,
            username = "john_doe",
            currency = 100,
            failedSessionCount = 0,
            totalSessionCount = 5,
            totalTimeFocused = 10000,
            totalConsecutiveCount = 2
        ))
    }
    //  Function to add data into database, then close the db connection
    fun createUser(user: User){
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_CURRENCY, user.currency)
            put(COLUMN_FAILED_SESSION_COUNT, user.failedSessionCount)
            put(COLUMN_TOTAL_SESSION_COUNT, user.totalSessionCount)
            put(COLUMN_TOTAL_TIME_FOCUSED, user.totalTimeFocused)
            put(COLUMN_TOTAL_CONSECUTIVE_COUNT, user.totalConsecutiveCount)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    //  Function for read all data in database
    //  We probably won't use this
    fun getAllUsers(): List<User> {
        val usersList = mutableListOf<User>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

//      iterate through rows of table
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
            val currency = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CURRENCY))
            val failedSessionCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAILED_SESSION_COUNT))
            val totalSessionCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_SESSION_COUNT))
            val totalTimeFocused = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_TIME_FOCUSED))
            val totalConsecutiveCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_CONSECUTIVE_COUNT))


            val user = User(id, username, currency, failedSessionCount, totalSessionCount, totalTimeFocused, totalConsecutiveCount)
            usersList.add(user)
        }

        // close cursor + db
        cursor.close()
        db.close()
        return usersList
    }

    //  Function to update data
    fun updateUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_CURRENCY, user.currency)
            put(COLUMN_FAILED_SESSION_COUNT, user.failedSessionCount)
            put(COLUMN_TOTAL_SESSION_COUNT, user.totalSessionCount)
            put(COLUMN_TOTAL_TIME_FOCUSED, user.totalTimeFocused)
            put(COLUMN_TOTAL_CONSECUTIVE_COUNT, user.totalConsecutiveCount)
        }

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(user.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    // Function to get one particular row of data by ID
    // Since we are only having 1 user, we can probably just call the id=1
    fun getUserByID(userID: Int): User {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $userID"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
        val currency = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CURRENCY))
        val failedSessionCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAILED_SESSION_COUNT))
        val totalSessionCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_SESSION_COUNT))
        val totalTimeFocused = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_TIME_FOCUSED))
        val totalConsecutiveCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_CONSECUTIVE_COUNT))

        cursor.close()
        db.close()

        return User(id, username, currency, failedSessionCount, totalSessionCount, totalTimeFocused, totalConsecutiveCount)
    }

    //  Function for deleting data
    fun deleteUser(userId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(userId.toString())

        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

}