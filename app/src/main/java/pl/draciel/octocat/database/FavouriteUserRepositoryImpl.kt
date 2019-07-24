package pl.draciel.octocat.database

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import pl.draciel.octocat.app.model.FavouriteUser
import pl.draciel.octocat.database.model.FavouriteUserEntity

internal class FavouriteUserRepositoryImpl(private val dao: FavouriteUserDao) : FavouriteUserRepository {

    override fun save(user: FavouriteUser): Completable = Single.just(user)
            .map { it.toUserEntity() }
            .flatMapCompletable { dao.save(it) }

    override fun saveAll(users: List<FavouriteUser>): Completable = Observable.fromIterable(users)
            .map { it.toUserEntity() }
            .toList()
            .flatMapCompletable { dao.saveAll(it) }

    override fun update(user: FavouriteUser): Completable = Single.just(user)
            .map { it.toUserEntity() }
            .flatMapCompletable { dao.update(it) }

    override fun delete(user: FavouriteUser): Completable = Single.just(user)
            .map { it.toUserEntity() }
            .flatMapCompletable { dao.delete(it) }

    override fun deleteById(id: Long): Completable = dao.deleteById(id)

    override fun findAll(): Single<List<FavouriteUser>> = dao.findAll()
            .flatMapObservable { Observable.fromIterable(it) }
            .map { it.toUser() }
            .toList()

    override fun findUserByLogin(login: String): Maybe<FavouriteUser> = dao.findUserByLogin(login)
            .map { it.toUser() }

    override fun findUserById(id: Long): Maybe<FavouriteUser> = dao.findUserById(id)
            .map { it.toUser() }

    override fun existsById(id: Long): Single<Boolean> = dao.findUserById(id)
            .map { true }
            .switchIfEmpty(Single.just(false))

    override fun existsByLogin(login: String): Single<Boolean> = dao.findUserByLogin(login)
            .map { true }
            .switchIfEmpty(Single.just(false))

}

private fun FavouriteUserEntity.toUser(): FavouriteUser =
    FavouriteUser(login, id, type, avatarUrl, company, bio)

private fun FavouriteUser.toUserEntity(): FavouriteUserEntity =
    FavouriteUserEntity(id, login, type, avatarUrl, company, bio)
