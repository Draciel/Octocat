package pl.draciel.octocat.database

import android.content.Context
import dagger.Module
import dagger.Provides
import pl.draciel.octocat.core.di.scopes.ApplicationContext

@Module
object DatabaseModule {

    @JvmStatic
    @Provides
    internal fun provideDatabase(@ApplicationContext context: Context) = OctocatDatabase.create(context)

    @JvmStatic
    @Provides
    fun provideUserRepository(database: OctocatDatabase): UserRepository = UserRepositoryImpl(database.userDao())

}
