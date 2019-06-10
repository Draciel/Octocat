package pl.draciel.octocat.database

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import pl.draciel.octocat.app.model.FavouriteUser
import pl.draciel.octocat.app.model.User

interface FavouriteUserRepository {

    @CheckReturnValue
    fun save(user: FavouriteUser): Completable

    @CheckReturnValue
    fun saveAll(users: List<FavouriteUser>): Completable

    @CheckReturnValue
    fun update(user: FavouriteUser): Completable

    @CheckReturnValue
    fun delete(user: FavouriteUser): Completable

    @CheckReturnValue
    fun deleteById(id: Long): Completable

    @CheckReturnValue
    fun findAll(): Single<List<FavouriteUser>>

    @CheckReturnValue
    fun findUserByLogin(login: String): Maybe<FavouriteUser>

    @CheckReturnValue
    fun findUserById(id: Long): Maybe<FavouriteUser>

    @CheckReturnValue
    fun existsById(id: Long): Single<Boolean>

    @CheckReturnValue
    fun existsByLogin(login: String): Single<Boolean>
}
