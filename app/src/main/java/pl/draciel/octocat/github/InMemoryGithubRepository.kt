package pl.draciel.octocat.github

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.threeten.bp.LocalDate
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
        TODO()
    }

    override fun getUserRepositories(userName: String): Single<Response<List<GithubCodeRepository>>> {
        TODO()
    }

    override fun getUserStarredRepositories(userName: String): Single<Response<List<GithubCodeRepository>>> {
        TODO("not implemented")
    }

    override fun getUserFollowings(userName: String): Single<Response<List<GithubUser>>> {
        TODO("not implemented")
    }

    override fun getUserFollowers(userName: String): Single<Response<List<GithubUser>>> {
        TODO("not implemented")
    }

    class InMemoryUserRepository : InMemoryCrudRepository<GithubUser>(
        { it.id }, { user, newId -> user.copy(id = newId, nodeId = "${user.type}:$newId") }
    ) {
        fun findByUserName(user: String) = db.values.firstOrNull { it?.login == user }
    }

    class InMemoryUserDetailsRepository : InMemoryCrudRepository<GithubUserDetails>(
        { it.id }, { user, newId -> user.copy(id = newId) }
    ) {
        fun findByUserName(user: String) = db.values.firstOrNull { it?.login == user }
    }

    class InMemoryCodeRepository : InMemoryCrudRepository<GithubCodeRepository>(
        { it.id }, { codeRepository, newId -> codeRepository.copy(id = newId) }
    )

    companion object {
        const val API_TEST_BASE = "api-test.com"

        fun createUser(nickname: String): GithubUser {
            return GithubUser(
                nickname,
                0,
                "",
                "$API_TEST_BASE/users/$nickname/avatar",
                "$API_TEST_BASE/users/$nickname/gravatar",
                "$API_TEST_BASE/users/$nickname",
                "$API_TEST_BASE/users/$nickname/html",
                "$API_TEST_BASE/users/$nickname/followers",
                "$API_TEST_BASE/users/$nickname/following",
                "$API_TEST_BASE/users/$nickname/gists",
                "$API_TEST_BASE/users/$nickname/starred",
                "$API_TEST_BASE/users/$nickname/subscriptions",
                "$API_TEST_BASE/users/$nickname/organizations",
                "$API_TEST_BASE/users/$nickname/repos",
                "$API_TEST_BASE/users/$nickname/events",
                "$API_TEST_BASE/users/$nickname/receivedEvents",
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
                "$API_TEST_BASE/users/$login/avatar",
                "$API_TEST_BASE/users/$login/gravatar",
                "$API_TEST_BASE/users/$login",
                "$API_TEST_BASE/users/$login/html",
                "$API_TEST_BASE/users/$login/followers",
                "$API_TEST_BASE/users/$login/following",
                "$API_TEST_BASE/users/$login/gists",
                "$API_TEST_BASE/users/$login/starred",
                "$API_TEST_BASE/users/$login/subscriptions",
                "$API_TEST_BASE/users/$login/organizations",
                "$API_TEST_BASE/users/$login/repos",
                "$API_TEST_BASE/users/$login/events",
                "$API_TEST_BASE/users/$login/receivedEvents",
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

    }

}
