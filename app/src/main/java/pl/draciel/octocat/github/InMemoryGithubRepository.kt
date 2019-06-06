package pl.draciel.octocat.github

import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.threeten.bp.LocalDateTime
import pl.draciel.octocat.core.utility.InMemoryCrudRepository
import pl.draciel.octocat.github.api.GithubService
import pl.draciel.octocat.github.api.model.*
import retrofit2.Response
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection

internal class InMemoryGithubService : GithubService {

    private val userRepository: InMemoryUserRepository = InMemoryUserRepository()
    private val codeRepository: InMemoryCodeRepository = InMemoryCodeRepository()
    private val userDetailsRepository: InMemoryUserDetailsRepository = InMemoryUserDetailsRepository()
    private val userStarredCodeRepository: InMemoryStarredRepositories = InMemoryStarredRepositories()
    private val followersRepository: InMemoryFollowers = InMemoryFollowers()

    init {
        userRepository.save(createUser("Draciel"))
        userRepository.save(createUser("Jake123"))
        userDetailsRepository.save(
            createUserDetails(
                "Draciel",
                "Jake",
                "",
                "",
                "",
                "test@gmail.com",
                false,
                "Android Dev",
                4,
                5,
                1,
                2,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now()
            )
        )
    }

    override fun getUser(userName: String): Single<Response<GithubUserDetails>> {
        val user = userDetailsRepository.findByUserName(userName)
        if (user != null) {
            return Single.just(Response.success<GithubUserDetails>(HttpsURLConnection.HTTP_OK, user))
        }
        return Single.just<Response<GithubUserDetails>>(
            Response.error<GithubUserDetails>(
                HttpURLConnection.HTTP_NOT_FOUND,
                ResponseBody.create(MediaType.get("application/json"), "")
            )
        )
    }

    override fun searchUsers(
        keyword: String,
        page: Int,
        perPage: Int,
        sort: String?,
        order: SortOrder
    ): Single<Response<SearchUserResponse>> {
        val repos = userRepository.searchUsers(keyword)
        if (!repos.isEmpty()) {
            return Single.just(
                Response.success<SearchUserResponse>(
                    HttpsURLConnection.HTTP_OK,
                    SearchUserResponse(repos.size, false, repos)
                )
            )
        }
        return Single.just<Response<SearchUserResponse>>(
            Response.error<SearchUserResponse>(
                HttpURLConnection.HTTP_NOT_FOUND,
                ResponseBody.create(MediaType.get("application/json"), "")
            )
        )
    }

    override fun getUserRepositories(userName: String): Single<Response<List<GithubCodeRepository>>> {
        val repos = codeRepository.findByUserName(userName)
        if (!repos.isEmpty()) {
            return Single.just(
                Response.success<List<GithubCodeRepository>>(
                    HttpsURLConnection.HTTP_OK, repos
                )
            )
        }
        return Single.just<Response<List<GithubCodeRepository>>>(
            Response.error<List<GithubCodeRepository>>(
                HttpURLConnection.HTTP_NOT_FOUND,
                ResponseBody.create(MediaType.get("application/json"), "")
            )
        )
    }

    override fun getUserStarredRepositories(userName: String): Single<Response<List<GithubCodeRepository>>> {
        val repos = userStarredCodeRepository.findStarred(userName)
        if (!repos.isEmpty()) {
            return Single.just(
                Response.success<List<GithubCodeRepository>>(
                    HttpsURLConnection.HTTP_OK, repos
                )
            )
        }
        return Single.just<Response<List<GithubCodeRepository>>>(
            Response.error<List<GithubCodeRepository>>(
                HttpURLConnection.HTTP_NOT_FOUND,
                ResponseBody.create(MediaType.get("application/json"), "")
            )
        )
    }

    override fun getUserFollowing(userName: String): Single<Response<List<GithubUser>>> {
        val repos = followersRepository.findUserFollowing(userName)
        if (!repos.isEmpty()) {
            return Single.just(
                Response.success<List<GithubUser>>(
                    HttpsURLConnection.HTTP_OK, repos
                )
            )
        }
        return Single.just<Response<List<GithubUser>>>(
            Response.error<List<GithubUser>>(
                HttpURLConnection.HTTP_NOT_FOUND,
                ResponseBody.create(MediaType.get("application/json"), "")
            )
        )
    }

