package pl.draciel.octocat.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.draciel.octocat.database.model.UserEntity
import java.lang.IllegalArgumentException

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class OctocatDatabase : RoomDatabase() {

    internal abstract fun userDao(): UserDao

    companion object {

        @JvmStatic
        fun create(context: Context): OctocatDatabase {
            if (context !is Application) {
                throw IllegalArgumentException("Cannot create database without app context")
            }
            return Room.databaseBuilder(context, OctocatDatabase::class.java, "octocat-db")
                    .build()
        }
    }
}
