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
import pl.draciel.octocat.github.api.model.SearchUserResponse
import java.net.HttpURLConnection

internal class GithubRepositoryImpl(private val githubService: GithubService) :
    GithubRepository {

    override fun requestUser(user: String): Single<UserDetails> {
        return githubService.getUser(user)
                .compose(ResponseTransformers.httpStatus(HttpURLConnection.HTTP_NOT_FOUND))
                .map { mapUserDetails(it.body()!!) }
    }

    override fun requestCodeRepositories(user: String): Observable<CodeRepository> {
        return githubService.getUserRepositories(user)
                .compose(ResponseTransformers.httpStatusObservable(HttpURLConnection.HTTP_NOT_FOUND))
                .map { mapCodeRepository(it.body()!!) }
    }

    override fun searchUsers(keyword: String): Observable<User> {
        return githubService.searchUsers(keyword)
                .compose(ResponseTransformers.httpStatus(HttpURLConnection.HTTP_NOT_FOUND))
                .flatMapObservable {
                    if (it.body() != null) {
                        return@flatMapObservable Observable.fromIterable((it.body() as SearchUserResponse).users)
                    }
                    return@flatMapObservable Observable.empty<GithubUser>()
                }
                .map { mapUser(it) }
    }

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
                codeRepository.repositoryName,
                codeRepository.url,
                codeRepository.description,
                codeRepository.pushedAt,
                codeRepository.language,
                codeRepository.watchers,
                codeRepository.forks,
                codeRepository.id
            )
        }

        @JvmStatic
        fun mapUserDetails(githubUserDetails: GithubUserDetails): UserDetails {
            return UserDetails(githubUserDetails.login)
        }
    }
}