    override fun getUserFollowers(userName: String): Single<Response<List<GithubUser>>> {
        val repos = followersRepository.findUserFollowers(userName)
        if (!repos.isEmpty()) {
            return Single.just(
                Response.success<List<GithubUser>>(
                    HttpsURLConnection.HTTP_OK, repos
                )
            )
        }
        return Single.just<Response<List<GithubUser>>>(
            Response.error<List<GithubUser>>(
                HttpURLConnection.HTTP_NOT_FOUND,
                ResponseBody.create(MediaType.get("application/json"), "")
            )
        )
    }

    class InMemoryUserRepository : InMemoryCrudRepository<GithubUser>(
        { it.id }, { user, newId -> user.copy(id = newId, nodeId = "${user.type}:$newId") }
    ) {
        fun findByUserName(user: String) = db.values.firstOrNull { it?.login == user }

        fun searchUsers(keyword: String) = db.values.filterNotNull()
                .filter { it.login.contains(keyword) }
    }

    class InMemoryUserDetailsRepository : InMemoryCrudRepository<GithubUserDetails>(
        { it.id }, { user, newId -> user.copy(id = newId) }
    ) {
        fun findByUserName(user: String) = db.values.firstOrNull { it?.login == user }
    }

    class InMemoryCodeRepository : InMemoryCrudRepository<GithubCodeRepository>(
        { it.id }, { codeRepository, newId -> codeRepository.copy(id = newId) }
    ) {
        fun findByUserName(user: String) = db.values.filterNotNull()
                .filter { it.owner.login == user }
    }

    data class StarredCodeRepository(
        val id: Long,
        val starredBy: String,
        val repo: GithubCodeRepository
    )

    data class Follower(
        val id: Long,
        val followed: GithubUser,
        val follower: GithubUser
    )

    class InMemoryStarredRepositories : InMemoryCrudRepository<StarredCodeRepository>(
        { it.id }, { repo, newId -> repo.copy(id = newId) }
    ) {
        fun findStarred(user: String) = db.values.filterNotNull()
                .filter { it.starredBy == user }
                .map { it.repo }
    }

    class InMemoryFollowers : InMemoryCrudRepository<Follower>(
        { it.id }, { user, newId -> user.copy(id = newId) }
    ) {
        fun findUserFollowers(user: String) = db.values.filterNotNull()
                .filter { it.followed.login == user }
                .map { it.follower }

        fun findUserFollowing(user: String) = db.values.filterNotNull()
                .filter { it.follower.login == user }
                .map { it.followed }
    }

