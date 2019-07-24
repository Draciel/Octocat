package pl.draciel.octocat.github

import io.reactivex.Observable
import io.reactivex.Single
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.model.UserDetails
import pl.draciel.octocat.core.utility.ResponseTransformers
import pl.draciel.octocat.github.api.GithubService
import pl.draciel.octocat.github.api.model.GithubCodeRepository
import pl.draciel.octocat.github.api.model.GithubUser
import pl.draciel.octocat.github.api.model.GithubUserDetails
import java.net.HttpURLConnection

internal class GithubRepositoryImpl(private val githubService: GithubService) : GithubRepository {

    override fun requestUser(user: String): Single<UserDetails> = githubService.getUser(user)
            .compose(ResponseTransformers.httpStatus(HttpURLConnection.HTTP_NOT_FOUND))
            .map { mapUserDetails(it.body()!!) }

    override fun requestUserCodeRepositories(user: String): Observable<CodeRepository> =
        githubService.getUserRepositories(user)
                .compose(ResponseTransformers.httpStatus(HttpURLConnection.HTTP_NOT_FOUND))
                .flatMapObservable {
                    if (it.body() != null) {
                        return@flatMapObservable Observable.fromIterable(it.body())
                    }
                    return@flatMapObservable Observable.empty<GithubCodeRepository>()
                }
                .map { mapCodeRepository(it) }

    override fun searchUsers(keyword: String): Observable<User> = githubService.searchUsers(keyword)
            .compose(ResponseTransformers.httpStatus(HttpURLConnection.HTTP_NOT_FOUND))
            .flatMapObservable {
                if (it.body() != null) {
                    return@flatMapObservable Observable.fromIterable((it.body())!!.users)
                }
                return@flatMapObservable Observable.empty<GithubUser>()
            }
            .map { mapUser(it) }

    override fun requestUserStarredCodeRepositories(user: String): Observable<CodeRepository> =
        githubService.getUserStarredRepositories(user)
                .compose(ResponseTransformers.httpStatus(HttpURLConnection.HTTP_NOT_FOUND))
                .flatMapObservable {
                    if (it.body() != null) {
                        return@flatMapObservable Observable.fromIterable(it.body())
                    }
                    return@flatMapObservable Observable.empty<GithubCodeRepository>()
                }
                .map { mapCodeRepository(it) }

    override fun requestUserFollowers(user: String): Observable<User> =
        githubService.getUserFollowers(user)
                .compose(ResponseTransformers.httpStatus(HttpURLConnection.HTTP_NOT_FOUND))
                .flatMapObservable {
                    if (it.body() != null) {
                        return@flatMapObservable Observable.fromIterable(it.body())
                    }
                    return@flatMapObservable Observable.empty<GithubUser>()
                }
                .map { mapUser(it) }

    override fun requestUserFollowing(user: String): Observable<User> =
        githubService.getUserFollowing(user)
                .compose(ResponseTransformers.httpStatus(HttpURLConnection.HTTP_NOT_FOUND))
                .flatMapObservable {
                    if (it.body() != null) {
                        return@flatMapObservable Observable.fromIterable(it.body())
                    }
                    return@flatMapObservable Observable.empty<GithubUser>()
                }
                .map { mapUser(it) }

    companion object {
        @JvmStatic
        fun mapUser(user: GithubUser): User {
            return User(
                user.login,
                user.id,
                user.type,
                user.avatarUrl,
                user.score
            )
        }

        @JvmStatic
        fun mapCodeRepository(codeRepository: GithubCodeRepository): CodeRepository {
            return CodeRepository(
                codeRepository.name,
                codeRepository.url,
                codeRepository.description,
                codeRepository.pushedAt,
                codeRepository.language,
                codeRepository.watchers,
                codeRepository.forks,
                codeRepository.stargazersCount,
                codeRepository.id
            )
        }

        @JvmStatic
        fun mapUserDetails(githubUserDetails: GithubUserDetails): UserDetails {
            return UserDetails(
                githubUserDetails.id,
                githubUserDetails.avatarUrl,
                githubUserDetails.login,
                githubUserDetails.nodeId,
                githubUserDetails.type,
                githubUserDetails.name,
                githubUserDetails.company,
                githubUserDetails.blog,
                githubUserDetails.location,
                githubUserDetails.bio,
                githubUserDetails.email,
                githubUserDetails.publicRepos,
                githubUserDetails.publicGists,
                githubUserDetails.followers,
                githubUserDetails.following,
                githubUserDetails.createdAt,
                githubUserDetails.updatedAt
            )
        }
    }
}
