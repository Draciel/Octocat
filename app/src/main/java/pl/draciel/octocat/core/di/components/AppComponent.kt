package pl.draciel.octocat.core.di.components

import dagger.Component
import pl.draciel.octocat.core.di.modules.AppModule
import pl.draciel.octocat.github.GithubModule
import pl.draciel.octocat.github.GithubRepository
import pl.draciel.octocat.net.NetModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, GithubModule::class])
interface AppComponent {

    fun githubRepository(): GithubRepository

}
