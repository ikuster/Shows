package i.kuster.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Show")
data class Show(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0,
    @ColumnInfo(name="user")
    val userName:String?,
    @ColumnInfo(name = "id_show")
    val showId: String,
    @ColumnInfo(name = "like")
    val like: Int=0
) : Serializable