package pl.draciel.octocat

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import pl.draciel.octocat.core.di.components.AppComponent
import pl.draciel.octocat.core.di.components.DaggerAppComponent
import pl.draciel.octocat.core.di.modules.AppModule
import timber.log.Timber


class GithubApp : Application() {

    val appComponent: AppComponent by lazy { createAppComponent() }

    private fun createAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        AndroidThreeTen.init(this)
    }

    companion object {

        @JvmStatic
        fun getApplication(context: Context): GithubApp = context.applicationContext as GithubApp

    }
}
