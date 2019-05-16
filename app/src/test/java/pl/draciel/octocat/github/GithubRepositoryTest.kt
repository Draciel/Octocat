package pl.draciel.octocat.github

import org.junit.Test
import retrofit2.HttpException

class GithubRepositoryTest {

    private val githubRepository: GithubRepository = GithubModule.provideInMemoryGithubRepository()

    @Test
    fun `getUser queryingExistingUser shouldPass`() {
        githubRepository.requestUser("Draciel")
                .test()
                .assertValueCount(1)
    }

    @Test
    fun `getUser queryingNotExistingUser shouldReturnException`() {
        githubRepository.requestUser("ThisUserDoesNotExist")
                .test()
                .assertError(HttpException::class.java)
    }
}
