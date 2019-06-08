package pl.draciel.octocat.database

import androidx.room.*
import io.reactivex.Completable

internal interface CrudDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(obj: T): Completable

    @Insert
    fun saveAll(obj: List<T>): Completable

    @Update
    fun update(obj: T): Completable

    @Delete
    fun delete(obj: T): Completable

    @Query("DELETE FROM users WHERE id = :id")
    fun deleteById(id: Long): Completable

}
