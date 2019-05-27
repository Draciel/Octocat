package pl.draciel.octocat.core.mvp

interface LifecyclePresenter<T : BaseView> {
    fun attachView(view: T)
    fun destroy()
    fun detachView()
    fun isViewAttached(): Boolean
}
