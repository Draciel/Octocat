package pl.draciel.octocat.core.di.modules

import android.content.Context
import dagger.Module
import pl.draciel.octocat.GitApp

@Module
class AppModule(app: GitApp) {

    private val appContext: Context = app.applicationContext

}
