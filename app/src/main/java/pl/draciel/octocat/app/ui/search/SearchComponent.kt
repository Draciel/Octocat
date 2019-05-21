package pl.draciel.octocat.app.ui.search

import dagger.Component
import pl.draciel.octocat.core.di.components.AppComponent
import pl.draciel.octocat.core.di.scopes.FragmentScope


@Component(modules = [SearchModule::class], dependencies = [AppComponent::class])
@FragmentScope
internal interface SearchComponent {
    fun inject(searchFragment: SearchFragment)
    //fixme migrate to subcomponents soon
}
