package pl.draciel.octocat.app.ui.userdetails

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import pl.draciel.octocat.concurrent.ComputationScheduler
import pl.draciel.octocat.concurrent.MainThreadScheduler
import pl.draciel.octocat.core.di.scopes.FragmentScope
import pl.draciel.octocat.github.GithubRepository

@Module
object UserDetailsModule {

    @Provides
    @FragmentScope
    @JvmStatic
    internal fun provideUserDetailsPresenter(
        githubRepository: GithubRepository,
        @MainThreadScheduler mainThreadScheduler: Scheduler,
        @ComputationScheduler computationScheduler: Scheduler
    ): UserDetailsMVP.Presenter {
        return UserDetailsPresenter(githubRepository, mainThreadScheduler, computationScheduler)
    }

}
