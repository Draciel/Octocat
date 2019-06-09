package pl.draciel.octocat.app.ui.favourites

import dagger.Component
import pl.draciel.octocat.core.di.components.AppComponent
import pl.draciel.octocat.core.di.scopes.FragmentScope

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [FavouritesModule::class])
interface FavouritesComponent {
    fun inject(favouritesFragment: FavouritesFragment)
}
