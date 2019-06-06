package pl.draciel.octocat.github.api

import io.reactivex.Single
import pl.draciel.octocat.github.api.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GithubService {

    @GET("users/{user}")
    fun getUser(@Path("user") userName: String): Single<Response<GithubUserDetails>>

    @GET("users/{user}/repos")
    fun getUserRepositories(@Path("user") userName: String): Single<Response<List<GithubCodeRepository>>>

    @GET("users/{user}/starred")
    fun getUserStarredRepositories(@Path("user") userName: String): Single<Response<List<GithubCodeRepository>>>

    @GET("users/{user}/following")
    fun getUserFollowing(@Path("user") userName: String): Single<Response<List<GithubUser>>>

    @GET("users/{user}/followers")
    fun getUserFollowers(@Path("user") userName: String): Single<Response<List<GithubUser>>>

    @GET("search/users")
    fun searchUsers(
        @Query("q") keyword: String,
        @Query("page") page: Int = 0,
        @Query("per_page") perPage: Int = 25,
        @Query("sort") sort: String? = null,
        @Query("order") order: SortOrder = SortOrder.DESCENDANT
    ): Single<Response<SearchUserResponse>>

}
