import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.focusbear.Reward

class RewardDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "notesapp.db"
        private const val TABLE_REWARDS = "rewards"
        private const val COLUMN_REWARD_IMAGE_RESOURCE_ID = "imageResourceId"
        private const val COLUMN_REWARD_NAME = "name"
        private const val COLUMN_REWARD_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val cREATEREWARDSTABLE = ("CREATE TABLE $TABLE_REWARDS " +
                "($COLUMN_REWARD_IMAGE_RESOURCE_ID INTEGER PRIMARY KEY," + // Ensure PRIMARY KEY if it's supposed to be
                "$COLUMN_REWARD_NAME TEXT," +
                "$COLUMN_REWARD_DESCRIPTION TEXT)")
        db.execSQL(cREATEREWARDSTABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REWARDS")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun addReward(reward: Reward) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_REWARD_NAME, reward.name)
            put(COLUMN_REWARD_DESCRIPTION, reward.description)
            put(COLUMN_REWARD_IMAGE_RESOURCE_ID, reward.imageResourceId)
        }

        // Check if the reward already exists in the database
        val cursor = db.query(
            TABLE_REWARDS,
            null,
            "$COLUMN_REWARD_NAME = ?",
            arrayOf(reward.name),
            null,
            null,
            null
        )

        if (cursor != null && cursor.count > 0) {
            // Reward already exists, update its details
            cursor.moveToFirst()
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_REWARD_IMAGE_RESOURCE_ID))
            db.update(TABLE_REWARDS, values, "$COLUMN_REWARD_IMAGE_RESOURCE_ID = ?", arrayOf(id.toString()))
        } else {
            // Reward does not exist, insert it into the database
            db.insert(TABLE_REWARDS, null, values)
        }

        // Close cursor and database
        cursor?.close()
        db.close()
    }


    @SuppressLint("Range")
    fun getAllRewards(): List<Reward> {
        val rewardsList = mutableListOf<Reward>()
        val query = "SELECT * FROM $TABLE_REWARDS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        cursor.use { cursor ->
            while (cursor.moveToNext()) {
                val imageResourceId = cursor.getInt(cursor.getColumnIndex(COLUMN_REWARD_IMAGE_RESOURCE_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_REWARD_NAME))
                val description = cursor.getString(cursor.getColumnIndex(COLUMN_REWARD_DESCRIPTION))
                val reward = Reward(name, description, imageResourceId)
                rewardsList.add(reward)
            }
        }

        return rewardsList
    }

    fun clearDatabase() {
        val db = this.writableDatabase
        db.delete(TABLE_REWARDS, null, null)
        db.close()
    }

}
