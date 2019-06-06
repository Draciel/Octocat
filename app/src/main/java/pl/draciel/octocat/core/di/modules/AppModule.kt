package pl.draciel.octocat.core.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.core.di.scopes.ActivityContext
import pl.draciel.octocat.core.di.scopes.ActivityScope
import pl.draciel.octocat.core.di.scopes.ApplicationContext
import javax.inject.Singleton

@Module
class AppModule(app: GithubApp) {

    private val appContext: Context = app.applicationContext

    @Provides
    @Singleton
    @ApplicationContext
    fun provideActivityContext(): Context = appContext

}
