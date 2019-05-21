package pl.draciel.octocat.app.ui.search

import pl.draciel.octocat.core.mvp.BaseView
import pl.draciel.octocat.core.mvp.LifecyclePresenter

internal interface SearchMVP {

    interface View : BaseView {

    }

    interface Presenter : LifecyclePresenter<View> {

    }

}
