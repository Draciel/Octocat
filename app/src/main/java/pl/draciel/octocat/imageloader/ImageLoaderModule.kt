package pl.draciel.octocat.imageloader

import android.content.Context
import dagger.Module
import dagger.Provides
import pl.draciel.octocat.core.di.scopes.ApplicationContext
import javax.inject.Singleton

@Module
object ImageLoaderModule {

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideImageLoader(@ApplicationContext context: Context): ImageLoader = GlideImageLoader(context)

}
