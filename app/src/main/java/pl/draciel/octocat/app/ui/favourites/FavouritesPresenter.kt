package pl.draciel.octocat.app.ui.favourites

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.core.mvp.BaseLifecyclePresenter
import pl.draciel.octocat.database.UserRepository
import timber.log.Timber

class FavouritesPresenter(
    private val uiThreadScheduler: Scheduler,
    private val backgroundScheduler: Scheduler,
    private val userRepository: UserRepository
) : FavouritesMVP.Presenter, BaseLifecyclePresenter<FavouritesMVP.View>() {

    private val compositeDisposable = CompositeDisposable()

    override fun loadFavouriteUsers() {
        compositeDisposable.add(userRepository.findAll()
                .subscribeOn(backgroundScheduler)
                .observeOn(uiThreadScheduler)
                .subscribeBy(
                    onError = { Timber.e(it) },
                    onSuccess = { view?.setUsers(it) }
                )
        )
    }

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }

    override fun destroy() {
        compositeDisposable.dispose()
        super.destroy()
    }
}
