package pl.draciel.octocat.github

import com.bumptech.glide.load.model.stream.HttpUriLoader
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import pl.draciel.octocat.core.utility.InMemoryCrudRepository
import pl.draciel.octocat.github.api.GithubService
import pl.draciel.octocat.github.api.model.GithubCodeRepository
import pl.draciel.octocat.github.api.model.GithubUser
import retrofit2.Response
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection

internal class InMemoryGithubService : GithubService {

    private val userRepository: InMemoryUserRepository = InMemoryUserRepository()
    private val codeRepository: InMemoryCodeRepository = InMemoryCodeRepository()

    init {
        userRepository.save(
            GithubUser(
                "Draciel",
                null,
                null,
                null,
                null,
                23,
                "api-test.com/user/Draciel",
                "api-test.com/user/Draciel/repos",
                LocalDateTime.of(2011, Month.JANUARY, 12, 12, 34),
                LocalDateTime.now()
            )
        )
    }

    override fun getUser(userName: String): Single<Response<GithubUser>> {
        val user = userRepository.findByUserName(userName)
        if (user != null) {
            return Single.just(Response.success<GithubUser>(HttpsURLConnection.HTTP_OK, user))
        }
        return Single.just<Response<GithubUser>>(
            Response.error<GithubUser>(
                HttpURLConnection.HTTP_NOT_FOUND,
                ResponseBody.create(MediaType.get("application/json"), "")
            )
        )
    }

    override fun getUserRepositories(userName: String): Observable<Response<GithubCodeRepository>> {
        TODO()
    }

    class InMemoryUserRepository : InMemoryCrudRepository<GithubUser>(
        { it.id }, { user, newId -> user.copy(id = newId) }
    ) {
        fun findByUserName(user: String) = db.values.firstOrNull { it?.login == user }
    }

    class InMemoryCodeRepository : InMemoryCrudRepository<GithubCodeRepository>(
        { it.id }, { codeRepository, newId -> codeRepository.copy(id = newId) }
    )

}
