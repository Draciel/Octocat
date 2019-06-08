package pl.draciel.octocat.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "avatarUrl")
    val avatarUrl: String,

    @ColumnInfo(name = "score")
    val score: Double

)
