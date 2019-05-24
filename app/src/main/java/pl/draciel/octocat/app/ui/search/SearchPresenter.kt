package pl.draciel.octocat.app.ui.search

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.core.mvp.BaseLifecyclePresenter
import pl.draciel.octocat.github.GithubRepository
import timber.log.Timber
import java.util.concurrent.TimeUnit

internal class SearchPresenter(
    private val githubRepository: GithubRepository,
    private val backgroundScheduler: Scheduler,
    private val uiScheduler: Scheduler
) : BaseLifecyclePresenter<SearchMVP.View>(),
    SearchMVP.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun attachView(view: SearchMVP.View) {
        super.attachView(view)

        compositeDisposable.add(
            Observable.merge(
                view.observeOnSearchChanged().debounce(400, TimeUnit.MILLISECONDS),
                view.observeOnSearchSubmit()
            ).throttleFirst(300, TimeUnit.MILLISECONDS)
                    .observeOn(backgroundScheduler)
                    .flatMapSingle {
                        if (it.isBlank()) {
                            return@flatMapSingle Single.just(emptyList<User>())
                        }
                        return@flatMapSingle githubRepository.searchUsers(it).toList()
                    }
                    .observeOn(uiScheduler)
                    .subscribeBy(
                        onError = { Timber.e(it) },
                        onNext = { it.forEach { u -> Timber.d("User %s", u.login) } }
                    )
        )
    }

    override fun destroy() {
        super.destroy()
        compositeDisposable.dispose()
    }
}
