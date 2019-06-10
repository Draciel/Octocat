package pl.draciel.octocat.database

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Maybe
import io.reactivex.Single
import pl.draciel.octocat.database.model.FavouriteUserEntity

@Dao
internal interface FavouriteUserDao : CrudDao<FavouriteUserEntity> {

    @Query("SELECT * FROM users")
    fun findAll(): Single<List<FavouriteUserEntity>>

    @Query("SELECT * FROM users WHERE login = :login")
    fun findUserByLogin(login: String): Maybe<FavouriteUserEntity>

    @Query("SELECT * FROM users WHERE id = :id")
    fun findUserById(id: Long): Maybe<FavouriteUserEntity>

}
