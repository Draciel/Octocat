package pl.draciel.octocat.app.ui.userdetails

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.core.mvp.BaseLifecyclePresenter
import pl.draciel.octocat.github.GithubRepository
import timber.log.Timber

class UserDetailsPresenter(
    private val githubRepository: GithubRepository,
    private val mainThreadScheduler: Scheduler,
    private val backgroundScheduler: Scheduler
) : BaseLifecyclePresenter<UserDetailsMVP.View>(), UserDetailsMVP.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadUserDetails(login: String) {
        compositeDisposable.add(
            githubRepository.requestUser(login)
                    .subscribeOn(backgroundScheduler)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(
                        onError = { Timber.e(it) },
                        onSuccess = {
                            view?.setUser(it)
                        }
                    )
        )
    }

    override fun destroy() {
        super.destroy()
        compositeDisposable.dispose()
    }
}
