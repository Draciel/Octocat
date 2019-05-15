package pl.draciel.octocat.github.api

import io.reactivex.Observable
import io.reactivex.Single
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.github.model.GithubCodeRepository
import pl.draciel.octocat.github.model.GithubUser

internal class GithubRepositoryImpl(private val githubService: GithubService) : GithubRepository {

    override fun getUser(user: String): Single<User> {
        return githubService.getUser(user)
            .map { mapUser(it.body()!!) }
    }

    override fun getCodeRepositories(user: String): Observable<CodeRepository> {
        return githubService.getUserRepositories(user)
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
                codeRepository.forks
            )
        }
    }
}
