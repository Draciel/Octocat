package pl.draciel.octocat.app.ui.userdetails.pager

import dagger.Component
import pl.draciel.octocat.app.ui.userdetails.pager.coderepostiory.CodeRepositoryFragment
import pl.draciel.octocat.app.ui.userdetails.pager.followers.FollowersFragment
import pl.draciel.octocat.core.di.components.AppComponent
import pl.draciel.octocat.core.di.scopes.FragmentScope


@FragmentScope
@Component(modules = [PageListModule::class], dependencies = [AppComponent::class])
interface PageListComponent {
    fun inject(injectingPageListFragment: InjectingPageListFragment)
}
