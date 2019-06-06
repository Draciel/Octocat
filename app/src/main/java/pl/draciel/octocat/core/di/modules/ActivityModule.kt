package pl.draciel.octocat.core.di.modules

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import pl.draciel.octocat.core.di.scopes.ActivityContext
import pl.draciel.octocat.core.di.scopes.ActivityScope

@Module
class ActivityModule(activity: Activity) {

    private val context: Context = activity

    @Provides
    @ActivityScope
    @ActivityContext
    fun provideActivityContext(): Context = context

}