    companion object {
        const val API_TEST_BASE = "api-test.com"

        fun createUser(nickname: String): GithubUser {
            return GithubUser(
                nickname,
                0,
                "",
                "$API_TEST_BASE/repos/$nickname/avatar",
                "$API_TEST_BASE/repos/$nickname/gravatar",
                "$API_TEST_BASE/repos/$nickname",
                "$API_TEST_BASE/repos/$nickname/html",
                "$API_TEST_BASE/repos/$nickname/followers",
                "$API_TEST_BASE/repos/$nickname/following",
                "$API_TEST_BASE/repos/$nickname/gists",
                "$API_TEST_BASE/repos/$nickname/starred",
                "$API_TEST_BASE/repos/$nickname/subscriptions",
                "$API_TEST_BASE/repos/$nickname/organizations",
                "$API_TEST_BASE/repos/$nickname/repos",
                "$API_TEST_BASE/repos/$nickname/events",
                "$API_TEST_BASE/repos/$nickname/receivedEvents",
                "User",
                false,
                13.37
            )
        }

        fun createUserDetails(
            login: String, name: String, company: String, blog: String, location: String,
            email: String, hireable: Boolean, bio: String, publicRepos: Int, publicGists: Int,
            followers: Int, following: Int, createdAt: LocalDateTime, updatedAt: LocalDateTime
        ): GithubUserDetails {
            return GithubUserDetails(
                login,
                0,
                "",
                "$API_TEST_BASE/repos/$login/avatar",
                "$API_TEST_BASE/repos/$login/gravatar",
                "$API_TEST_BASE/repos/$login",
                "$API_TEST_BASE/repos/$login/html",
                "$API_TEST_BASE/repos/$login/followers",
                "$API_TEST_BASE/repos/$login/following",
                "$API_TEST_BASE/repos/$login/gists",
                "$API_TEST_BASE/repos/$login/starred",
                "$API_TEST_BASE/repos/$login/subscriptions",
                "$API_TEST_BASE/repos/$login/organizations",
                "$API_TEST_BASE/repos/$login/repos",
                "$API_TEST_BASE/repos/$login/events",
                "$API_TEST_BASE/repos/$login/receivedEvents",
                "User",
                false,
                name,
                company,
                blog,
                location,
                email,
                hireable,
                bio,
                publicRepos,
                publicGists,
                followers,
                following,
                createdAt,
                updatedAt
            )
        }

        fun createUserCodeRepositories(
            name: String,
            fullName: String,
            description: String,
            isPrivate: Boolean,
            owner: GithubUser,
            fork: Boolean,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            pushedAt: LocalDateTime,
            size: Int,
            stargazersCount: Int,
            watchersCount: Int,
            language: String?,
            hasIssues: Boolean,
            hasProjects: Boolean,
            hasDownloads: Boolean,
            hasWiki: Boolean,
            hasPages: Boolean,
            forksCount: Int,
            archived: Boolean,
            disabled: Boolean,
            openIssuesCount: Int,
            license: GithubLicense,
            forks: Int,
            openIssues: Int,
            watchers: Int,
            defaultBranch: String
        ): GithubCodeRepository {
            return GithubCodeRepository(
                0,
                "",
                name,
                fullName,
                isPrivate,
                owner,
                "$API_TEST_BASE/repos/$name/html",
                description,
                fork,
                "$API_TEST_BASE/repos/$name",
                "$API_TEST_BASE/repos/$name/fork",
                "$API_TEST_BASE/repos/$name/keys",
                "$API_TEST_BASE/repos/$name/collaborators",
                "$API_TEST_BASE/repos/$name/teams",
                "$API_TEST_BASE/repos/$name/hooks",
                "$API_TEST_BASE/repos/$name/issueEvents",
                "$API_TEST_BASE/repos/$name/events",
                "$API_TEST_BASE/repos/$name/assignees",
                "$API_TEST_BASE/repos/$name/branches",
                "$API_TEST_BASE/repos/$name/tags",
                "$API_TEST_BASE/repos/$name/blobs",
                "$API_TEST_BASE/repos/$name/gitTags",
                "$API_TEST_BASE/repos/$name/gitRefs",
                "$API_TEST_BASE/repos/$name/trees",
                "$API_TEST_BASE/repos/$name/statuses",
                "$API_TEST_BASE/repos/$name/languages",
                "$API_TEST_BASE/repos/$name/stargazers",
                "$API_TEST_BASE/repos/$name/contributors",
                "$API_TEST_BASE/repos/$name/subscribers",
                "$API_TEST_BASE/repos/$name/subscription",
                "$API_TEST_BASE/repos/$name/commits",
                "$API_TEST_BASE/repos/$name/gitCommits",
                "$API_TEST_BASE/repos/$name/comments",
                "$API_TEST_BASE/repos/$name/issueComments",
                "$API_TEST_BASE/repos/$name/contents",
                "$API_TEST_BASE/repos/$name/compare",
                "$API_TEST_BASE/repos/$name/merges",
                "$API_TEST_BASE/repos/$name/archive",
                "$API_TEST_BASE/repos/$name/downloads",
                "$API_TEST_BASE/repos/$name/issues",
                "$API_TEST_BASE/repos/$name/pulls",
                "$API_TEST_BASE/repos/$name/milestones",
                "$API_TEST_BASE/repos/$name/notifications",
                "$API_TEST_BASE/repos/$name/labels",
                "$API_TEST_BASE/repos/$name/releases",
                "$API_TEST_BASE/repos/$name/deployments",
                createdAt,
                updatedAt,
                pushedAt,
                "$API_TEST_BASE/repos/$name/git",
                "$API_TEST_BASE/repos/$name/ssh",
                "$API_TEST_BASE/repos/$name/done",
                "$API_TEST_BASE/repos/$name/svn",
                "$API_TEST_BASE/repos/$name/homepage",
                size,
                stargazersCount,
                watchersCount,
                language,
                hasIssues,
                hasProjects,
                hasDownloads,
                hasWiki,
                hasPages,
                forksCount,
                "$API_TEST_BASE/repos/$name/mirror",
                archived,
                disabled,
                openIssuesCount,
                license,
                forks,
                openIssues,
                watchers,
                defaultBranch
            )
        }

    }

}
