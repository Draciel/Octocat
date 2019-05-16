package pl.draciel.octocat.github

import io.reactivex.Observable
import io.reactivex.Single
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.core.utility.ResponseTransformers
import pl.draciel.octocat.github.api.GithubService
import pl.draciel.octocat.github.api.model.GithubCodeRepository
import pl.draciel.octocat.github.api.model.GithubUser
import java.net.HttpURLConnection

internal class GithubRepositoryImpl(private val githubService: GithubService) :
    GithubRepository {

    override fun requestUser(user: String): Single<User> {
        return githubService.getUser(user)
                .compose(ResponseTransformers.httpStatus(HttpURLConnection.HTTP_NOT_FOUND))
                .map { mapUser(it.body()!!) }
    }

    override fun requestCodeRepositories(user: String): Observable<CodeRepository> {
        return githubService.getUserRepositories(user)
                .compose(ResponseTransformers.httpStatusObservable(HttpURLConnection.HTTP_NOT_FOUND))
                .map { mapCodeRepository(it.body()!!) }
    }

    companion object {
        @JvmStatic
        fun mapUser(user: GithubUser): User {
            return User(
                user.login,
                user.name,
                user.company,
                user.location,
                user.email,
                user.id,
                user.url,
                user.reposUrl,
                user.createdAt,
                user.updatedAt
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
    }
}
