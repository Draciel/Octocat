package pl.draciel.octocat.app.ui.userdetails

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.app.model.FavouriteUser
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.model.UserDetails
import pl.draciel.octocat.core.mvp.BaseLifecyclePresenter
import pl.draciel.octocat.database.FavouriteUserRepository
import pl.draciel.octocat.github.GithubRepository
import timber.log.Timber

class UserDetailsPresenter(
    private val githubRepository: GithubRepository,
    private val mainThreadScheduler: Scheduler,
    private val backgroundScheduler: Scheduler,
    private val favouriteUserRepository: FavouriteUserRepository
) : BaseLifecyclePresenter<UserDetailsMVP.View>(), UserDetailsMVP.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadUserDetails(login: String) {
        compositeDisposable.add(
            Single.zip(githubRepository.requestUser(login), favouriteUserRepository.existsByLogin(login),
                BiFunction<UserDetails, Boolean, Pair<UserDetails, Boolean>> { ud, exists -> Pair(ud, exists) })
                    .subscribeOn(backgroundScheduler)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(
                        onError = { Timber.e(it) },
                        onSuccess = {
                            view?.setUserDetails(it.first)
                            view?.setFavouriteChecked(it.second)
                        }
                    )
        )
    }

    override fun saveUserInFavourites(userDetails: UserDetails) {
        compositeDisposable.add(favouriteUserRepository.save(userDetails.toUser())
                .subscribeOn(backgroundScheduler)
                .subscribeBy(
                    onError = { Timber.e(it) },
                    onComplete = { Timber.d("Saved user in favourites") }
                )
        )
    }

    override fun removeUserFromFavourites(userDetails: UserDetails) {
        compositeDisposable.add(favouriteUserRepository.delete(userDetails.toUser())
                .subscribeOn(backgroundScheduler)
                .subscribeBy(
                    onError = { Timber.e(it) },
                    onComplete = { Timber.d("Saved user in favourites") }
                )
        )
    }

    override fun destroy() {
        super.destroy()
        compositeDisposable.dispose()
    }
}

private fun UserDetails.toUser(): FavouriteUser = FavouriteUser(login, id, type, avatarUrl, company, bio)
