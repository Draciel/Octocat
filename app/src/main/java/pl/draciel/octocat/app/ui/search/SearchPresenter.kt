package pl.draciel.octocat.app.ui.search

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.R
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
    private var searchUsersDisposable: Disposable? = null

    override fun attachView(view: SearchMVP.View) {
        super.attachView(view)
        compositeDisposable.add(
            Observable.merge(
                view.observeOnSearchChanged().debounce(400, TimeUnit.MILLISECONDS),
                view.observeOnSearchSubmit()
            ).throttleFirst(300, TimeUnit.MILLISECONDS)
                    .observeOn(uiScheduler)
                    .subscribeBy(
                        onError = { Timber.e(it) },
                        onNext = {
                            if (!it.isBlank()) {
                                view.showProgress()

                                searchUsersDisposable?.let { d ->
                                    if (!d.isDisposed) {
                                        d.dispose()
                                    }
                                }

                                searchUsersDisposable = githubRepository.searchUsers(it)
                                        .toList()
                                        .subscribeOn(backgroundScheduler)
                                        .observeOn(uiScheduler)
                                        .doOnEvent { _, _ -> view.hideProgress() }
                                        .subscribeBy(
                                            onSuccess = { list ->
                                                view.updateResults(list)
                                            },
                                            onError = {
                                                view.showError(R.string.something_went_wrong)
                                            }
                                        )
                            }
                        }
                    )
        )
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
        searchUsersDisposable?.dispose()
    }

    override fun destroy() {
        super.destroy()
        compositeDisposable.dispose()
        searchUsersDisposable?.dispose()
    }
}
