package pl.draciel.octocat.core.di.components

import dagger.Component
import pl.draciel.octocat.core.di.modules.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

}
