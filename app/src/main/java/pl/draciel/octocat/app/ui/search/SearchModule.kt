package pl.draciel.octocat.app.ui.search

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import pl.draciel.octocat.concurrent.ComputationScheduler
import pl.draciel.octocat.concurrent.MainThreadScheduler
import pl.draciel.octocat.core.di.scopes.FragmentScope
import pl.draciel.octocat.github.GithubRepository

@Module
object SearchModule {

    @Provides
    @FragmentScope
    @JvmStatic
    internal fun provideSearchPresenter(
        githubRepository: GithubRepository,
        @MainThreadScheduler mainThreadScheduler: Scheduler,
        @ComputationScheduler computationScheduler: Scheduler
    ): SearchMVP.Presenter {
        return SearchPresenter(githubRepository, computationScheduler, mainThreadScheduler)
    }

}
