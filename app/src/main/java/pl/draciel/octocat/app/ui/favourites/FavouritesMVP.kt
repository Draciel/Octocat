package pl.draciel.octocat.app.ui.favourites

import pl.draciel.octocat.app.model.FavouriteUser
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.core.mvp.BaseView
import pl.draciel.octocat.core.mvp.LifecyclePresenter

interface FavouritesMVP {

    interface View : BaseView {
        fun setUsers(users: List<FavouriteUser>)
    }

    interface Presenter : LifecyclePresenter<View> {
        fun loadFavouriteUsers()
    }
}
