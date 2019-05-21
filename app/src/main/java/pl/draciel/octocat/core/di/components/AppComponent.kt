package pl.draciel.octocat.core.di.components

import dagger.Component
import io.reactivex.Scheduler
import pl.draciel.octocat.concurrent.ComputationScheduler
import pl.draciel.octocat.concurrent.SchedulerModule
import pl.draciel.octocat.concurrent.UiScheduler
import pl.draciel.octocat.core.di.modules.AppModule
import pl.draciel.octocat.github.GithubModule
import pl.draciel.octocat.github.GithubRepository
import pl.draciel.octocat.net.NetModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, GithubModule::class, SchedulerModule::class])
interface AppComponent {

    fun githubRepository(): GithubRepository

    @UiScheduler
    fun uiScheduler(): Scheduler

    @ComputationScheduler
    fun computationScheduler(): Scheduler

}
