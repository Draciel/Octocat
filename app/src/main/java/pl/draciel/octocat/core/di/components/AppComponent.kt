package pl.draciel.octocat.core.di.components

import dagger.Component
import io.reactivex.Scheduler
import pl.draciel.octocat.concurrent.ComputationScheduler
import pl.draciel.octocat.concurrent.SchedulerModule
import pl.draciel.octocat.concurrent.MainThreadScheduler
import pl.draciel.octocat.core.di.modules.AppModule
import pl.draciel.octocat.database.DatabaseModule
import pl.draciel.octocat.database.FavouriteUserRepository
import pl.draciel.octocat.github.GithubModule
import pl.draciel.octocat.github.GithubRepository
import pl.draciel.octocat.imageloader.ImageLoader
import pl.draciel.octocat.imageloader.ImageLoaderModule
import pl.draciel.octocat.net.NetModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetModule::class,
        GithubModule::class,
        SchedulerModule::class,
        ImageLoaderModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {

    fun githubRepository(): GithubRepository

    @MainThreadScheduler
    fun uiScheduler(): Scheduler

    @ComputationScheduler
    fun computationScheduler(): Scheduler

    fun imageLoader(): ImageLoader

    fun userRepository(): FavouriteUserRepository

}
