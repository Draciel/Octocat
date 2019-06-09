package pl.draciel.octocat.app.ui.favourites

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import pl.draciel.octocat.concurrent.ComputationScheduler
import pl.draciel.octocat.concurrent.MainThreadScheduler
import pl.draciel.octocat.core.di.scopes.FragmentScope
import pl.draciel.octocat.database.UserRepository

@Module
object FavouritesModule {

    @Provides
    @JvmStatic
    @FragmentScope
    internal fun provideFavourites(
        @MainThreadScheduler uiThreadScheduler: Scheduler,
        @ComputationScheduler backgroundScheduler: Scheduler,
        userRepository: UserRepository
    ): FavouritesMVP.Presenter {
        return FavouritesPresenter(uiThreadScheduler, backgroundScheduler, userRepository)
    }

}
