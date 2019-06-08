package pl.draciel.octocat.database

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import pl.draciel.octocat.database.model.UserEntity

@Dao
internal interface UserDao : CrudDao<UserEntity> {

    @Query("SELECT * FROM users")
    fun findAll(): Single<List<UserEntity>>

    @Query("SELECT * FROM users WHERE login = :login")
    fun findUserByLogin(login: String): Maybe<UserEntity>

    @Query("SELECT * FROM users WHERE id = :id")
    fun findUserById(id: Long): Maybe<UserEntity>

}
