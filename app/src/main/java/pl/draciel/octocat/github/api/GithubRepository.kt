package pl.draciel.octocat.github.api

import io.reactivex.Observable
import io.reactivex.Single
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.model.User

interface GithubRepository {
    fun getUser(user: String): Single<User>
    fun getCodeRepositories(user: String): Observable<CodeRepository>
}
