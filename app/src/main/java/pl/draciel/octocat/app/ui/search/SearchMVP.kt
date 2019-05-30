package pl.draciel.octocat.app.ui.search

import io.reactivex.Observable
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.core.mvp.BaseView
import pl.draciel.octocat.core.mvp.LifecyclePresenter

internal interface SearchMVP {

    interface View : BaseView {
        fun observeOnSearchChanged(): Observable<String>
        fun observeOnSearchSubmit(): Observable<String>
        fun updateResults(users: List<User>)
    }

    interface Presenter : LifecyclePresenter<View>

}
