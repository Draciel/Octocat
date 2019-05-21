package pl.draciel.octocat.net

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.LocalDateTime
import pl.draciel.octocat.BuildConfig
import pl.draciel.octocat.net.converters.LocalDateTimeAdapter
import javax.inject.Singleton

@Module
object NetModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        return builder.create()
    }

    @JvmStatic
    @Singleton
    @Provides
    @AppInterceptors
    internal fun provideAppInterceptors(): List<Interceptor> {
        if (!BuildConfig.DEBUG) {
            return emptyList()
        }
        val interceptors: MutableList<Interceptor> = mutableListOf()
        val bodyInterceptor = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
        interceptors.add(bodyInterceptor)
        return interceptors
    }

    @JvmStatic
    @Singleton
    @Provides
    @NetworkInterceptors
    internal fun provideNetworkInterceptors(): List<Interceptor> = emptyList()

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(
        @AppInterceptors appInterceptors: List<@JvmSuppressWildcards Interceptor>,
        @NetworkInterceptors networkInterceptors: List<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient {
        val bodyInterceptor = HttpLoggingInterceptor()
        bodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()

        appInterceptors.forEach { client.addInterceptor(it) }
        networkInterceptors.forEach { client.addInterceptor(it) }

        return client.build()
    }

}
