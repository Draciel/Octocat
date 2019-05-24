package pl.draciel.octocat.github

import io.reactivex.Observable
import io.reactivex.Single
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.model.UserDetails

interface GithubRepository {
    fun requestUser(user: String): Single<UserDetails>
    fun requestCodeRepositories(user: String): Observable<CodeRepository>
    fun searchUsers(keyword: String): Observable<User>
}
