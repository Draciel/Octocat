package pl.draciel.octocat.github

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.model.UserDetails

interface GithubRepository {

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    fun requestUser(user: String): Single<UserDetails>

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    fun requestUserCodeRepositories(user: String): Observable<CodeRepository>

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    fun requestUserStarredCodeRepositories(user: String): Observable<CodeRepository>

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    fun requestUserFollowers(user: String): Observable<User>

    //fixme find better name because it makes confusion
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    fun requestUserFollowing(user: String): Observable<User>

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    fun searchUsers(keyword: String): Observable<User>
}
