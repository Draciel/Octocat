package pl.draciel.octocat.core.di.modules

import android.content.Context
import dagger.Module
import pl.draciel.octocat.GithubApp

@Module
class AppModule(app: GithubApp) {

    private val appContext: Context = app.applicationContext

}
