package pl.draciel.octocat.app.ui.userdetails

import pl.draciel.octocat.app.model.UserDetails
import pl.draciel.octocat.core.mvp.BaseView
import pl.draciel.octocat.core.mvp.LifecyclePresenter

interface UserDetailsMVP {

    interface View : BaseView {
        fun setUserDetails(user: UserDetails)
        fun setFavouriteChecked(checked: Boolean)
    }

    interface Presenter : LifecyclePresenter<View> {
        fun loadUserDetails(login: String)
        fun saveUserInFavourites(userDetails: UserDetails)
        fun removeUserFromFavourites(userDetails: UserDetails)
    }

}
