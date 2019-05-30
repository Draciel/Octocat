package pl.draciel.octocat.app.ui.userdetails

import dagger.Component
import pl.draciel.octocat.core.di.components.AppComponent
import pl.draciel.octocat.core.di.scopes.FragmentScope

@FragmentScope
@Component(modules = [UserDetailsModule::class], dependencies = [AppComponent::class])
interface UserDetailsComponent {
    fun inject(userDetailsFragment: UserDetailsFragment)
}
