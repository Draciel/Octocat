package pl.draciel.octocat.database

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import pl.draciel.octocat.app.model.User

interface UserRepository {

    @CheckReturnValue
    fun save(user: User): Completable

    @CheckReturnValue
    fun saveAll(users: List<User>): Completable

    @CheckReturnValue
    fun update(user: User): Completable

    @CheckReturnValue
    fun delete(user: User): Completable

    @CheckReturnValue
    fun deleteById(id: Long): Completable

    @CheckReturnValue
    fun findAll(): Single<List<User>>

    @CheckReturnValue
    fun findUserByLogin(login: String): Maybe<User>

    @CheckReturnValue
    fun findUserById(id: Long): Maybe<User>

    @CheckReturnValue
    fun existsById(id: Long): Single<Boolean>

    @CheckReturnValue
    fun existsByLogin(login: String): Single<Boolean>
}
