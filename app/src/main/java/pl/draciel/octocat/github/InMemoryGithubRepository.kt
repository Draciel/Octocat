package pl.draciel.octocat.github

import com.bumptech.glide.load.model.stream.HttpUriLoader
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import pl.draciel.octocat.app.model.UserDetails
import pl.draciel.octocat.core.utility.InMemoryCrudRepository
import pl.draciel.octocat.github.api.GithubService
import pl.draciel.octocat.github.api.model.*
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*
import javax.net.ssl.HttpsURLConnection

internal class InMemoryGithubService : GithubService {

    private val userRepository: InMemoryUserRepository = InMemoryUserRepository()
    private val codeRepository: InMemoryCodeRepository = InMemoryCodeRepository()
    private val userDetailsRepository: InMemoryUserDetailsRepository = InMemoryUserDetailsRepository()

    init {
        userRepository.save(createUser("Draciel"))
        userRepository.save(createUser("Jake123"))
        userDetailsRepository.save(GithubUserDetails("Draciel", 0))
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

    override fun getUserRepositories(userName: String): Observable<Response<GithubCodeRepository>> {
        TODO()
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

        fun createUser(name: String): GithubUser {
            return GithubUser(
                name,
                0,
                "",
                "$API_TEST_BASE/users/$name/avatar",
                "$API_TEST_BASE/users/$name/gravatar",
                "$API_TEST_BASE/users/$name",
                "$API_TEST_BASE/users/$name/html",
                "$API_TEST_BASE/users/$name/followers",
                "$API_TEST_BASE/users/$name/following",
                "$API_TEST_BASE/users/$name/gists",
                "$API_TEST_BASE/users/$name/starred",
                "$API_TEST_BASE/users/$name/subscriptions",
                "$API_TEST_BASE/users/$name/organizations",
                "$API_TEST_BASE/users/$name/repos",
                "$API_TEST_BASE/users/$name/events",
                "$API_TEST_BASE/users/$name/receivedEvents",
                "User",
                false,
                13.37
            )
        }
    }

}
