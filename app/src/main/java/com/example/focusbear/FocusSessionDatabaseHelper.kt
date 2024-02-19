package com.example.focusbear

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper

class FocusSessionDatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "focussessions"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TIME_FOCUSED = "timeFocused"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_TIME_FOCUSED INTEGER, " +
                "$COLUMN_DATE INTEGER)"
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
    fun createFocusSession(focusSession: FocusSession){
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_TIME_FOCUSED, focusSession.timeFocused)
            put(COLUMN_DATE, focusSession.date)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    //  Function for read all data in database
    fun getAllFocusSessions(): List<FocusSession> {
        val focusSessionsList = mutableListOf<FocusSession>()
        val db = readableDatabase
        val query = "SELECT * FROM ${TABLE_NAME}"
        val cursor = db.rawQuery(query, null)

        //  iterate through rows of table
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val timeFocused = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_FOCUSED))
            val date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE))

            val focusSession = FocusSession(id, timeFocused, date)
            focusSessionsList.add(focusSession)
        }

        // close cursor + db
        cursor.close()
        db.close()
        return focusSessionsList
    }

    //  Function to update data
    fun updateFocusSession(focusSession: FocusSession) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TIME_FOCUSED, focusSession.timeFocused)
            put(COLUMN_DATE, focusSession.date)
        }

        val whereClause = "${COLUMN_ID} = ?"
        val whereArgs = arrayOf(focusSession.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    // Function to get one particular row of data by ID
    // Since we are only having 1 user, we can probably just call the id=1
    fun getFocusSessionByID(focusSessionID: Int): FocusSession {
        val db = readableDatabase
        val query = "SELECT * FROM ${TABLE_NAME} WHERE ${COLUMN_ID} = $focusSessionID"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val timeFocused = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_FOCUSED))
        val date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE))

        cursor.close()
        db.close()

        return FocusSession(id, timeFocused, date)
    }

    //  Function for deleting data
    fun deleteFocusSession(focusSessionId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(focusSessionId.toString())

        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

}