package pl.draciel.octocat.app.ui.search

import dagger.Module
import dagger.Provides
import pl.draciel.octocat.core.di.scopes.FragmentScope
import pl.draciel.octocat.github.GithubRepository

@Module
object SearchModule {

    @Provides
    @FragmentScope
    @JvmStatic
    internal fun provideSearchPresenter(githubRepository: GithubRepository): SearchMVP.Presenter {
        return SearchPresenter(githubRepository)
    }

}
