package pl.draciel.octocat.concurrent

object SchedulerSupportExtension {
    /**
     * The operator/class runs on AndroidRxJava's [io.reactivex.android.schedulers.AndroidSchedulers.mainThread] main thread scheduler}
     * or takes timing information from it.
     */
    const val MAIN_THREAD_SCHEDULER = "io.reactivex.android:main-thread"
}
