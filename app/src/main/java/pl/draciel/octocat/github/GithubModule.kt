package pl.draciel.octocat.github

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import pl.draciel.octocat.github.api.GithubRepository
import pl.draciel.octocat.github.api.GithubRepositoryImpl
import pl.draciel.octocat.github.api.GithubService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val API_GITHUB_BASE = "https://api.github.com/"

@Module
object GithubModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideGithubRepositoryImpl(retrofit: Retrofit): GithubRepository {
        val service = retrofit.create(GithubService::class.java)
        return GithubRepositoryImpl(service)
    }

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_GITHUB_BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

}
