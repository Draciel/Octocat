package pl.draciel.octocat.core.di.components

import dagger.Component
import pl.draciel.octocat.core.di.modules.ActivityModule
import pl.draciel.octocat.core.di.scopes.ActivityScope

@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [AppComponent::class])
interface ActvitiyComponent {

}
