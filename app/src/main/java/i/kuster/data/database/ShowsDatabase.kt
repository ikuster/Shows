package i.kuster.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import i.kuster.data.database.dao.ShowsDao

@Database(
    version = 1,
    entities = [
        Show::class]
)
abstract class ShowsDatabase : RoomDatabase() {

    abstract fun showsDao(): ShowsDao
}