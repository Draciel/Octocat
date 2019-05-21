package pl.draciel.octocat.concurrent

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
object SchedulerModule {

    @Provides
    @Singleton
    @JvmStatic
    @ComputationScheduler
    fun provideExecutionScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @Singleton
    @JvmStatic
    @UiScheduler
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Singleton
    @JvmStatic
    @IoScheduler
    fun provideIoScheduler(): Scheduler = Schedulers.io()

}
