package pl.draciel.octocat.github.api

import io.reactivex.Observable
import io.reactivex.Single
import pl.draciel.octocat.github.model.GithubCodeRepository
import pl.draciel.octocat.github.model.GithubUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface GithubService {

    @GET("users/{user}")
    fun getUser(@Path("user") user: String): Single<Response<GithubUser>>

    @GET("users/{user}/repos")
    fun getUserRepositories(@Path("user") user: String): Observable<Response<GithubCodeRepository>>

}
