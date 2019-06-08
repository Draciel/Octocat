package pl.draciel.octocat.database

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.database.model.UserEntity

internal class UserRepositoryImpl(private val dao: UserDao) : UserRepository {

    override fun save(user: User): Completable = Single.just(user)
            .map { it.toUserEntity() }
            .flatMapCompletable { dao.save(it) }

    override fun saveAll(users: List<User>): Completable = Observable.fromIterable(users)
            .map { it.toUserEntity() }
            .toList()
            .flatMapCompletable { dao.saveAll(it) }


    override fun update(user: User): Completable = Single.just(user)
            .map { it.toUserEntity() }
            .flatMapCompletable { dao.update(it) }


    override fun delete(user: User): Completable = Single.just(user)
            .map { it.toUserEntity() }
            .flatMapCompletable { dao.delete(it) }

    override fun deleteById(id: Long): Completable = dao.deleteById(id)


    override fun findAll(): Single<List<User>> = dao.findAll()
            .flatMapObservable { Observable.fromIterable(it) }
            .map { it.toUser() }
            .toList()

    override fun findUserByLogin(login: String): Maybe<User> = dao.findUserByLogin(login)
            .map { it.toUser() }

    override fun findUserById(id: Long): Maybe<User> = dao.findUserById(id)
            .map { it.toUser() }

    override fun existsById(id: Long): Single<Boolean> = dao.findUserById(id)
            .map { true }
            .switchIfEmpty(Single.just(false))

    override fun existsByLogin(login: String): Single<Boolean> = dao.findUserByLogin(login)
            .map { true }
            .switchIfEmpty(Single.just(false))

}

private fun UserEntity.toUser(): User = User(login, id, type, avatarUrl, score)
private fun User.toUserEntity(): UserEntity = UserEntity(id, login, type, avatarUrl, score)
