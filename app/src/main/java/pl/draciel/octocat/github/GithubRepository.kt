package pl.draciel.octocat.github

import io.reactivex.Observable
import io.reactivex.Single
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.model.User

interface GithubRepository {
    fun requestUser(user: String): Single<User>
    fun requestCodeRepositories(user: String): Observable<CodeRepository>
}
